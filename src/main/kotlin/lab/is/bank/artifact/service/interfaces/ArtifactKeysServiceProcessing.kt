package lab.`is`.bank.artifact.service.interfaces

import lab.`is`.bank.artifact.database.entity.Artifact
import lab.`is`.bank.artifact.database.entity.Key
import lab.`is`.bank.artifact.dto.ArtifactDto
import lab.`is`.bank.artifact.dto.RetrieveArtifactRequest
import lab.`is`.bank.authorization.dto.ClientDto

interface ArtifactKeysServiceProcessing {
    fun getKey(
        artifactDto: ArtifactDto,
        clientPassport: String,
        reasonToSave: String,
    ): Key

    fun getKey(retrievingKey: RetrieveArtifactRequest): Artifact

    fun generatePdfKey(key: Key): ByteArray

    fun takeArtifact(
        artifactName: String,
        clientPassport: String,
    )

    fun getAllKeys(clientDto: ClientDto): List<Key>
}
