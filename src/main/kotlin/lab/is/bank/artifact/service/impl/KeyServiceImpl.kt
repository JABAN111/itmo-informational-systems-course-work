package lab.`is`.bank.artifact.service.impl

import jakarta.transaction.Transactional
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
): KeyService {
    override fun getKeyByUuid(uuid: UUID): Key? {
        val result = keyRepository.findById(uuid)
        return if(result.isEmpty){
            null
        }else{
            result.get()
        }
    }

    override fun saveKey(key: Key): Key {
        return keyRepository.save(key)
    }

    override fun saveKey(keyDto: KeyDto): Key {
        return saveKey(KeyMapper.toEntity(keyDto))
    }
}