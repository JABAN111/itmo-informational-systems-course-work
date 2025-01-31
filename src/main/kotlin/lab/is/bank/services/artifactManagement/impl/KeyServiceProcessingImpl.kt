package lab.`is`.bank.services.artifactManagement.impl

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import lab.`is`.bank.database.entity.artifact.ArtifactHistory
import lab.`is`.bank.database.entity.artifact.Key
import lab.`is`.bank.database.repository.artifactManagement.ArtifactHistoryRepository
import lab.`is`.bank.database.repository.artifactManagement.ArtifactStorageRepository
import lab.`is`.bank.database.repository.artifactManagement.KeyRepository
import lab.`is`.bank.database.repository.artifactManagement.MagicalPropertyRepository
import lab.`is`.bank.dto.ClientDto
import lab.`is`.bank.dto.artifact.ArtifactDto
import lab.`is`.bank.dto.artifact.ArtifactStorageDto
import lab.`is`.bank.dto.artifact.KeyDto
import lab.`is`.bank.mapper.ClientMapper
import lab.`is`.bank.mapper.artifact.ArtifactHistoryMapper
import lab.`is`.bank.mapper.artifact.ArtifactStorageMapper
import lab.`is`.bank.mapper.artifact.KeyMapper
import lab.`is`.bank.mapper.artifact.MagicalPropertiesMapper
import lab.`is`.bank.services.artifactManagement.DangerousArtifactException
import lab.`is`.bank.services.artifactManagement.interfaces.ArtifactService
import lab.`is`.bank.services.artifactManagement.interfaces.KeyServiceProcessing
import lab.`is`.bank.services.auth.interfaces.ClientService
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

@Service
@Transactional
class KeyServiceProcessingImpl(
    private val artifactValidationService: ArtifactValidationService,
    private val clientService: ClientService,
    private val keyRepository: KeyRepository,
    private val em: EntityManager,
    private val artifactService: ArtifactService,
    private val artifactStorageRepository: ArtifactStorageRepository,//fixme заменить на сервис
    private val artifactHistoryRepository: ArtifactHistoryRepository,//fixme заменить на сервис
    private val magicalPropertyRepository: MagicalPropertyRepository,//fixme заменить на сервис
) : KeyServiceProcessing {

    override fun getKey(artifactDto: ArtifactDto, clientPassport: String): Key {
        val lvl: String = artifactValidationService.levelOfDanger(artifactDto.name)

        if (!artifactValidationService.validateArtifact(artifactDto.name)) {
            throw DangerousArtifactException("Artifact is too dangerous! Level of danger: $lvl")
        }
        val currentClientEntity = clientService.saveOrGet(ClientDto(clientPassport))
        artifactDto.artifactHistory?.clientsHistory?.add(currentClientEntity)

        val clientDto = ClientMapper.toDto(currentClientEntity)
        em.flush()
        val storage = ArtifactStorageDto(artifact = artifactDto)
        val keyDto = KeyDto(
            artifactStorage = storage,
            client = clientDto,
            keyValue = UUID.randomUUID()
        )
        val artifactDto = keyDto.artifactStorage.artifact
        val artifactHistoryDto = artifactDto.artifactHistory
        val magicalPropertyDto = artifactDto.magicalProperty

        if (artifactHistoryDto != null) {
            artifactHistoryDto.lastClient = clientDto
        }

        if (magicalPropertyDto != null) {
            magicalPropertyRepository.save(MagicalPropertiesMapper.toEntity(magicalPropertyDto))
        }

        val historyToSave = artifactHistoryDto?.let { ArtifactHistoryMapper.toEntity(it) }
        val savedHistory: ArtifactHistory = historyToSave?.let { artifactHistoryRepository.save(it) }!!

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
                content.showText("Key Value: ${key.keyValue}")
                content.newLineAtOffset(0f, -20f)
                content.showText("Issued At: ${key.issuedAt}")
                content.newLineAtOffset(0f, -20f)
                content.showText("Expires At: ${key.expiresAt}")
                content.endText()
            }

            val qrImage = generateQRCodeImage(key.keyValue.toString())
            val qrXObject = LosslessFactory.createFromImage(document, qrImage)

            PDPageContentStream(document, page).use { content ->
                content.drawImage(qrXObject, 100f, 500f, 150f, 150f)
            }

            val outputStream = ByteArrayOutputStream()
            document.save(outputStream)
            return outputStream.toByteArray()
        }
    }

    override fun getAllKeys(clientDto: ClientDto): List<Key>{
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
}