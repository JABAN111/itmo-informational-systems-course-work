package lab.`is`.bank.deposit.mapper

import lab.`is`.bank.deposit.database.entity.DepositAccount
import lab.`is`.bank.deposit.database.entity.MoneyType
import lab.`is`.bank.deposit.dto.DepositAccountDto

class DepositAccountMapper {
    companion object {
        fun toEntity(dto: DepositAccountDto): DepositAccount {
            val depositAccount = DepositAccount()

            depositAccount.balance = dto.balance
            depositAccount.depositAccountName = dto.depositAccountName
            depositAccount.moneyType =
                try {
                    MoneyType.valueOf(dto.moneyType)
                } catch (e: IllegalArgumentException) {
                    throw IllegalArgumentException("Invalid money type: ${dto.moneyType}")
                }
            depositAccount.balance = dto.balance

            return depositAccount
        }
    }
}
