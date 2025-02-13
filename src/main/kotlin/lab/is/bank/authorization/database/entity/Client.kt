package lab.`is`.bank.authorization.database.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "Client")
class Client {
    @Id
    lateinit var passportID: String


}