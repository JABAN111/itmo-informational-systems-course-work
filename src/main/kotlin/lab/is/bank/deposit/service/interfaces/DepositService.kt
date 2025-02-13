package lab.`is`.bank.deposit.service.interfaces

import lab.`is`.bank.authorization.dto.ClientDto
import lab.`is`.bank.deposit.database.entity.DepositAccount
import lab.`is`.bank.deposit.dto.DepositAccountDto
import java.util.*

interface DepositService : DepositOperations {
    fun createDepositAccount(dto: DepositAccountDto): DepositAccount

    fun getDepositAccountByUUID(uuid: UUID): DepositAccount

    fun getDepositsByUser(userDto: ClientDto): List<DepositAccount>
}
