package lab.`is`.bank.artifact.mapper

import lab.`is`.bank.artifact.database.entity.Artifact
import lab.`is`.bank.artifact.dto.ArtifactDto
import lab.`is`.bank.authorization.mapper.ClientMapper

class ArtifactMapper {
    companion object{
        fun toEntity(dto: ArtifactDto): Artifact {
            val artifact = Artifact()
            artifact.name = dto.name
            if(dto.magicalProperty != null)
                artifact.magicalProperty = MagicalPropertiesMapper.toEntity(dto.magicalProperty)
            artifact.currentClient = dto.currentClient?.let { ClientMapper.toEntity(it) }!!

            return artifact
        }
    }
}