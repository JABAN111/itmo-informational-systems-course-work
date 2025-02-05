package lab.`is`.bank.deposit.service.interfaces

import lab.`is`.bank.deposit.database.entity.DepositAccount
import lab.`is`.bank.deposit.database.entity.transaction.Transaction
import lab.`is`.bank.deposit.database.entity.transaction.TransactionStatus
import lab.`is`.bank.deposit.database.entity.transaction.TransactionType
import lab.`is`.bank.deposit.dto.DepositExportData
import lab.`is`.bank.deposit.dto.TransactionDto
import java.math.BigDecimal
import java.util.UUID

interface TransactionService {


    fun registerTransaction(dto: TransactionDto) : Transaction
    fun registerTransaction(transaction: Transaction) : Transaction

    fun registerSuccessTransaction(
        fromAccount: DepositAccount,
        toAccount: DepositAccount,
        amount: BigDecimal,
        transactionType: TransactionType
    ) : Transaction

    fun registerFailedTransaction(
        fromAccount: DepositAccount,
        toAccount: DepositAccount,
        amount: BigDecimal,
        transactionType: TransactionType
    ) : Transaction

    fun registerTransaction(
        fromAccount: DepositAccount,
        toAccount: DepositAccount,
        amount: BigDecimal,
        transactionStatus: TransactionStatus,
        transactionType: TransactionType
    ) : Transaction


    fun getTransaction(uuid: UUID) : Transaction
    fun getDataForExport(accountId: UUID, types: Array<String>) : List<DepositExportData>
}