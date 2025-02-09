package lab.`is`.bank.artifact.database.entity

import jakarta.persistence.*
import lab.`is`.bank.authorization.database.entity.Client
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
    lateinit var name: String

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "magical_property_uuid")
    lateinit var magicalProperty: MagicalProperty

    @ManyToOne
    @JoinColumn(name = "current_client_passport_id")
    lateinit var currentClient: Client


    lateinit var createdAt: Date

    @PrePersist
    fun prePersist() {
        createdAt = Date()
    }
}