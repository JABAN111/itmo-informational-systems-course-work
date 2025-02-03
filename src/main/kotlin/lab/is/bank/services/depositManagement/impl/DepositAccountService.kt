package lab.`is`.bank.services.depositManagement.impl

import lab.`is`.bank.database.entity.Client
import lab.`is`.bank.database.entity.depositManagement.DepositAccount
import lab.`is`.bank.database.entity.depositManagement.transaction.TransactionType
import lab.`is`.bank.database.repository.depositManagement.DepositAccountRepository
import lab.`is`.bank.dto.ClientDto
import lab.`is`.bank.dto.deposit.DepositAccountDto
import lab.`is`.bank.dto.deposit.OperationDto
import lab.`is`.bank.mapper.deposit.DepositAccountMapper
import lab.`is`.bank.services.auth.interfaces.ClientService
import lab.`is`.bank.services.depositManagement.exception.MoneyTypeException
import lab.`is`.bank.services.depositManagement.exception.NotEnoughMoneyException
import lab.`is`.bank.services.depositManagement.interfaces.DepositService
import lab.`is`.bank.services.depositManagement.interfaces.TransactionService
import lab.`is`.bank.services.exception.ObjectNotExistException

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
    private val transactionService: TransactionService
) : DepositService {

    private val log: Logger = LoggerFactory.getLogger(DepositAccountService::class.java)

    override fun createDepositAccount(dto: DepositAccountDto): DepositAccount {
        if (dto.owner.passportID.isEmpty() || dto.owner.passportID.isBlank()) {
            log.warn("passport id is null")

            transactionService.registerFailedTransaction(
                amount = dto.balance,
                fromAccount = DepositAccount(),//todo не очень хорошо, потому что потом говно в отчетах может полезть
                //либо бд будет ругаться на отсутствие этой записи
                toAccount = DepositAccount(),
                transactionType = TransactionType.CREATE
            )
            throw IllegalArgumentException("passport id is null")//fixme в случае ошибки это срывает транзакцию -> не сохраняет ошибочные операции
        }

        log.info("creating deposit account with data: $dto")

        val depositAccount = DepositAccountMapper.toEntity(dto)
        val owner: Client = clientService.saveOrGet(dto.owner)
        depositAccount.owner = owner

        val savedDepositAccount = depositAccountRepository.save(depositAccount)

        transactionService.registerSuccessTransaction(
            toAccount = savedDepositAccount,
            fromAccount = savedDepositAccount,
            amount = dto.balance,
            transactionType = TransactionType.CREATE
        )
        return savedDepositAccount
    }

    override fun getDepositAccountByUUID(uuid: UUID): DepositAccount {
        return depositAccountRepository.findById(uuid).orElseThrow {
            log.warn("cannot find the account by uuid: $uuid")
            ObjectNotExistException("Аккаунт не найден")
        }
    }


    override fun getDepositsByUser(userDto: ClientDto): List<DepositAccount> {
        return depositAccountRepository.findDepositAccountsByOwnerPassportID(userDto.passportID)
    }


    override fun addMoney(operationDto: OperationDto): DepositAccount {
        validateOperationDto(dto = operationDto)

        val account = getDepositAccountByUUID(operationDto.fromAccount)
        val amount = operationDto.amount

        account.balance = account.balance.add(amount)

        val result = depositAccountRepository.save(account)
        log.info("Successfully add money to account ${account.id}")
        transactionService.registerSuccessTransaction(
            fromAccount = account,
            toAccount = account,
            amount = amount,
            transactionType = TransactionType.DEPOSITING
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
                transactionType = TransactionType.TRANSFER
            )
            throw MoneyTypeException("Валюты счетов не совпадают")
        }

        // Вызываем метод репозитория для перевода средств
        val transferStatus = depositAccountRepository.transfer(fromAccount.id, toAccount.id, amount)

        // Проверяем статус перевода и регистрируем транзакцию в зависимости от результата
        if (transferStatus == "SUCCEEDED") {
            transactionService.registerSuccessTransaction(
                fromAccount = fromAccount,
                toAccount = toAccount,
                amount = amount,
                transactionType = TransactionType.TRANSFER
            )
        } else {
            transactionService.registerFailedTransaction(
                fromAccount = fromAccount,
                toAccount = toAccount,
                amount = amount,
                transactionType = TransactionType.TRANSFER
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
                transactionType = TransactionType.WITHDRAW
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
            transactionType = TransactionType.WITHDRAW
        )
        return result
    }

    private fun validateOperationDto(dto: OperationDto) {
        require(dto.amount > BigDecimal.ZERO)
        requireNotNull(dto.fromAccount)
    }
}