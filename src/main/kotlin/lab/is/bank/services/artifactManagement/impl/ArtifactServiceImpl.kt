package lab.`is`.bank.services.artifactManagement.impl

import jakarta.transaction.Transactional
import lab.`is`.bank.database.entity.artifact.Artifact
import lab.`is`.bank.database.repository.artifactManagement.ArtifactRepository
import lab.`is`.bank.dto.artifact.ArtifactDto
import lab.`is`.bank.mapper.artifact.ArtifactMapper
import lab.`is`.bank.services.artifactManagement.interfaces.ArtifactService
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class ArtifactServiceImpl(
    private val artifactRepository: ArtifactRepository
) : ArtifactService {
    override fun save(artifact: Artifact): Artifact {
        return artifactRepository.save(artifact)
    }

    override fun save(artifactDto: ArtifactDto): Artifact {
        return save(ArtifactMapper.toEntity(artifactDto))
    }

    override fun getArtifactById(artifactId: UUID): Artifact {
        return artifactRepository.findByUuid(artifactId)
            ?: throw NoSuchElementException("Artifact with id $artifactId not found")
    }

}