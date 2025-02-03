package lab.`is`.bank.services.depositManagement.impl

import jakarta.persistence.Tuple
import lab.`is`.bank.database.entity.depositManagement.DepositAccount
import lab.`is`.bank.database.entity.depositManagement.transaction.Transaction
import lab.`is`.bank.database.entity.depositManagement.transaction.TransactionStatus
import lab.`is`.bank.database.entity.depositManagement.transaction.TransactionType
import lab.`is`.bank.database.repository.depositManagement.transaction.TransactionRepository
import lab.`is`.bank.dto.DepositExportData
import lab.`is`.bank.dto.deposit.TransactionDto
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import lab.`is`.bank.mapper.deposit.TransactionMapper
import lab.`is`.bank.services.depositManagement.interfaces.TransactionService
import lab.`is`.bank.services.exception.ObjectNotExistException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.UUID

@Service
class TransactionServiceImpl(
    private val transactionRepository: TransactionRepository
) : TransactionService {

    private val log = LoggerFactory.getLogger(TransactionServiceImpl::class.java)

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun registerTransaction(dto: TransactionDto): Transaction {
        val transaction = TransactionMapper.toEntity(dto)
        return transactionRepository.save(transaction)
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun registerTransaction(transaction: Transaction) : Transaction{
        return transactionRepository.save(transaction)
    }

    override fun registerSuccessTransaction(
        fromAccount: DepositAccount,
        toAccount: DepositAccount,
        amount: BigDecimal,
        transactionType: TransactionType
    ): Transaction {
        return registerTransaction(
            fromAccount,
            toAccount,
            amount,
            TransactionStatus.SUCCEEDED,
            transactionType
        )
    }


    override fun getTransaction(uuid: UUID): Transaction {
        return transactionRepository.findById(uuid).orElseThrow {
            log.error("cannot find the transaction by uuid: $uuid")
            ObjectNotExistException("Транзакция не найдена")//fatal error btw
        }
    }

    override fun getDataForExport(accountId: UUID, types: Array<String>): List<DepositExportData> {
        val data: List<Tuple> = transactionRepository.getDataForReport(accountId, types)
        val result = mutableListOf<DepositExportData>();
        for (tuple in data) {
            val dto = DepositExportData(
                moneyType = tuple.get(0).toString(),
                accountID = tuple.get(1) as UUID,
                transactionAmount = BigDecimal(tuple.get(2).toString()),
                transactionCreate = tuple.get(3).toString()
            )
            result.add(dto)
        }
        return result
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun registerFailedTransaction(
        fromAccount: DepositAccount,
        toAccount: DepositAccount,
        amount: BigDecimal,
        transactionType: TransactionType
    ): Transaction {
        log.warn("Регистрация неудачной транзакции: from=${fromAccount.id}, to=${toAccount.id}, amount=$amount")
        return registerTransaction(
            fromAccount,
            toAccount,
            amount,
            TransactionStatus.FAILED,
            transactionType
        )
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun registerTransaction(
        fromAccount: DepositAccount,
        toAccount: DepositAccount,
        amount: BigDecimal,
        transactionStatus: TransactionStatus,
        transactionType: TransactionType
    ): Transaction {
        val transaction = Transaction().apply {
            this.fromAccount = fromAccount
            this.toAccount = toAccount
            this.amount = amount
            this.transactionStatus = transactionStatus
            this.transactionType = transactionType
        }
        return transactionRepository.save(transaction)
    }
}