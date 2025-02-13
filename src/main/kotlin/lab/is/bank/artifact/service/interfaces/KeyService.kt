package lab.`is`.bank.artifact.service.interfaces

import lab.`is`.bank.artifact.database.entity.ArtifactStorage
import lab.`is`.bank.artifact.database.entity.Key
import lab.`is`.bank.artifact.dto.KeyDto
import java.util.*

interface KeyService {
    fun get(uuid: UUID): Key?

    fun save(key: Key): Key

    fun delete(clientPassport: String)

    fun delete(storage: ArtifactStorage)

    fun save(keyDto: KeyDto): Key
}
