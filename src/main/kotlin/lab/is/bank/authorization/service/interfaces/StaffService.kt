package lab.`is`.bank.authorization.service.interfaces

import lab.`is`.bank.authorization.database.entity.Staff
import java.util.UUID

interface StaffService {
    fun save(): Staff
    fun getCurrentUserUUID(): UUID
    fun getCurrentUser(): Staff
    fun getOrCreateStaff(): Staff
}