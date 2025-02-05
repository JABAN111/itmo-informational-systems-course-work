package lab.`is`.bank.deposit.service.interfaces

import lab.`is`.bank.deposit.database.entity.DepositAccount
import lab.`is`.bank.deposit.dto.OperationDto

interface DepositOperations {

    fun addMoney(operationDto: OperationDto): DepositAccount

    fun transferMoney(operationDto: OperationDto)

    fun withdrawMoney(operationDto: OperationDto): DepositAccount

}