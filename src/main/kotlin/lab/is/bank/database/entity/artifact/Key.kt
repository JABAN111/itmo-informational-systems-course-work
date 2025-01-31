package lab.`is`.bank.database.entity.artifact

import jakarta.persistence.*
import lab.`is`.bank.database.entity.Client
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

    lateinit var keyValue: UUID

    lateinit var issuedAt: Date
    lateinit var expiresAt: Date

    @PrePersist
    fun updateIssuedAt() {
        issuedAt = Date()
        expiresAt = Date(issuedAt.time + 1000 * 60 * 60 * 24)
    }
}

