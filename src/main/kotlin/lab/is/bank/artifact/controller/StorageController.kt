package lab.`is`.bank.artifact.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import lab.`is`.bank.artifact.database.entity.ArtifactStorage
import lab.`is`.bank.artifact.service.impl.ArtifactKeysServiceProcessing
import lab.`is`.bank.artifact.service.interfaces.ArtifactStorageService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v0/storage")
@Tag(name = "Storage", description = "Artifact storage management API")
class StorageController(
    private val artifactStorageService: ArtifactStorageService,
    private val artifactKeysServiceProcessing: ArtifactKeysServiceProcessing,
) {
    @GetMapping("/{uuid}")
    @Operation(summary = "Get storage info", description = "Retrieves information about artifact storage by UUID")
    fun getInfoAboutStorage(
        @Parameter(description = "Storage UUID") @PathVariable uuid: String,
    ): ArtifactStorage = artifactStorageService.get(UUID.fromString(uuid))!!

    @DeleteMapping("/artifact/{artifactName}/{passport}")
    @Operation(summary = "Delete artifact key", description = "Removes an artifact key from storage")
    fun deleteKey(
        @Parameter(description = "Artifact name") @PathVariable artifactName: String,
        @Parameter(description = "User passport ID") @PathVariable passport: String,
    ) {
        artifactKeysServiceProcessing.takeArtifact(artifactName, passport)
    }
}
