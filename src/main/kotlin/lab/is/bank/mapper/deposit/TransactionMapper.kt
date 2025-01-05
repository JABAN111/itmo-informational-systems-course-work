package lab.`is`.bank.mapper.deposit

import lab.`is`.bank.database.entity.depositManagement.DepositAccount
import lab.`is`.bank.database.entity.depositManagement.transaction.Transaction
import lab.`is`.bank.database.entity.depositManagement.transaction.TransactionStatus
import lab.`is`.bank.database.entity.depositManagement.transaction.TransactionType
import lab.`is`.bank.dto.deposit.DepositAccountDto
import lab.`is`.bank.dto.deposit.TransactionDto
import org.springframework.stereotype.Component
import java.math.BigDecimal

class TransactionMapper {

    companion object {
        fun toEntity(dto: TransactionDto): Transaction {
            //todo здесь явно не все проверки
            require(dto.amount > BigDecimal.ZERO) { "Transaction amount must be greater than 0" }
            require(dto.transactionStatus.isNotBlank()) { "Transaction status must not be blank" }
            require(dto.transactionType.isNotBlank()) { "Transaction type must not be blank" }
//        require(dto.accountId != null) { "Account ID must not be null" }

            val transaction = Transaction()
            transaction.fromAccount = DepositAccountMapper.toEntity(dto.fromAccountDto)
            transaction.toAccount = DepositAccountMapper.toEntity(dto.toAccountDto)
            transaction.amount = dto.amount
            transaction.transactionStatus = try {
                TransactionStatus.valueOf(dto.transactionStatus)
            } catch (e: IllegalArgumentException) {
                TODO("реализовать свою обертку ошибок")
            }
            transaction.transactionType = try {
                TransactionType.valueOf(dto.transactionType)
            } catch (e: IllegalArgumentException) {
                TODO("реализовать свою обертку ошибок")
            }
            transaction.transactionDate = dto.transactionDate

            return transaction
        }
    }
}