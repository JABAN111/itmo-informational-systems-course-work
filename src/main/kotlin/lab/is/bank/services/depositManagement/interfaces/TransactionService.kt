package lab.`is`.bank.services.depositManagement.interfaces

import jakarta.transaction.Transactional
import lab.`is`.bank.database.entity.depositManagement.DepositAccount
import lab.`is`.bank.database.entity.depositManagement.transaction.Transaction
import lab.`is`.bank.database.entity.depositManagement.transaction.TransactionStatus
import lab.`is`.bank.database.entity.depositManagement.transaction.TransactionType
import lab.`is`.bank.dto.DepositExportData
import lab.`is`.bank.dto.deposit.TransactionDto
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