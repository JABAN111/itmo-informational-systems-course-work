package lab.`is`.bank.security

import lab.`is`.bank.authorization.database.entity.Staff

interface AuthUtilsService {
    fun getCurrentStaff(): Staff
}
