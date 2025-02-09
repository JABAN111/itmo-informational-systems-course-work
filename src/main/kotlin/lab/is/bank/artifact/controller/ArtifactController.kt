package lab.`is`.bank.artifact.controller

import lab.`is`.bank.artifact.database.entity.Artifact
import lab.`is`.bank.artifact.database.entity.Key
import lab.`is`.bank.authorization.dto.ClientDto
import lab.`is`.bank.artifact.dto.ArtifactDto
import lab.`is`.bank.artifact.service.interfaces.ArtifactValidationService
import lab.`is`.bank.artifact.service.interfaces.ArtifactKeysServiceProcessing
import lab.`is`.bank.artifact.service.interfaces.ArtifactService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam


@RequestMapping("/api/v0/artifact")
@RestController
@PreAuthorize("hasRole('ROLE_ARTIFACTER')")
class ArtifactController(
    private val artifactValidationService: ArtifactValidationService,
    private val artifactKeysServiceProcessing: ArtifactKeysServiceProcessing,
    private val artifactService: ArtifactService,
) {
    @PostMapping("/get-key/{why}")
    fun createKey(@RequestBody artifactDto: ArtifactDto,
                  @PathVariable why: String
    ): ResponseEntity<ByteArray> {
        val key = artifactKeysServiceProcessing.getKey(artifactDto, artifactDto.currentClient!!.passportID, why)
        val pdfBytes = artifactKeysServiceProcessing.generatePdfKey(key)

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
        return artifactKeysServiceProcessing.getAllKeys(clientDto)
    }

    @GetMapping("/reference")
    fun getReference(): List<ArtifactDto> {
        return artifactValidationService.getAllArtifact()
    }

    @DeleteMapping("/artifact/{artifactName}")
    fun deleteKey(@PathVariable artifactName: String) {
        artifactService.deleteArtifact(artifactName)
    }

}