package lab.`is`.bank.artifact.controller

import lab.`is`.bank.artifact.database.entity.ArtifactStorage
import lab.`is`.bank.artifact.service.impl.ArtifactKeysServiceProcessing
import lab.`is`.bank.artifact.service.interfaces.ArtifactStorageService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v0/storage")
class StorageController(
    private val artifactStorageService: ArtifactStorageService,
    private val artifactKeysServiceProcessing: ArtifactKeysServiceProcessing
) {
    @GetMapping("get-info/{uuid}")
    fun getInfoAboutStorage(@PathVariable uuid: String): ArtifactStorage {
        return artifactStorageService.get(UUID.fromString(uuid))!!
    }

    @DeleteMapping("/artifact/{artifactName}/{passport}")
    fun deleteKey(@PathVariable artifactName: String, @PathVariable passport: String) {
        artifactKeysServiceProcessing.takeArtifact(artifactName, passport)
    }

}