package lab.`is`.bank.artifact.service.interfaces

import lab.`is`.bank.artifact.database.entity.ArtifactStorage
import lab.`is`.bank.artifact.dto.ArtifactStorageDto
import java.util.*

interface ArtifactStorageService {
    fun save(dto: ArtifactStorageDto): ArtifactStorage

    fun save(artifactStorage: ArtifactStorage): ArtifactStorage

    fun get(id: UUID): ArtifactStorage?

    fun get(artifactName: String): ArtifactStorage?

    fun delete(uuid: UUID)
}
