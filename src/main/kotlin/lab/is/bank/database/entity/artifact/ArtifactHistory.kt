package lab.`is`.bank.database.entity.artifact

import jakarta.persistence.*
import lab.`is`.bank.database.entity.Client
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

    @PreUpdate
    fun updateChangeDate() {
        changeDate = Timestamp(System.currentTimeMillis())
    }
}