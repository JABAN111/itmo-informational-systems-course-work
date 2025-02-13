package lab.`is`.bank.authorization.service.interfaces

import lab.`is`.bank.authorization.database.entity.Staff
import lab.`is`.bank.authorization.dto.StaffDto
import org.springframework.security.core.userdetails.UserDetailsService
import java.util.*

interface StaffService {
    fun save(staff: Staff): Staff

    fun getCurrentUserUUID(): UUID

    fun getCurrentUser(): Staff

    fun getOrCreateStaff(dto: StaffDto): Staff

    fun getOrCreateStaff(staff: Staff): Staff

    fun getUserDetailsService(staff: Staff): UserDetailsService

    fun getUserDetailsService(): UserDetailsService

    fun getStaff(username: String): Staff?
}
