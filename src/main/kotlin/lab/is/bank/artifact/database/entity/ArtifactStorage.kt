package lab.`is`.bank.artifact.database.entity

import jakarta.persistence.*
import java.util.*

@Entity
class ArtifactStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var uuid : UUID

    @ManyToOne
    @JoinColumn
    lateinit var artifact: Artifact
}