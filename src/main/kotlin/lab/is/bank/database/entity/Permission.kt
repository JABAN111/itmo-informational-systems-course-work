package lab.`is`.bank.database.entity

import jakarta.annotation.Nonnull
import jakarta.persistence.*
import java.util.UUID

@Entity
open class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var long: UUID? = null

    @Nonnull
    open var name: String? = null

    open var description: String? = null
}