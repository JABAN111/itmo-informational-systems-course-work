package lab.`is`.bank.artifact.database.entity

import jakarta.persistence.*
import lab.`is`.bank.authorization.database.entity.Client
import lab.`is`.bank.authorization.database.entity.Staff
import java.util.*

@Entity
class Key {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var uuid: UUID

    @ManyToOne
    @JoinColumn
    lateinit var artifactStorage: ArtifactStorage

    @ManyToOne
    @JoinColumn(name = "client_passport_id", referencedColumnName = "passportID")
    lateinit var client: Client

    lateinit var jwtToken: String

    lateinit var issuedAt: Date
    lateinit var expiresAt: Date

    @ManyToOne
    lateinit var giver: Staff

    @PrePersist
    fun updateIssuedAt() {
        issuedAt = Date()
        expiresAt = Date(issuedAt.time + 1000 * 60 * 60 * 24)
    }
}
