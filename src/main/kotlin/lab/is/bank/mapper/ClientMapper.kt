package lab.`is`.bank.mapper

import lab.`is`.bank.database.entity.Client
import lab.`is`.bank.dto.ClientDto

class ClientMapper {

    companion object{

        fun toEntity(dto: ClientDto): Client {
//            require(dto.email.isNotBlank()) { "Username must not be blank" }
            require(dto.passportID.isNotBlank()) { "Passport ID must not be blank" }
            val client = Client()

            client.passportID = dto.passportID
//            client.email = dto.email
            return client
        }

    }
}