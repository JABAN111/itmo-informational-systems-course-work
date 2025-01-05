package lab.`is`.bank.services.depositManagement.interfaces

import lab.`is`.bank.database.entity.depositManagement.DepositAccount
import lab.`is`.bank.dto.ClientDto
import lab.`is`.bank.dto.deposit.DepositAccountDto
import java.util.*

interface DepositService : DepositOperations {

    fun createDepositAccount(dto: DepositAccountDto): DepositAccount
    fun getDepositAccountByUUID(uuid: UUID): DepositAccount
    fun getDepositsByUser(userDto: ClientDto): List<DepositAccount>

}