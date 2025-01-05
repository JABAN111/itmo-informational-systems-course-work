package lab.`is`.bank.dto.deposit

import lab.`is`.bank.dto.ClientDto
import java.math.BigDecimal

data class DepositAccountDto(
    val owner: ClientDto,
    val depositAccountName: String,
    val balance: BigDecimal = BigDecimal.ZERO,
    val moneyType: String,
)
