package lab.`is`.bank.services.auth.interfaces

import lab.`is`.bank.database.entity.Staff
import java.util.UUID

interface StaffService {
    fun save(): Staff
    fun getCurrentUserUUID(): UUID
    fun getCurrentUser(): Staff
    fun getOrCreateStaff(): Staff
}