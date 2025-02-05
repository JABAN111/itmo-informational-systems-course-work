package lab.`is`.bank.deposit.dto

import java.math.BigDecimal
import java.util.*

data class OperationDto(
    val fromAccount: UUID,
    val toAccount: UUID,
    val amount: BigDecimal,
)
