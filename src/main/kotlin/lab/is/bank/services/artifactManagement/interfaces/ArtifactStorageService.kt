package lab.`is`.bank.services.artifactManagement.interfaces

import lab.`is`.bank.database.entity.artifact.ArtifactStorage
import lab.`is`.bank.dto.artifact.ArtifactStorageDto

interface ArtifactStorageService {
    fun save(dto: ArtifactStorageDto): ArtifactStorage
    fun save(artifactStorage: ArtifactStorage): ArtifactStorage

}