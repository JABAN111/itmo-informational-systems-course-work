package lab.`is`.bank.artifact.mapper

import lab.`is`.bank.artifact.database.entity.Key
import lab.`is`.bank.artifact.dto.KeyDto
import lab.`is`.bank.authorization.mapper.ClientMapper

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