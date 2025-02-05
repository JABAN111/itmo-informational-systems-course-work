package lab.`is`.bank.deposit.dto

import lab.`is`.bank.authorization.dto.ClientDto
import java.math.BigDecimal

data class DepositAccountDto(
    val owner: ClientDto,
    val depositAccountName: String,
    val balance: BigDecimal = BigDecimal.ZERO,
    val moneyType: String,
)
