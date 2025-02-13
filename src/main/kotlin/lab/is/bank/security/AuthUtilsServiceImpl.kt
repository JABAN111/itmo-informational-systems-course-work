package lab.`is`.bank.security

import lab.`is`.bank.authorization.database.entity.Staff
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthUtilsServiceImpl : AuthUtilsService {
    override fun getCurrentStaff(): Staff {
        val context = SecurityContextHolder.getContext()
        return context.authentication.principal as Staff
    }
}
