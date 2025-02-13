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

    override fun getOrCreateStaff(dto: StaffDto): Staff {
        val newStaff = StaffMapper.toEntity(dto)
        return getOrCreateStaff(newStaff)
    }

    @Transactional
    override fun getOrCreateStaff(staff: Staff): Staff {
        val existingStaff = staffRepository.findByStaffName(staff.staffName)
        if (existingStaff != null) {
            if (staff.role != existingStaff.role) {
                existingStaff.role = staff.role
                return staffRepository.save(existingStaff)
            }
            return existingStaff
        }

        return try {
            staffRepository.save(staff)
        } catch (ex: DataIntegrityViolationException) {
            staffRepository.findByStaffName(staff.staffName) ?: throw ex
        }
    }

    override fun getCurrentUserUUID(): UUID {
        val token =
            try {
                SecurityContextHolder
                    .getContext()
                    .authentication
                    .credentials as Jwt
            } catch (e: Exception) {
                logger.error("Error retrieving JWT from security context", e)
                throw RuntimeException("Token is not available")
            }

        return UUID.fromString(token.subject)
    }

    override fun getCurrentUser(): Staff {
        val userUuid = getCurrentUserUUID()
        return staffRepository.findById(userUuid).orElseThrow {
            logger.error("User not found for UUID: $userUuid")
            RuntimeException("User not found for UUID: $userUuid")
        }
    }

    override fun save(staff: Staff): Staff = staffRepository.save(staff)

    fun getStaffByUsername(staffName: String): Staff? = staffRepository.findByStaffName(staffName)

    override fun getUserDetailsService(staff: Staff): UserDetailsService {
        getOrCreateStaff(staff)
        return UserDetailsService { username -> getStaffByUsername(username) }
    }

    override fun getUserDetailsService(): UserDetailsService = UserDetailsService { username -> getStaffByUsername(username) }

    override fun getStaff(username: String): Staff? = staffRepository.findByStaffName(username)
}
