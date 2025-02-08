package lab.`is`.bank.artifact.service.impl

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import lab.`is`.bank.artifact.database.entity.Key
import lab.`is`.bank.artifact.database.repository.ArtifactHistoryRepository
import lab.`is`.bank.artifact.database.repository.ArtifactStorageRepository
import lab.`is`.bank.artifact.database.repository.KeyRepository
import lab.`is`.bank.artifact.database.repository.MagicalPropertyRepository
import lab.`is`.bank.authorization.dto.ClientDto
import lab.`is`.bank.artifact.dto.ArtifactDto
import lab.`is`.bank.artifact.dto.ArtifactStorageDto
import lab.`is`.bank.artifact.dto.KeyDto
import lab.`is`.bank.authorization.mapper.ClientMapper
import lab.`is`.bank.artifact.mapper.ArtifactHistoryMapper
import lab.`is`.bank.artifact.mapper.ArtifactStorageMapper
import lab.`is`.bank.artifact.mapper.KeyMapper
import lab.`is`.bank.artifact.mapper.MagicalPropertiesMapper
import lab.`is`.bank.artifact.service.exceptions.ArtifactExceptions
import lab.`is`.bank.artifact.service.interfaces.ArtifactService
import lab.`is`.bank.artifact.service.interfaces.KeyServiceProcessing
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
import lab.`is`.bank.artifact.service.exceptions.UsedBanWord
import lab.`is`.bank.artifact.service.interfaces.ArtifactValidationService
import lab.`is`.bank.authorization.mapper.StaffMapper
import lab.`is`.bank.security.AuthUtilsService
import javax.crypto.SecretKey

@Service
@Transactional
class KeyServiceProcessingImpl(
    private val artifactValidationService: ArtifactValidationService,
    private val clientService: ClientService,
    private val keyRepository: KeyRepository,
    private val em: EntityManager,
    private val artifactService: ArtifactService,
    private val authUtilsService: AuthUtilsService,
    private val artifactStorageRepository: ArtifactStorageRepository,//fixme заменить на сервис
    private val artifactHistoryRepository: ArtifactHistoryRepository,//fixme заменить на сервис
    private val magicalPropertyRepository: MagicalPropertyRepository,//fixme заменить на сервис
) : KeyServiceProcessing {
    private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    override fun getKey(artifactDto: ArtifactDto, clientPassport: String): Key {
        val lvl: String = artifactValidationService.levelOfDanger(artifactDto.name)
        val reasonToSave: String? = artifactDto.artifactHistory?.reasonToSave

        if (!artifactValidationService.validateArtifact(artifactDto.name, clientPassport)) {
            throw ArtifactExceptions("Artifact is too dangerous! Level of danger: $lvl or user $clientPassport banned")
        }
        if(reasonToSave != null && !artifactValidationService.validateDescription(reasonToSave)) {//случай, если пользователь закинул банворд
            artifactValidationService.addBannedUser(clientPassport)
            throw UsedBanWord("User used ban word")
        }

        val currentClientEntity = clientService.saveOrGet(ClientDto(clientPassport))
        artifactDto.artifactHistory?.clientsHistory?.add(currentClientEntity)

        val clientDto = ClientMapper.toDto(currentClientEntity)
        em.flush()
        val storage = ArtifactStorageDto(artifact = artifactDto)

        val jwtToken = generateJwtToken(artifactDto.name, clientPassport)

        val currentStaff = authUtilsService.getCurrentStaff()

        val keyDto = KeyDto(
            artifactStorage = storage,
            client = clientDto,
            keyValue = jwtToken,
            giver = StaffMapper.toDto(staff = currentStaff)
        )

        val artifactDto = keyDto.artifactStorage.artifact
        val artifactHistoryDto = artifactDto.artifactHistory
        val magicalPropertyDto = artifactDto.magicalProperty

        artifactHistoryDto?.lastClient = clientDto
        magicalPropertyDto?.let { magicalPropertyRepository.save(MagicalPropertiesMapper.toEntity(it)) }

        val savedHistory = artifactHistoryDto?.let { artifactHistoryRepository.save(ArtifactHistoryMapper.toEntity(it)) }!!

        val artifact = artifactService.save(artifactDto)
        em.flush()

        val savedStorage = ArtifactStorageMapper.toEntity(storage)
        savedStorage.artifact = artifact
        artifactStorageRepository.save(savedStorage)
        em.flush()

        val keyToSave = KeyMapper.toEntity(keyDto)
        savedStorage.artifact.history = savedHistory
        keyToSave.artifactStorage = savedStorage
        keyToSave.client = currentClientEntity
        val savedKey = keyRepository.save(keyToSave)

        return savedKey
    }
//    override fun getKey(artifactDto: ArtifactDto, clientPassport: String): Key {
//        val lvl: String = artifactValidationService.levelOfDanger(artifactDto.name)
//
//        if (!artifactValidationService.validateArtifact(artifactDto.name)) {
//            throw DangerousArtifactException("Artifact is too dangerous! Level of danger: $lvl")
//        }
//        val currentClientEntity = clientService.saveOrGet(ClientDto(clientPassport))
//        artifactDto.artifactHistory?.clientsHistory?.add(currentClientEntity)
//
//        val clientDto = ClientMapper.toDto(currentClientEntity)
//        em.flush()
//        val storage = ArtifactStorageDto(artifact = artifactDto)
//        val keyDto = KeyDto(
//            artifactStorage = storage,
//            client = clientDto,
//            keyValue = UUID.randomUUID()
//        )
//        val artifactDto = keyDto.artifactStorage.artifact
//        val artifactHistoryDto = artifactDto.artifactHistory
//        val magicalPropertyDto = artifactDto.magicalProperty
//
//        if (artifactHistoryDto != null) {
//            artifactHistoryDto.lastClient = clientDto
//        }
//
//        if (magicalPropertyDto != null) {
//            magicalPropertyRepository.save(MagicalPropertiesMapper.toEntity(magicalPropertyDto))
//        }
//
//        val historyToSave = artifactHistoryDto?.let { ArtifactHistoryMapper.toEntity(it) }
//        val savedHistory: ArtifactHistory = historyToSave?.let { artifactHistoryRepository.save(it) }!!
//
//        val artifact = artifactService.save(artifactDto)
//        em.flush()
//
//        val savedStorage = ArtifactStorageMapper.toEntity(storage)
//        savedStorage.artifact = artifact
//        artifactStorageRepository.save(savedStorage)
//        em.flush()
//
//        val keyToSave = KeyMapper.toEntity(keyDto)
//        savedStorage.artifact.history = savedHistory
//        keyToSave.artifactStorage = savedStorage
//        keyToSave.client = currentClientEntity
//        val savedKey = keyRepository.save(keyToSave)
//        return savedKey
//    }


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