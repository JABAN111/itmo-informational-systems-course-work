package lab.`is`.bank.services.artifactManagement.interfaces

import lab.`is`.bank.database.entity.artifact.ArtifactStorage
import lab.`is`.bank.dto.artifact.ArtifactStorageDto
import java.util.*

interface ArtifactStorageService {
    fun save(dto: ArtifactStorageDto): ArtifactStorage
    fun save(artifactStorage: ArtifactStorage): ArtifactStorage

    fun getInfo(id: UUID): ArtifactStorage
}