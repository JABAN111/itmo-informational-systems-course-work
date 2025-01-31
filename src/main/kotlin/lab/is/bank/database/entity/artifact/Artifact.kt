package lab.`is`.bank.database.entity.artifact

import jakarta.persistence.*
import lab.`is`.bank.database.entity.Client
import java.util.*

/**
 * entity Artifacts {
 *     + artifact_id : UUID <<PK>>
 *     --
 *     name : VARCHAR
 *     magical_properties : UUID <<FK>>
 *     current_user_id : UUID <<FK>>
 *     history_id : UUID <<FK>>
 *     created_at : TIMESTAMP
 * }
 */
@Entity
class Artifact {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var uuid: UUID

    lateinit var name: String

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "magical_property_uuid")
    lateinit var magicalProperty: MagicalProperty

    @ManyToOne
    @JoinColumn(name = "current_client_passport_id")
    lateinit var currentClient: Client

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "history_uuid")
    lateinit var history: ArtifactHistory

    lateinit var createdAt: Date

    @PrePersist
    fun prePersist() {
        createdAt = Date()
    }
}