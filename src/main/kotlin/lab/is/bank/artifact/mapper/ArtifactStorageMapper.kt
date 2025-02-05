package lab.`is`.bank.artifact.mapper

import lab.`is`.bank.artifact.database.entity.ArtifactStorage
import lab.`is`.bank.artifact.dto.ArtifactStorageDto

class ArtifactStorageMapper {
    companion object {
        fun toEntity(dto: ArtifactStorageDto): ArtifactStorage {
            val artifactStorage = ArtifactStorage()
            artifactStorage.artifact = ArtifactMapper.toEntity(dto.artifact)

            return artifactStorage
        }
    }
}