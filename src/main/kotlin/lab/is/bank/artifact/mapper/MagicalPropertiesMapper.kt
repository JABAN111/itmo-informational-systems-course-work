package lab.`is`.bank.artifact.mapper

import lab.`is`.bank.artifact.database.entity.MagicalProperty
import lab.`is`.bank.artifact.dto.MagicalPropertyDto

class MagicalPropertiesMapper {
    companion object {
        fun toEntity(dto: MagicalPropertyDto): MagicalProperty {
            val magicalProperty = MagicalProperty()
            magicalProperty.dangerLevel = dto.dangerLevel
            magicalProperty.property = dto.property

            return magicalProperty
        }
    }
}
