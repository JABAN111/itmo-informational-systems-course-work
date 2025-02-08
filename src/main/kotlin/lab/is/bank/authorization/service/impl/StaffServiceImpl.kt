package lab.`is`.bank.authorization.service.impl

import jakarta.transaction.Transactional
import lab.`is`.bank.authorization.database.entity.Staff
import lab.`is`.bank.authorization.database.repository.StaffRepository
import lab.`is`.bank.authorization.dto.StaffDto
import lab.`is`.bank.authorization.mapper.StaffMapper
import lab.`is`.bank.authorization.service.interfaces.StaffService
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class StaffServiceImpl(
    private val staffRepository: StaffRepository,
) : StaffService {

    private val logger = LoggerFactory.getLogger(StaffServiceImpl::class.java)

    override fun getOrCreateStaff(): Staff {
        TODO("вместо тысячи слов")
    }
//        val token = try {
//            SecurityContextHolder
//                .getContext()
//                .authentication
//                .credentials as Jwt
//        } catch (e: Exception) {
//            logger.error("Error retrieving JWT from security context", e)
//            throw RuntimeException("Token is not available")
//        }
//
//        val userUuid: UUID = UUID.fromString(token.subject)
//        val client: Optional<Staff> = staffRepository.findById(userUuid)
//
//        if (client.isPresent) {
//            logger.info("User found with UUID: $userUuid")
//            return client.get() // Если пользователь найден, возвращаем его
//        }
//
//        // Если пользователь не найден, создаем нового
//        val clientToCreate = Staff().apply {
//            uuid = userUuid
//            staffName = token.getClaim("preferred_username")
//        }
//        logger.info("Creating new user with UUID: $userUuid")
//        return staffRepository.save(clientToCreate)
//
//    }

    override fun getOrCreateStaff(dto: StaffDto): Staff {
        val newStaff = StaffMapper.toEntity(dto)
        return getOrCreateStaff(newStaff)
    }

//    @Transactional

//    @Transactional
//    override fun getOrCreateStaff(staff: Staff): Staff {
//        synchronized(this) { // Блокируем вызовы на уровне JVM
//            val existingStaff = staffRepository.findByStaffName(staff.staffName)
//            if (existingStaff != null) {
//                if (staff.role != existingStaff.role) {
//                    existingStaff.role = staff.role
//                    return staffRepository.save(existingStaff)
//                }
//                return existingStaff
//            }
//
//            return try {
//                staffRepository.save(staff)
//            } catch (ex: DataIntegrityViolationException) {
//                // Если в момент сохранения другой поток уже создал пользователя, просто достаём его
//                staffRepository.findByStaffName(staff.staffName) ?: throw ex
//            }
//        }
//    }

    @Transactional
    override fun getOrCreateStaff(staff: Staff): Staff {
        // Сначала пытаемся найти пользователя
        val existingStaff = staffRepository.findByStaffName(staff.staffName)
        if (existingStaff != null) {
            if (staff.role != existingStaff.role) {
                existingStaff.role = staff.role
                return staffRepository.save(existingStaff)
            }
            return existingStaff
        }

        return try {
            // Если пользователя нет — создаём
            staffRepository.save(staff)
        } catch (ex: DataIntegrityViolationException) {
            // Если другой поток уже создал пользователя, просто загружаем его из БД
            staffRepository.findByStaffName(staff.staffName) ?: throw ex
        }
    }


    override fun getCurrentUserUUID(): UUID {
        val token = try {
            SecurityContextHolder
                .getContext()
                .authentication
                .credentials as Jwt
        } catch (e: Exception) {
            logger.error("Error retrieving JWT from security context", e)
            throw RuntimeException("Token is not available")
        }

        return UUID.fromString(token.subject) // Извлекаем UUID из токена
    }

    // Получение текущего пользователя
    override fun getCurrentUser(): Staff {
        val userUuid = getCurrentUserUUID()
        return staffRepository.findById(userUuid).orElseThrow {
            logger.error("User not found for UUID: $userUuid")
            RuntimeException("User not found for UUID: $userUuid")
        }
    }

    override fun save(staff: Staff): Staff {
       return staffRepository.save(staff)
    }

    // Метод сохранения нового сотрудника, если это необходимо
    override fun save(): Staff {
        TODO("да")
        val jwtToken = try {
            SecurityContextHolder
                .getContext()
                .authentication
                .credentials as Jwt
        } catch (e: Exception) {
            logger.error("Error retrieving JWT from security context", e)
            throw RuntimeException("Token is not available")
        }

        val uuid = jwtToken.subject
        val client = Staff().apply {
//            this.uuid = UUID.fromString(uuid)
            this.staffName = jwtToken.getClaim("preferred_username")
        }
        logger.info("Saving staff with UUID: $uuid")
        return client
    }

    fun getStaffByUsername(staffName: String): Staff? {
        return staffRepository.findByStaffName(staffName)
    }

    override fun getUserDetailsService(staff: Staff): UserDetailsService {
        getOrCreateStaff(staff)
        return UserDetailsService { username -> getStaffByUsername(username) }
    }

    override fun getUserDetailsService(): UserDetailsService {
        return UserDetailsService { username -> getStaffByUsername(username) }
    }

    override fun getStaff(username: String): Staff? {
        return staffRepository.findByStaffName(username)
    }
}