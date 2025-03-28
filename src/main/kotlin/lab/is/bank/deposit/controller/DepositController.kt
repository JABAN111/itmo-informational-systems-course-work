package lab.`is`.bank.deposit.controller

import lab.`is`.bank.authorization.dto.ClientDto
import lab.`is`.bank.deposit.database.entity.DepositAccount
import lab.`is`.bank.deposit.dto.DepositAccountDto
import lab.`is`.bank.deposit.dto.OperationDto
import lab.`is`.bank.deposit.service.interfaces.DepositService
import org.springframework.web.bind.annotation.*
import java.util.concurrent.atomic.AtomicInteger

@RestController
@RequestMapping("/api/v0/deposit")
class DepositController(
    val depositService: DepositService,
) {
    private var counter: AtomicInteger = AtomicInteger(0)

    @PostMapping("/create")
    fun createDeposit(
        @RequestBody depositDto: DepositAccountDto,
    ): DepositAccount {
        println(counter.incrementAndGet())
        return depositService.createDepositAccount(depositDto)
    }

    @PostMapping("/transfer")
    fun transferMoney(
        @RequestBody operationDto: OperationDto,
    ) {
        depositService.transferMoney(operationDto)
    }

    @PostMapping("/withdraw")
    fun withdrawMoney(
        @RequestBody operationDto: OperationDto,
    ): DepositAccount = depositService.withdrawMoney(operationDto)

    @GetMapping("/get-all/{passport}")
    fun getDepositsByUser(
        @PathVariable passport: String,
    ): List<DepositAccount> = depositService.getDepositsByUser(ClientDto(passport))

    @PostMapping("/add-money")
    fun addMoney(
        @RequestBody operationDto: OperationDto,
    ): DepositAccount {
        println(counter.incrementAndGet())

        return depositService.addMoney(operationDto)
    }
}
