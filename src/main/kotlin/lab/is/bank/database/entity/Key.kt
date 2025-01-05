package lab.`is`.bank.database.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
open class Key {
    //todo

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open var id: UUID? = null

    @Column(unique = true)
//    @ManyToOne
    open var artifactStorageId: UUID? = null


}