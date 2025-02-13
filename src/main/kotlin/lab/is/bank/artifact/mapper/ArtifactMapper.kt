package lab.`is`.bank.artifact.mapper

import lab.`is`.bank.artifact.database.entity.Artifact
import lab.`is`.bank.artifact.dto.ArtifactDto
import lab.`is`.bank.authorization.mapper.ClientMapper

class ArtifactMapper {
    companion object {
        fun toEntity(dto: ArtifactDto): Artifact {
            val artifact = Artifact()
            artifact.name = dto.name

            val magicalPropertyDto = dto.magicalProperty
            if (magicalPropertyDto != null) {
                artifact.magicalProperty = MagicalPropertiesMapper.toEntity(magicalPropertyDto)
            }

            artifact.currentClient = dto.currentClient?.let { ClientMapper.toEntity(it) }!!

            return artifact
        }
    }
}
