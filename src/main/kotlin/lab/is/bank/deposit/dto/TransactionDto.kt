package lab.`is`.bank.deposit.dto

import java.math.BigDecimal
import java.util.*

data class TransactionDto(
    val fromAccountDto: DepositAccountDto? = null,
    val toAccountDto: DepositAccountDto? = null,
    val amount: BigDecimal,
    var transactionStatus: String = "FAILED",
    val transactionType: String? = null,
    val transactionDate: Date = Date(),
)
