package lab.`is`.bank.services.artifactManagement.interfaces

import lab.`is`.bank.database.entity.artifact.Key
import lab.`is`.bank.dto.ClientDto
import lab.`is`.bank.dto.artifact.ArtifactDto

interface KeyServiceProcessing {

    fun getKey(artifactDto: ArtifactDto, clientPassport: String): Key
    fun generatePdfKey(key: Key): ByteArray

    fun getAllKeys(clientDto: ClientDto): List<Key>
}