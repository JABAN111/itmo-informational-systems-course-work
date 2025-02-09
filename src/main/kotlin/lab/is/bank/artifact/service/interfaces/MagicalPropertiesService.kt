package lab.`is`.bank.artifact.service.interfaces

import lab.`is`.bank.artifact.database.entity.MagicalProperty
import lab.`is`.bank.artifact.dto.MagicalPropertyDto

interface MagicalPropertiesService {
    fun save(dto: MagicalPropertyDto): MagicalProperty
    fun save(magicalProperty: MagicalProperty): MagicalProperty


}