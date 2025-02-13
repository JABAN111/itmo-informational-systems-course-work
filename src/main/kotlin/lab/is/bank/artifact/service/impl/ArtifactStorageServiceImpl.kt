package lab.`is`.bank.artifact.service.impl

import jakarta.transaction.Transactional
import lab.`is`.bank.artifact.database.entity.ArtifactStorage
import lab.`is`.bank.artifact.database.repository.ArtifactStorageRepository
import lab.`is`.bank.artifact.dto.ArtifactStorageDto
import lab.`is`.bank.artifact.mapper.ArtifactStorageMapper
import lab.`is`.bank.artifact.service.interfaces.ArtifactStorageService
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class ArtifactStorageServiceImpl(
    private val artifactStorageRepository: ArtifactStorageRepository,
) : ArtifactStorageService {
    override fun save(dto: ArtifactStorageDto): ArtifactStorage = save(ArtifactStorageMapper.toEntity(dto))

    override fun save(artifactStorage: ArtifactStorage): ArtifactStorage = artifactStorageRepository.save(artifactStorage)

    override fun get(id: UUID): ArtifactStorage? = artifactStorageRepository.findByUuid(id)

    override fun get(artifactName: String): ArtifactStorage? {
        val res = artifactStorageRepository.findByArtifactName(artifactName)

        if (res.isNotEmpty()) {
            println(res.values)
            res["uuid"]

            val uuid = UUID.fromString(res["uuid"].toString())

            return artifactStorageRepository.findById(uuid).orElse(null)
        }

        return null
    }

    override fun delete(uuid: UUID) = artifactStorageRepository.deleteById(uuid)
}
