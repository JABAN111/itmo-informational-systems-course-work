package lab.`is`.bank.authorization.service.impl

import lab.`is`.bank.authorization.database.entity.Staff
import lab.`is`.bank.authorization.database.repository.StaffRepository
import lab.`is`.bank.authorization.service.interfaces.StaffService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import java.util.*

@Service
class StaffServiceImpl(
    private val staffRepository: StaffRepository,
) : StaffService {

    private val logger = LoggerFactory.getLogger(StaffServiceImpl::class.java)

    // Метод для создания или получения существующего пользователя
    override fun getOrCreateStaff(): Staff {
        val token = try {
            SecurityContextHolder
                .getContext()
                .authentication
                .credentials as Jwt
        } catch (e: Exception) {
            logger.error("Error retrieving JWT from security context", e)
            throw RuntimeException("Token is not available")
        }

        val userUuid: UUID = UUID.fromString(token.subject)
        val client: Optional<Staff> = staffRepository.findById(userUuid)

        if (client.isPresent) {
            logger.info("User found with UUID: $userUuid")
            return client.get() // Если пользователь найден, возвращаем его
        }

        // Если пользователь не найден, создаем нового
        val clientToCreate = Staff().apply {
            uuid = userUuid
            staff_name = token.getClaim("preferred_username")
        }
        logger.info("Creating new user with UUID: $userUuid")
        return staffRepository.save(clientToCreate) // Сохраняем нового пользователя в базу данных
    }

    // Получение UUID текущего пользователя
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

    // Метод сохранения нового сотрудника, если это необходимо
    override fun save(): Staff {
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
            this.uuid = UUID.fromString(uuid)
            this.staff_name = jwtToken.getClaim("preferred_username")
        }
        logger.info("Saving staff with UUID: $uuid")
        return client
    }
}