package lab.`is`.bank.artifact.service.impl

import jakarta.transaction.Transactional
import lab.`is`.bank.artifact.database.entity.Artifact
import lab.`is`.bank.artifact.database.entity.ArtifactHistory
import lab.`is`.bank.artifact.database.repository.ArtifactHistoryRepository
import lab.`is`.bank.artifact.dto.ArtifactHistoryDto
import lab.`is`.bank.artifact.mapper.ArtifactHistoryMapper
import lab.`is`.bank.artifact.service.interfaces.ArtifactHistoryService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@Transactional
class ArtifactHistoryServiceImpl(
    private val artifactHistoryRepository: ArtifactHistoryRepository,
) : ArtifactHistoryService {
    override fun save(artifactHistory: ArtifactHistory): ArtifactHistory = artifactHistoryRepository.save(artifactHistory)

    override fun save(artifactHistoryDto: ArtifactHistoryDto): ArtifactHistory = save(ArtifactHistoryMapper.toEntity(artifactHistoryDto))

    override fun delete(artifactName: String) {
        artifactHistoryRepository.deleteByArtifactName(artifactName)
    }

    override fun getArtifactHistory(uuid: UUID): ArtifactHistory? =
        artifactHistoryRepository.findById(uuid).let {
            if (it.isEmpty) null else it.get()
        }

    override fun getArtifactHistoryByArtifactName(artifact: Artifact): ArtifactHistory? =
        artifactHistoryRepository.findArtifactHistoriesByArtifact(artifact)
}
