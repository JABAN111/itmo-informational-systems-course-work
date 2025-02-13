package lab.`is`.bank.authorization.service.impl

import lab.`is`.bank.authorization.database.entity.Client
import lab.`is`.bank.authorization.database.repository.ClientRepository
import lab.`is`.bank.authorization.dto.ClientDto
import lab.`is`.bank.authorization.service.interfaces.ClientService
import lab.`is`.bank.common.exception.ObjectAlreadyExistException
import lab.`is`.bank.common.exception.ObjectNotExistException
import lab.`is`.bank.deposit.service.impl.DepositAccountService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ClientServiceImpl(
    private val clientRep: ClientRepository,
) : ClientService {
    private val log = LoggerFactory.getLogger(DepositAccountService::class.java)

    override fun save(clientDto: ClientDto): Client {
        val client =
            try {
                getIfClientExists(clientDto.passportID)
            } catch (
                e: ObjectNotExistException,
            ) {
                log.info("Creating client with data: $clientDto")
                val newClient = Client()
                newClient.passportID = clientDto.passportID
//            newClient.email = clientDto.email
                return clientRep.save(newClient)
            }
        log.warn("Client with passportID ${client.passportID} already exists")
        throw ObjectAlreadyExistException("Client with passportID ${client.passportID} already exists")
    }

    override fun getIfClientExists(passportID: String): Client =
        clientRep.findByPassportID(passportID)
            ?: throw ObjectNotExistException("Client with passportID $passportID not found")

    override fun get(passportID: String): Client? = clientRep.findByPassportID(passportID)

    override fun saveOrGet(clientDto: ClientDto): Client =
        try {
            getIfClientExists(clientDto.passportID)
        } catch (e: ObjectNotExistException) {
            save(clientDto)
        }
}
