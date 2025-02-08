package lab.`is`.bank.authorization.database.entity

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

/**
 *
 * Экземпляр сущности "Сотрудник", то есть банкира. Нужен в основном для логирования
 */

@Entity
class Staff : UserDetails {
    @Id
    @Column(nullable = false, unique = true)
    lateinit var staffName: String

    @Enumerated(EnumType.STRING)
    var role: StaffRole? = null

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role.toString()))
    }

    override fun getPassword(): String {
        throw UnsupportedOperationException("Пароль должен быть реализован")
    }

    override fun getUsername(): String {
        return staffName
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