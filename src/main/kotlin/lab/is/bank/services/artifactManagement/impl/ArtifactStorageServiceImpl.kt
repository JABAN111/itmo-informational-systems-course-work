package lab.`is`.bank.services.artifactManagement.impl

import lab.`is`.bank.database.entity.artifact.ArtifactStorage
import lab.`is`.bank.database.repository.artifactManagement.ArtifactStorageRepository
import lab.`is`.bank.dto.artifact.ArtifactStorageDto
import lab.`is`.bank.services.artifactManagement.interfaces.ArtifactStorageService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ArtifactStorageServiceImpl(
    private val artifactStorageRepository: ArtifactStorageRepository
) : ArtifactStorageService {
    override fun save(dto: ArtifactStorageDto): ArtifactStorage {
        TODO("Not yet implemented")
    }

    override fun save(artifactStorage: ArtifactStorage): ArtifactStorage {
        return artifactStorageRepository.save(artifactStorage)
    }

    override fun getInfo(id: UUID): ArtifactStorage {
        val result = artifactStorageRepository.findByUuid(id)
        println(result)
        if(result != null) {
            return result
        }
        TODO("da")
    }


}