package lab.`is`.bank.deposit.dto

import java.math.BigDecimal
import java.util.*

data class DepositExportData(
    val moneyType: String,
    val accountID: UUID,
    val transactionAmount: BigDecimal,
    val transactionCreate: String,
)

