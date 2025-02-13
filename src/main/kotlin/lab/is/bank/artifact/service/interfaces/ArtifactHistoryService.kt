package lab.`is`.bank.artifact.service.interfaces

import lab.`is`.bank.artifact.database.entity.Artifact
import lab.`is`.bank.artifact.database.entity.ArtifactHistory
import lab.`is`.bank.artifact.dto.ArtifactHistoryDto
import java.util.UUID

interface ArtifactHistoryService {
    fun save(artifactHistory: ArtifactHistory): ArtifactHistory

    fun save(artifactHistoryDto: ArtifactHistoryDto): ArtifactHistory

    fun delete(artifactName: String)

    fun getArtifactHistory(uuid: UUID): ArtifactHistory?

    fun getArtifactHistoryByArtifactName(artifact: Artifact): ArtifactHistory?
}
