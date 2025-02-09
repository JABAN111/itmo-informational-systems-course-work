package lab.`is`.bank.artifact.service.impl

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import lab.`is`.bank.artifact.database.entity.Key
import lab.`is`.bank.artifact.database.repository.KeyRepository
import lab.`is`.bank.authorization.dto.ClientDto
import lab.`is`.bank.authorization.mapper.ClientMapper
import lab.`is`.bank.artifact.service.exceptions.ArtifactExceptions
import lab.`is`.bank.authorization.service.interfaces.ClientService
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory
import org.springframework.stereotype.Service
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.util.*
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import lab.`is`.bank.artifact.database.entity.ArtifactHistory
import lab.`is`.bank.artifact.dto.*
import lab.`is`.bank.artifact.service.exceptions.UsedBanWord
import lab.`is`.bank.artifact.service.interfaces.*
import lab.`is`.bank.artifact.service.interfaces.ArtifactKeysServiceProcessing
import lab.`is`.bank.common.exception.ObjectAlreadyExistException
import lab.`is`.bank.security.AuthUtilsService
import org.springframework.dao.DuplicateKeyException
import javax.crypto.SecretKey

@Service
@Transactional
class ArtifactKeysServiceProcessing(
    private val aiProcessingService: ArtifactValidationService,
    private val clientService: ClientService,
    private val keyRepository: KeyRepository,//todo удалить
    private val em: EntityManager,
    private val artifactService: ArtifactService,
    private val authUtilsService: AuthUtilsService,
    private val artifactStorageService: ArtifactStorageService,
    private val artifactHistoryService: ArtifactHistoryService,
    private val keyService: KeyService,
) : ArtifactKeysServiceProcessing {
    private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    override fun getKey(artifactDto: ArtifactDto, clientPassport: String, reasonToSave: String): Key {
        val lvl: String = aiProcessingService.levelOfDanger(artifactDto.name)
        val magicProperty = aiProcessingService.getSpecification(artifactName = artifactDto.name)

        artifactDto.magicalProperty = MagicalPropertyDto(dangerLevel = lvl, property = magicProperty)

        if (!aiProcessingService.validateArtifact(artifactDto.name, clientPassport)) {
            throw ArtifactExceptions("Artifact is too dangerous! Level of danger: $lvl or user $clientPassport banned")
        }
        if (!aiProcessingService.validateDescription(reasonToSave)) {//случай, если пользователь закинул банворд
            aiProcessingService.addBannedUser(clientPassport)
            throw UsedBanWord("User used ban word")
        }

        val clientEntity = clientService.saveOrGet(ClientDto(passportID = clientPassport))
        em.flush()

        val artifactEntity = try {
            artifactService.save(artifactDto)
        } catch (e: DuplicateKeyException) {
            throw ObjectAlreadyExistException("Artifact with name ${artifactDto.name} already exists")
        }
        em.flush()

        val artifactStorageEntity = artifactStorageService.save(
            dto = ArtifactStorageDto(artifact = artifactDto)
        )

        val artifactHistoryEntity: ArtifactHistory? = artifactHistoryService.getArtifactHistoryByArtifactName(artifactEntity)
        if(artifactHistoryEntity == null) {
            artifactHistoryService.save(
                ArtifactHistoryDto(
                    artifact = artifactDto,
                    reasonToSave = reasonToSave,
                    clientsHistory = mutableListOf(clientEntity)
                )
            )
        }else{
            artifactHistoryEntity.apply { this.clientsHistory.add(clientEntity) }
            artifactHistoryService.save(artifactHistoryEntity)
        }

        val keyEntity = keyService.saveKey(
            Key().apply {
                this.artifactStorage = artifactStorageEntity
                this.client = clientEntity
                this.jwtToken = generateJwtToken(
                    artifactName = artifactEntity.name,
                    clientPassport = clientPassport
                )
                this.giver = authUtilsService.getCurrentStaff()
            }
        )

        return keyEntity
    }

    override fun generatePdfKey(key: Key): ByteArray {
        PDDocument().use { document ->
            val page = PDPage()
            document.addPage(page)

            PDPageContentStream(document, page).use { content ->
                content.setFont(PDType1Font.HELVETICA_BOLD, 12f)
                content.beginText()
                content.newLineAtOffset(100f, 700f)

                content.showText("Key Information")
                content.newLineAtOffset(0f, -20f)
                content.showText("UUID: ${key.uuid}")
                content.newLineAtOffset(0f, -20f)
                content.showText("Artifact Name: ${key.artifactStorage.artifact.name}")
                content.newLineAtOffset(0f, -20f)
                content.showText("Danger Level: ${key.artifactStorage.artifact.magicalProperty?.dangerLevel}")
                content.newLineAtOffset(0f, -20f)
                content.showText("Key Value: ${key.jwtToken}")
                content.newLineAtOffset(0f, -20f)
                content.showText("Issued At: ${key.issuedAt}")
                content.newLineAtOffset(0f, -20f)
                content.showText("Expires At: ${key.expiresAt}")
                content.endText()
            }

            val qrImage = generateQRCodeImage(key.jwtToken)
            val qrXObject = LosslessFactory.createFromImage(document, qrImage)

            PDPageContentStream(document, page).use { content ->
                content.drawImage(qrXObject, 100f, 500f, 150f, 150f)
            }

            val outputStream = ByteArrayOutputStream()
            document.save(outputStream)
            return outputStream.toByteArray()
        }
    }

    override fun getAllKeys(clientDto: ClientDto): List<Key> {
        return keyRepository.findByClient(ClientMapper.toEntity(clientDto))
    }

    private fun generateQRCodeImage(text: String): BufferedImage {
        val qrCodeWriter = QRCodeWriter()
        val hints = mapOf(EncodeHintType.CHARACTER_SET to "UTF-8")
        val bitMatrix: BitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200, hints)

        val width = bitMatrix.width
        val height = bitMatrix.height
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        for (x in 0 until width) {
            for (y in 0 until height) {
                image.setRGB(x, y, if (bitMatrix[x, y]) Color.BLACK.rgb else Color.WHITE.rgb)
            }
        }
        return image
    }

    private fun generateJwtToken(artifactName: String, clientPassport: String): String {
        val result = Jwts.builder()
            .setSubject("ArtifactKey")
            .claim("artifact", artifactName)
            .claim("owner", clientPassport)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24 часа
            .signWith(secretKey)
            .compact()
        return result
    }

}