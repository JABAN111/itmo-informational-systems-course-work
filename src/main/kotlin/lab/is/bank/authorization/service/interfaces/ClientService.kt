package lab.`is`.bank.authorization.service.interfaces

import lab.`is`.bank.authorization.database.entity.Client
import lab.`is`.bank.authorization.dto.ClientDto

interface ClientService {

    fun save(clientDto: ClientDto): Client

    fun getIfClientExists(passportID: String): Client
    fun get(passportID: String): Client?

    fun saveOrGet(clientDto: ClientDto): Client
}