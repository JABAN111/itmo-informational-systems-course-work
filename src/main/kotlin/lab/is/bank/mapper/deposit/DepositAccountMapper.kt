package lab.`is`.bank.mapper.deposit

import lab.`is`.bank.database.entity.depositManagement.DepositAccount
import lab.`is`.bank.database.entity.depositManagement.MoneyType
import lab.`is`.bank.dto.deposit.DepositAccountDto

class DepositAccountMapper(
) {
    companion object {
        fun toEntity(dto: DepositAccountDto): DepositAccount {
            val depositAccount = DepositAccount()

            depositAccount.balance = dto.balance
            depositAccount.depositAccountName = dto.depositAccountName
            depositAccount.moneyType = try {
                MoneyType.valueOf(dto.moneyType)
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Invalid money type: ${dto.moneyType}")
            }
            depositAccount.balance = dto.balance

            return depositAccount
        }
    }


}