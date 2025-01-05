package lab.`is`.bank.mapper.deposit

import lab.`is`.bank.database.entity.depositManagement.DepositAccount
import lab.`is`.bank.database.entity.depositManagement.MoneyType
import lab.`is`.bank.dto.deposit.DepositAccountDto

class DepositAccountMapper(
) {
    companion object {
        fun toEntity(dto: DepositAccountDto): DepositAccount {
            //todo require...

            val depositAccount = DepositAccount()

            depositAccount.balance = dto.balance
//            depositAccount.owner = UserMapper.toEntity(dto.owner)
            depositAccount.depositAccountName = dto.depositAccountName
            depositAccount.moneyType = try {
                MoneyType.valueOf(dto.moneyType)
            } catch (e: IllegalArgumentException) {
                TODO("добавить кастомные ошибки с оберткой необходимо")
            }
            depositAccount.balance = dto.balance

            return depositAccount
        }
    }


}