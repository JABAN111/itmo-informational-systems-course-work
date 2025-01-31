package lab.`is`.bank.mapper

import lab.`is`.bank.database.entity.Client
import lab.`is`.bank.dto.ClientDto

class ClientMapper {

    companion object {

        fun toEntity(dto: ClientDto): Client {
            require(dto.passportID.isNotBlank()) { "Passport ID must not be blank" }
            val client = Client()

            client.passportID = dto.passportID
            return client
        }


        fun toDto(entity: Client): ClientDto {
            val dto = ClientDto(
                passportID = entity.passportID
            )
            return dto
        }
    }
}