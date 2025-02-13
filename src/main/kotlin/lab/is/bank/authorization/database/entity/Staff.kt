package lab.`is`.bank.authorization.database.entity

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

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

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(SimpleGrantedAuthority(role.toString()))

    override fun getPassword(): String = staffName

    override fun getUsername(): String = staffName

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
