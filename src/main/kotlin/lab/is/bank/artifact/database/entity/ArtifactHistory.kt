package lab.`is`.bank.artifact.database.entity

import jakarta.persistence.*
import lab.`is`.bank.authorization.database.entity.Client
import java.sql.Timestamp
import java.util.UUID

@Entity
class ArtifactHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var uuid: UUID

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "artifact_history_clients",
        joinColumns = [JoinColumn(name = "artifact_history_id")],
        inverseJoinColumns = [JoinColumn(name = "client_id")]
    )
    var clientsHistory: MutableList<Client> = mutableListOf()

    var changeDate: Timestamp = Timestamp(System.currentTimeMillis())

    lateinit var reasonToSave: String

    @PreUpdate
    fun updateChangeDate() {
        changeDate = Timestamp(System.currentTimeMillis())
    }
}