package lab.`is`.bank.artifact.service.interfaces

import lab.`is`.bank.artifact.database.entity.Key
import lab.`is`.bank.artifact.dto.KeyDto
import java.util.*

interface KeyService{
    fun getKeyByUuid(uuid: UUID): Key?
    fun saveKey(key: Key): Key
    fun saveKey(keyDto: KeyDto): Key
}