package lab.`is`.bank.services.depositManagement.interfaces

import lab.`is`.bank.database.entity.depositManagement.DepositAccount
import lab.`is`.bank.dto.deposit.OperationDto

interface DepositOperations {

    fun addMoney(operationDto: OperationDto): DepositAccount

    fun transferMoney(operationDto: OperationDto)

    fun withdrawMoney(operationDto: OperationDto): DepositAccount

}