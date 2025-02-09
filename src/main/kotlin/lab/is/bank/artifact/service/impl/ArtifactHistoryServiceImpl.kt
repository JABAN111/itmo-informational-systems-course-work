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
): ArtifactHistoryService {
    override fun save(artifactHistory: ArtifactHistory): ArtifactHistory {
        return artifactHistoryRepository.save(artifactHistory)
    }

    override fun save(artifactHistoryDto: ArtifactHistoryDto): ArtifactHistory {
        return save(ArtifactHistoryMapper.toEntity(artifactHistoryDto))
    }

    override fun getArtifactHistory(uuid: UUID): ArtifactHistory? {
        return artifactHistoryRepository.findById(uuid).let {
            if (it.isEmpty) null else it.get()
        }
    }

    override fun getArtifactHistoryByArtifactName(artifact: Artifact): ArtifactHistory? {
        return artifactHistoryRepository.findArtifactHistoriesByArtifact(artifact)
    }

}