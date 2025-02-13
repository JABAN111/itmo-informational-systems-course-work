package lab.`is`.bank.deposit.service.impl

import jakarta.persistence.EntityManager
import lab.`is`.bank.authorization.database.entity.Client
import lab.`is`.bank.authorization.dto.ClientDto
import lab.`is`.bank.authorization.service.interfaces.ClientService
import lab.`is`.bank.common.exception.ObjectNotExistException
import lab.`is`.bank.deposit.database.entity.DepositAccount
import lab.`is`.bank.deposit.database.entity.transaction.TransactionType
import lab.`is`.bank.deposit.database.repository.DepositAccountRepository
import lab.`is`.bank.deposit.dto.DepositAccountDto
import lab.`is`.bank.deposit.dto.OperationDto
import lab.`is`.bank.deposit.mapper.DepositAccountMapper
import lab.`is`.bank.deposit.service.exception.MoneyTypeException
import lab.`is`.bank.deposit.service.exception.NotEnoughMoneyException
import lab.`is`.bank.deposit.service.interfaces.DepositService
import lab.`is`.bank.deposit.service.interfaces.TransactionService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*
import kotlin.Throws

@Service
@Transactional
class DepositAccountService(
    private val depositAccountRepository: DepositAccountRepository,
    private val clientService: ClientService,
    private val transactionService: TransactionService,
    private val em: EntityManager,
) : DepositService {
    private val log: Logger = LoggerFactory.getLogger(DepositAccountService::class.java)

    override fun createDepositAccount(dto: DepositAccountDto): DepositAccount {
        if (dto.owner.passportID.isEmpty() || dto.owner.passportID.isBlank()) {
            log.warn("passport id is null")

            transactionService.registerFailedTransaction(
                amount = dto.balance,
                fromAccount = DepositAccount(),
                toAccount = DepositAccount(),
                transactionType = TransactionType.CREATE,
            )
            throw IllegalArgumentException("passport id is null")
        }

        log.info("creating deposit account with data: $dto")

        val depositAccount = DepositAccountMapper.toEntity(dto)
        val owner: Client = clientService.saveOrGet(dto.owner)
        depositAccount.owner = owner

        val savedDepositAccount = depositAccountRepository.save(depositAccount)
        em.flush()
        transactionService.registerSuccessTransaction(
            toAccount = savedDepositAccount,
            fromAccount = savedDepositAccount,
            amount = dto.balance,
            transactionType = TransactionType.CREATE,
        )
        return savedDepositAccount
    }

    override fun getDepositAccountByUUID(uuid: UUID): DepositAccount =
        depositAccountRepository.findById(uuid).orElseThrow {
            log.warn("cannot find the account by uuid: $uuid")
            ObjectNotExistException("Аккаунт не найден")
        }

    override fun getDepositsByUser(userDto: ClientDto): List<DepositAccount> =
        depositAccountRepository.findDepositAccountsByOwnerPassportID(userDto.passportID)

    override fun addMoney(operationDto: OperationDto): DepositAccount {
        validateOperationDto(dto = operationDto)

        val account = getDepositAccountByUUID(operationDto.fromAccount)
        val amount = operationDto.amount
        if (amount < BigDecimal.ZERO) {
            transactionService.registerFailedTransaction(
                amount = amount,
                fromAccount = account,
                toAccount = account,
                transactionType = TransactionType.DEPOSITING,
            )
            throw NotEnoughMoneyException("Negative amount of money cannot be saved")
        }
        account.balance = account.balance.add(amount)

        val result = depositAccountRepository.save(account)
        log.info("Successfully add money to account ${account.id}")
        transactionService.registerSuccessTransaction(
            fromAccount = account,
            toAccount = account,
            amount = amount,
            transactionType = TransactionType.DEPOSITING,
        )

        return result
    }

    @Throws(ObjectNotExistException::class, NotEnoughMoneyException::class)
    override fun transferMoney(operationDto: OperationDto) {
        validateOperationDto(dto = operationDto)
        requireNotNull(operationDto.toAccount)

        val fromAccount = getDepositAccountByUUID(operationDto.fromAccount)
        val toAccount = getDepositAccountByUUID(operationDto.toAccount)
        val amount = operationDto.amount

        if (fromAccount.moneyType != toAccount.moneyType) {
            transactionService.registerFailedTransaction(
                fromAccount = fromAccount,
                toAccount = toAccount,
                amount = amount,
                transactionType = TransactionType.TRANSFER,
            )
            throw MoneyTypeException("Валюты счетов не совпадают")
        }

        val transferStatus = depositAccountRepository.transfer(fromAccount.id, toAccount.id, amount)

        if (transferStatus == "SUCCEEDED") {
            transactionService.registerSuccessTransaction(
                fromAccount = fromAccount,
                toAccount = toAccount,
                amount = amount,
                transactionType = TransactionType.TRANSFER,
            )
        } else {
            transactionService.registerFailedTransaction(
                fromAccount = fromAccount,
                toAccount = toAccount,
                amount = amount,
                transactionType = TransactionType.TRANSFER,
            )
            throw RuntimeException("Ошибка перевода: $transferStatus")
        }
    }

    override fun withdrawMoney(operationDto: OperationDto): DepositAccount {
        validateOperationDto(dto = operationDto)

        val account = getDepositAccountByUUID(operationDto.fromAccount)
        val amount = operationDto.amount

        if (account.balance.minus(amount) < BigDecimal.ZERO) {
            transactionService.registerFailedTransaction(
                fromAccount = account,
                toAccount = account,
                amount = amount,
                transactionType = TransactionType.WITHDRAW,
            )
            throw NotEnoughMoneyException("Недостаточно средств на счете")
        }

        account.balance = account.balance.minus(amount)

        val result = depositAccountRepository.save(account)
        log.info("Successfully withdraw money from account ${account.id}")
        transactionService.registerSuccessTransaction(
            fromAccount = account,
            toAccount = account,
            amount = amount,
            transactionType = TransactionType.WITHDRAW,
        )
        return result
    }

    private fun validateOperationDto(dto: OperationDto) {
        require(dto.amount > BigDecimal.ZERO)
        requireNotNull(dto.fromAccount)
    }
}
