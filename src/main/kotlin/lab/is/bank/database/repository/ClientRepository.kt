package lab.`is`.bank.database.repository

import lab.`is`.bank.database.entity.Client
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository : JpaRepository< Client, String > {
    fun findByPassportID(passportID: String): Client?
}