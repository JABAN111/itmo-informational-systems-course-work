package lab.`is`.bank.artifact.controller

import lab.`is`.bank.artifact.database.entity.Artifact
import lab.`is`.bank.artifact.database.entity.Key
import lab.`is`.bank.artifact.dto.ArtifactDto
import lab.`is`.bank.artifact.dto.RetrieveArtifactRequest
import lab.`is`.bank.artifact.dto.UpdateArtifactRequest
import lab.`is`.bank.artifact.service.interfaces.AiOperatorService
import lab.`is`.bank.artifact.service.interfaces.ArtifactKeysServiceProcessing
import lab.`is`.bank.authorization.dto.ClientDto
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v0/artifact")
@RestController
@PreAuthorize("hasRole('ROLE_ARTIFACTER')")
class ArtifactController(
    private val artifactValidationService: AiOperatorService,
    private val artifactKeysServiceProcessing: ArtifactKeysServiceProcessing,
    private val aiOperatorService: AiOperatorService,
) {
    @PostMapping("/get-key/{why}")
    fun createKey(
        @RequestBody artifactDto: ArtifactDto,
        @PathVariable why: String,
    ): ResponseEntity<ByteArray> {
        val key = artifactKeysServiceProcessing.getKey(artifactDto, artifactDto.currentClient!!.passportID, why)
        val pdfBytes = artifactKeysServiceProcessing.generatePdfKey(key)

        val headers =
            HttpHeaders().apply {
                contentType = MediaType.APPLICATION_PDF
                setContentDispositionFormData("attachment", "key-${key.uuid}.pdf")
            }

        return ResponseEntity
            .ok()
            .headers(headers)
            .body(pdfBytes)
    }

    @PostMapping("/get-all")
    fun getAllKeysData(
        @RequestBody clientDto: ClientDto,
    ): List<Key> = artifactKeysServiceProcessing.getAllKeys(clientDto)

    @GetMapping("/reference")
    fun getReference(): List<ArtifactDto> = artifactValidationService.getAllArtifact()

    @PostMapping("/retrieve")
    fun retrieveArtifact(
        @RequestBody request: RetrieveArtifactRequest,
    ): Artifact = artifactKeysServiceProcessing.getKey(retrievingKey = request)

    @PutMapping("/update-level")
    fun requestUpdateLevel(
        @RequestBody request: UpdateArtifactRequest,
    ) {
        aiOperatorService.requestUpdate(request)
    }
}
