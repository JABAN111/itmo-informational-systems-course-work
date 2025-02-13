package lab.`is`.bank.artifact.service.impl

import jakarta.transaction.Transactional
import lab.`is`.bank.artifact.database.entity.ArtifactStorage
import lab.`is`.bank.artifact.database.entity.Key
import lab.`is`.bank.artifact.database.repository.KeyRepository
import lab.`is`.bank.artifact.dto.KeyDto
import lab.`is`.bank.artifact.mapper.KeyMapper
import lab.`is`.bank.artifact.service.interfaces.KeyService
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class KeyServiceImpl(
    private val keyRepository: KeyRepository,
) : KeyService {
    override fun get(uuid: UUID): Key? {
        val result = keyRepository.findById(uuid)
        return if (result.isEmpty) {
            null
        } else {
            result.get()
        }
    }

    override fun save(key: Key): Key = keyRepository.save(key)

    override fun save(keyDto: KeyDto): Key = save(KeyMapper.toEntity(keyDto))

    override fun delete(clientPassport: String) {
        keyRepository.deleteKeyByClientPassportID(clientPassport)
    }

    override fun delete(storage: ArtifactStorage) {
        keyRepository.deleteKeyByArtifactStorage(storage)
    }
}
