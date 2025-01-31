package lab.`is`.bank.database.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "Client")
class Client {
    @Id
    lateinit var passportID: String
}