package lab.`is`.bank.artifact.service.interfaces

import lab.`is`.bank.artifact.database.entity.Artifact
import lab.`is`.bank.artifact.dto.ArtifactDto
import lab.`is`.bank.artifact.dto.ArtifactExportData
import java.util.UUID

interface ArtifactService {
    fun save(artifact: Artifact): Artifact
    fun save(artifactDto: ArtifactDto): Artifact

    fun getArtifactById(artifactId: UUID): Artifact
    fun getDataForExport(someOwner: String?, someMagicProperty: List<String>?) : List<ArtifactExportData>

}