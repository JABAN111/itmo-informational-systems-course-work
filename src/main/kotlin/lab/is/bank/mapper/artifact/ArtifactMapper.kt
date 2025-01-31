package lab.`is`.bank.mapper.artifact

import lab.`is`.bank.database.entity.artifact.Artifact
import lab.`is`.bank.dto.artifact.ArtifactDto
import lab.`is`.bank.mapper.ClientMapper

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