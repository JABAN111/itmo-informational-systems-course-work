package lab.`is`.bank.deposit.mapper

import lab.`is`.bank.deposit.database.entity.transaction.Transaction
import lab.`is`.bank.deposit.database.entity.transaction.TransactionStatus
import lab.`is`.bank.deposit.database.entity.transaction.TransactionType
import lab.`is`.bank.deposit.dto.TransactionDto
import java.math.BigDecimal

class TransactionMapper {
    companion object {
        fun toEntity(dto: TransactionDto): Transaction {
            require(dto.amount >= BigDecimal.ZERO) { "Transaction amount must be greater than 0" }
            require(dto.transactionStatus.isNotBlank()) { "Transaction status must not be blank" }

            val transaction = Transaction()
            transaction.fromAccount = dto.fromAccountDto?.let { DepositAccountMapper.toEntity(it) }
            transaction.toAccount = dto.toAccountDto?.let { DepositAccountMapper.toEntity(it) }
            transaction.amount = dto.amount
            transaction.transactionStatus =
                try {
                    TransactionStatus.valueOf(dto.transactionStatus)
                } catch (e: IllegalArgumentException) {
                    throw IllegalArgumentException("Invalid transaction status")
                }
            transaction.transactionType =
                try {
                    dto.transactionType?.let { TransactionType.valueOf(it) }
                } catch (e: IllegalArgumentException) {
                    throw IllegalArgumentException("Invalid transaction type")
                }
            transaction.transactionDate = dto.transactionDate

            return transaction
        }
    }
}
