package lab.`is`.bank.controller.artifact

import lab.`is`.bank.database.entity.artifact.Key
import lab.`is`.bank.dto.ClientDto
import lab.`is`.bank.dto.artifact.ArtifactDto
import lab.`is`.bank.dto.artifact.ArtifactHistoryDto
import lab.`is`.bank.services.artifactManagement.interfaces.ArtifactValidationService
import lab.`is`.bank.services.artifactManagement.interfaces.KeyServiceProcessing
import org.bouncycastle.asn1.cms.CMSAttributes.contentType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*


@RequestMapping("/api/v0/artifact")
@RestController
class ArtifactController(
    private val artService: ArtifactValidationService,
    private val keyServiceProcessing: KeyServiceProcessing,
    private val artifactValidationService: ArtifactValidationService
) {

    @GetMapping("/test")
    fun req() {
        val check = ArtifactDto(
            name = "invisibility_cloak",
            currentClient = ClientDto("21"),
            artifactHistory = ArtifactHistoryDto(
                ClientDto("21"),
                "some_description"
            ),
        )

        println(artService.validateArtifact("invisibility_cloak"))
        println(keyServiceProcessing.getKey(check, "21"))
    }

    //    @PostMapping("/get-key")
//    fun getKey(@RequestBody artifactDto: ArtifactDto): ByteArray {
//        val key = keyServiceProcessing.getKey(artifactDto, artifactDto.currentClient!!.passportID)
//        return keyServiceProcessing.generatePdfKey(key)
//    }
    @PostMapping("/get-key")
    fun getKey(@RequestBody artifactDto: ArtifactDto): ResponseEntity<ByteArray> {
        val key = keyServiceProcessing.getKey(artifactDto, artifactDto.currentClient!!.passportID)
        val pdfBytes = keyServiceProcessing.generatePdfKey(key)

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_PDF
//            contentDisposition = HttpHeaders.CONTENT_DISPOSITION
            setContentDispositionFormData("attachment", "key-${key.uuid}.pdf")
        }

        return ResponseEntity.ok()
            .headers(headers)
            .body(pdfBytes)
    }

    @GetMapping("/testall")
    fun getAll(): List<ArtifactDto> {
        return artifactValidationService.getAllArtifact()
    }

}