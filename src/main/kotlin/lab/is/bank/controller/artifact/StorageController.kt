package lab.`is`.bank.controller.artifact

import lab.`is`.bank.database.entity.artifact.ArtifactStorage
import lab.`is`.bank.services.artifactManagement.interfaces.ArtifactStorageService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v0/storage")
class StorageController(
    private val artifactStorageService: ArtifactStorageService
) {
    @GetMapping("get-info/{uuid}")
    fun getInfoAboutStorage(@PathVariable uuid: String): ArtifactStorage {
        return artifactStorageService.getInfo(UUID.fromString(uuid))
    }
}