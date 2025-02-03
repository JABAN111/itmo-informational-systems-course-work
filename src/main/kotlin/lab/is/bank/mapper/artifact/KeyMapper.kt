package lab.`is`.bank.mapper.artifact

import lab.`is`.bank.database.entity.artifact.Key
import lab.`is`.bank.dto.artifact.KeyDto
import lab.`is`.bank.mapper.ClientMapper

class KeyMapper {
    companion object {
        fun toEntity(dto: KeyDto): Key {
            val key = Key()
            key.artifactStorage = ArtifactStorageMapper.toEntity(dto.artifactStorage)
            key.jwtToken = dto.keyValue
            key.client = ClientMapper.toEntity(dto.client)

            return key
        }
    }
}