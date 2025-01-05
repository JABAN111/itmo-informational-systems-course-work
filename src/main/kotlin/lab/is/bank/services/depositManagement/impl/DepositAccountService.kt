package lab.`is`.bank.services.depositManagement.impl

import lab.`is`.bank.database.entity.Client
import lab.`is`.bank.database.entity.depositManagement.DepositAccount
import lab.`is`.bank.database.entity.depositManagement.transaction.Transaction
import lab.`is`.bank.database.entity.depositManagement.transaction.TransactionStatus
import lab.`is`.bank.database.entity.depositManagement.transaction.TransactionType
import lab.`is`.bank.database.repository.depositManagement.DepositAccountRepository
import lab.`is`.bank.database.repository.depositManagement.transaction.TransactionRepository
import lab.`is`.bank.dto.ClientDto
import lab.`is`.bank.dto.deposit.DepositAccountDto
import lab.`is`.bank.dto.deposit.OperationDto
import lab.`is`.bank.mapper.deposit.DepositAccountMapper
import lab.`is`.bank.services.auth.interfaces.ClientService
import lab.`is`.bank.services.auth.interfaces.StaffService
import lab.`is`.bank.services.depositManagement.exception.MoneyTypeException
import lab.`is`.bank.services.depositManagement.exception.NotEnoughMoneyException
import lab.`is`.bank.services.depositManagement.interfaces.DepositService
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
    private val clientService: ClientService
) : DepositService {

    private val log: Logger = LoggerFactory.getLogger(DepositAccountService::class.java)

    override fun createDepositAccount(dto: DepositAccountDto): DepositAccount {
        if(dto.owner.passportID.isEmpty()
            || dto.owner.passportID.isBlank()
            || dto.owner.passportID.length == 1){
            log.warn("passport id is null")
            throw IllegalArgumentException("passport id is null")
        }

        log.info("creating deposit account with data: $dto")

        val depositAccount = DepositAccountMapper.toEntity(dto)

        val owner: Client = clientService.saveOrGet(dto.owner)
        depositAccount.owner = owner

        return depositAccountRepository.save(depositAccount)
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

        return result
    }

    @Throws(ObjectNotExistException::class, NotEnoughMoneyException::class)
    override fun transferMoney(operationDto: OperationDto){
        validateOperationDto(dto = operationDto)
        requireNotNull(operationDto.toAccount)//плохо, а че вы хотели, будет время -- сделаю лучше

        val fromAccount = getDepositAccountByUUID(operationDto.fromAccount)
        val toAccount = getDepositAccountByUUID(operationDto.toAccount)
        val amount = operationDto.amount

        if(fromAccount.moneyType != toAccount.moneyType){
            throw MoneyTypeException("Валюты счетов не совпадают")
        }

        if(fromAccount.balance.minus(amount) < BigDecimal.ZERO){
            throw NotEnoughMoneyException("Недостаточно средств на счете")
        }

        fromAccount.balance = fromAccount.balance.minus(amount)
        toAccount.balance = toAccount.balance.add(amount)

        depositAccountRepository.save(fromAccount)
        depositAccountRepository.save(toAccount)

    }

    override fun withdrawMoney(operationDto: OperationDto) : DepositAccount {
        validateOperationDto(dto = operationDto)

        val account = getDepositAccountByUUID(operationDto.fromAccount)
        val amount = operationDto.amount

        if(account.balance.minus(amount) < BigDecimal.ZERO){
            throw NotEnoughMoneyException("Недостаточно средств на счете")
        }

        account.balance = account.balance.minus(amount)

        val result = depositAccountRepository.save(account)
        log.info("Successfully withdraw money from account ${account.id}")

        return result
    }


    private fun createTransaction(fromAccount: DepositAccount,
                                  toAccount: DepositAccount,
                                  amount: BigDecimal,
                                  transactionType: TransactionType): Transaction {
        val transaction = Transaction()

        transaction.amount = amount
        transaction.toAccount = fromAccount
        transaction.fromAccount = toAccount
        transaction.transactionStatus = TransactionStatus.PENDING
        transaction.transactionType = transactionType


        return transaction
    }


    private fun validateOperationDto(dto: OperationDto) {
        require(dto.amount > BigDecimal.ZERO)
        requireNotNull(dto.fromAccount)
    }
}