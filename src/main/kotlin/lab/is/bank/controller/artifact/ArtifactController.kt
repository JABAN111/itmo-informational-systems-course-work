package lab.`is`.bank.controller.artifact

import lab.`is`.bank.database.entity.artifact.Key
import lab.`is`.bank.dto.ClientDto
import lab.`is`.bank.dto.artifact.ArtifactDto
import lab.`is`.bank.services.artifactManagement.interfaces.ArtifactValidationService
import lab.`is`.bank.services.artifactManagement.interfaces.KeyServiceProcessing
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType


@RequestMapping("/api/v0/artifact")
@RestController
class ArtifactController(
    private val keyServiceProcessing: KeyServiceProcessing,
    private val artifactValidationServiceImpl: lab.`is`.bank.services.artifactManagement.impl.ArtifactValidationServiceImpl,
) {
    @PostMapping("/get-key")
    fun createKey(@RequestBody artifactDto: ArtifactDto): ResponseEntity<ByteArray> {
        val key = keyServiceProcessing.getKey(artifactDto, artifactDto.currentClient!!.passportID)
        val pdfBytes = keyServiceProcessing.generatePdfKey(key)

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_PDF
            setContentDispositionFormData("attachment", "key-${key.uuid}.pdf")
        }

        return ResponseEntity.ok()
            .headers(headers)
            .body(pdfBytes)
    }
    @PostMapping("/get-all")
    fun getAllKeysData(@RequestBody clientDto: ClientDto): List<Key> {
        return keyServiceProcessing.getAllKeys(clientDto)
    }

    @GetMapping("/reference")
    fun getReference(): List<ArtifactDto> {
        val got = artifactValidationServiceImpl.getAllArtifact()
        return got
    }

}