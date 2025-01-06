package lab.`is`.bank.services.depositManagement.impl

import jakarta.transaction.Transactional
import lab.`is`.bank.database.entity.depositManagement.DepositAccount
import lab.`is`.bank.database.entity.depositManagement.transaction.Transaction
import lab.`is`.bank.database.entity.depositManagement.transaction.TransactionStatus
import lab.`is`.bank.database.entity.depositManagement.transaction.TransactionType
import lab.`is`.bank.database.repository.depositManagement.transaction.TransactionRepository
import lab.`is`.bank.dto.deposit.TransactionDto
import lab.`is`.bank.mapper.deposit.TransactionMapper
import lab.`is`.bank.services.depositManagement.interfaces.TransactionService
import lab.`is`.bank.services.exception.ObjectNotExistException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.UUID

@Service
@Transactional
class TransactionServiceImpl(
    private val transactionRepository: TransactionRepository
) : TransactionService {

    private val log = LoggerFactory.getLogger(TransactionServiceImpl::class.java)
    override fun registerTransaction(dto: TransactionDto): Transaction {
        val transaction = TransactionMapper.toEntity(dto)
        return transactionRepository.save(transaction)
    }

    override fun registerTransaction(transaction: Transaction) : Transaction{
        return transactionRepository.save(transaction)
    }



    override fun registerTransaction(
        fromAccount: DepositAccount,
        toAccount: DepositAccount,
        amount: BigDecimal,
        transactionStatus: TransactionStatus,
        transactionType: TransactionType
    ): Transaction {
        val transaction = Transaction()
        transaction.fromAccount = fromAccount
        transaction.toAccount = toAccount
        transaction.amount = amount
        transaction.transactionStatus = transactionStatus
        transaction.transactionType = transactionType
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

    override fun registerFailedTransaction(
        fromAccount: DepositAccount,
        toAccount: DepositAccount,
        amount: BigDecimal,
        transactionType: TransactionType
    ): Transaction {
        return registerTransaction(
            fromAccount,
            toAccount,
            amount,
            TransactionStatus.FAILED,
            transactionType
        )
    }


    override fun getTransaction(uuid: UUID): Transaction {
        return transactionRepository.findById(uuid).orElseThrow {
            log.error("cannot find the transaction by uuid: $uuid")
            ObjectNotExistException("Транзакция не найдена")//fatal error btw
        }
    }
}