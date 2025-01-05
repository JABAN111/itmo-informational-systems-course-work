package lab.`is`.bank.dto.deposit

import java.math.BigDecimal
import java.util.*

data class TransactionDto(
    val fromAccountDto: DepositAccountDto,
    val toAccountDto: DepositAccountDto,
    val amount: BigDecimal,
    val transactionStatus: String,
    val transactionType: String,
    val transactionDate: Date,
)