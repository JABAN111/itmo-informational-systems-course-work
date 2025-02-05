package lab.`is`.bank.deposit.mapper

import lab.`is`.bank.deposit.database.entity.transaction.Transaction
import lab.`is`.bank.deposit.database.entity.transaction.TransactionStatus
import lab.`is`.bank.deposit.database.entity.transaction.TransactionType
import lab.`is`.bank.deposit.dto.TransactionDto
import java.math.BigDecimal

class TransactionMapper {

    companion object {
        fun toEntity(dto: TransactionDto): Transaction {
            //todo здесь явно не все проверки
            require(dto.amount >= BigDecimal.ZERO) { "Transaction amount must be greater than 0" }
            require(dto.transactionStatus.isNotBlank()) { "Transaction status must not be blank" }
//            require(dto.transactionType.isNotBlank()) { "Transaction type must not be blank" }
//        require(dto.accountId != null) { "Account ID must not be null" }

            val transaction = Transaction()
            transaction.fromAccount = dto.fromAccountDto?.let { DepositAccountMapper.toEntity(it) }
            transaction.toAccount = dto.toAccountDto?.let { DepositAccountMapper.toEntity(it) }
            transaction.amount = dto.amount
            transaction.transactionStatus = try {
                TransactionStatus.valueOf(dto.transactionStatus)
            } catch (e: IllegalArgumentException) {
                TODO("реализовать свою обертку ошибок")
            }
            transaction.transactionType = try {
                dto.transactionType?.let { TransactionType.valueOf(it) }
            } catch (e: IllegalArgumentException) {
                TODO("реализовать свою обертку ошибок")
            }
            transaction.transactionDate = dto.transactionDate

            return transaction
        }
    }
}