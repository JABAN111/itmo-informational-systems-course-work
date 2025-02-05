package lab.`is`.bank.authorization.database.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

/**
 *
 * Экземпляр сущности "Сотрудник", то есть банкира. Нужен в основном для логирования
 */

@Entity
class Staff : UserDetails {

    @Id
    lateinit var uuid: UUID

    lateinit var staff_name: String

    @Enumerated(EnumType.STRING)
    var role: StaffRole? = null

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role.toString()))
    }

    override fun getPassword(): String {
        throw UnsupportedOperationException("Пароль должен быть реализован")
    }

    override fun getUsername(): String {
        return staff_name
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}