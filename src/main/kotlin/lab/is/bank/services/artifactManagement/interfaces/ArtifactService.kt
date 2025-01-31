package lab.`is`.bank.services.artifactManagement.interfaces

import lab.`is`.bank.database.entity.artifact.Artifact
import lab.`is`.bank.dto.artifact.ArtifactDto
import java.util.UUID

interface ArtifactService {
    fun save(artifact: Artifact): Artifact
    fun save(artifactDto: ArtifactDto): Artifact

    fun getArtifactById(artifactId: UUID): Artifact

}