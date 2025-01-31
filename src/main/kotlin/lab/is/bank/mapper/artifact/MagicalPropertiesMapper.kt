package lab.`is`.bank.mapper.artifact

import lab.`is`.bank.database.entity.artifact.MagicalProperty
import lab.`is`.bank.dto.artifact.MagicalPropertyDto

class MagicalPropertiesMapper {
    companion object{
        fun toEntity(dto: MagicalPropertyDto): MagicalProperty {

            val magicalProperty = MagicalProperty()
            magicalProperty.dangerLevel = dto.dangerLevel

            return magicalProperty
        }
    }
}