package lab.`is`.bank.database.entity



import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.*

@Entity
class ArtifactStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id : UUID

    lateinit var name : String

}