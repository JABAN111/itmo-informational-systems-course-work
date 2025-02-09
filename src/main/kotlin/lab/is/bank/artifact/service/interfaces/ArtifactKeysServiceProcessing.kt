package lab.`is`.bank.artifact.service.interfaces

import lab.`is`.bank.artifact.database.entity.Key
import lab.`is`.bank.authorization.dto.ClientDto
import lab.`is`.bank.artifact.dto.ArtifactDto

interface ArtifactKeysServiceProcessing {

    fun getKey(artifactDto: ArtifactDto, clientPassport: String, reasonToSave: String): Key
    fun generatePdfKey(key: Key): ByteArray

    fun getAllKeys(clientDto: ClientDto): List<Key>
}