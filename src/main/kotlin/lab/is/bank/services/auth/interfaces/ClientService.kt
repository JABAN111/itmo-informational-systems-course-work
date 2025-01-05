package lab.`is`.bank.services.auth.interfaces

import lab.`is`.bank.database.entity.Client
import lab.`is`.bank.dto.ClientDto
import java.util.UUID

interface ClientService {

    fun save(clientDto: ClientDto) : Client

    fun getIfClientExists(passportID: String) : Client

    fun saveOrGet(clientDto: ClientDto) : Client
}