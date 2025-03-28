package lab.`is`.bank.artifact.service.interfaces

import lab.`is`.bank.artifact.database.entity.Artifact
import lab.`is`.bank.artifact.dto.ArtifactDto
import lab.`is`.bank.artifact.dto.ArtifactExportData

interface ArtifactService {
    fun save(artifact: Artifact): Artifact

    fun save(artifactDto: ArtifactDto): Artifact

    fun delete(artifactName: String)

    fun getDataForExport(
        someOwner: String?,
        someMagicProperty: List<String>?,
    ): List<ArtifactExportData>

    fun getArtifact(artifactName: String): Artifact?
}
