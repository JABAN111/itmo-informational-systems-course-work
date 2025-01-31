package lab.`is`.bank.mapper.artifact

import lab.`is`.bank.database.entity.artifact.ArtifactStorage
import lab.`is`.bank.dto.artifact.ArtifactStorageDto

class ArtifactStorageMapper {
    companion object {
        fun toEntity(dto: ArtifactStorageDto): ArtifactStorage {
            val artifactStorage = ArtifactStorage()
            artifactStorage.artifact = ArtifactMapper.toEntity(dto.artifact)

            return artifactStorage
        }
    }
}