package lab.`is`.bank.services.auth.impl

import lab.`is`.bank.database.entity.Staff
import lab.`is`.bank.database.repository.StaffRepository
import lab.`is`.bank.services.auth.interfaces.StaffService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import java.util.*

/**
 * Этот сервис необходим для корректного взаимодействия с keycloak
 */
@Service
class StaffServiceImpl(
//    private val jwtConverter: JwtAuthConverter,
    private val staffRepository: StaffRepository,
) : StaffService {

    override fun save(): Staff {
        val jwtToken = SecurityContextHolder
            .getContext()
            .authentication
            .credentials as Jwt

        val uuid = jwtToken.subject


        val client: Staff = Staff()
        client.uuid = UUID.fromString(uuid)
//        user.password = UUID.randomUUID().toString()
        client.username = jwtToken.getClaim("preferred_username")
        println(client)
        return client;

    }

    override fun getCurrentUserUUID(): UUID {
        TODO("Not yet implemented")
    }

    override fun getCurrentUser(): Staff {
        TODO("Not yet implemented")
    }


    override fun getOrCreateStaff(): Staff {
        val token = SecurityContextHolder
            .getContext()
            .authentication
            .credentials as Jwt

        val userUuid:UUID = UUID.fromString(token.subject)
        val client: Optional<Staff> = staffRepository.findById(userUuid)
        if(client.isPresent){
            return client.get()
        }

        val clientToCreate = Staff()
        clientToCreate.uuid = userUuid
        clientToCreate.username = token.getClaim("preferred_username")
        return staffRepository.save(clientToCreate)
    }

}