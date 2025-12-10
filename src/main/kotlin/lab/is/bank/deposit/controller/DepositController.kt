package lab.`is`.bank.deposit.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import lab.`is`.bank.authorization.dto.ClientDto
import lab.`is`.bank.deposit.database.entity.DepositAccount
import lab.`is`.bank.deposit.dto.DepositAccountDto
import lab.`is`.bank.deposit.dto.OperationDto
import lab.`is`.bank.deposit.service.interfaces.DepositService
import org.springframework.web.bind.annotation.*
import java.util.concurrent.atomic.AtomicInteger

@RestController
@RequestMapping("/api/v0/deposit")
@Tag(name = "Deposit", description = "Deposit account management API")
class DepositController(
    val depositService: DepositService,
) {
    private var counter: AtomicInteger = AtomicInteger(0)

    @PostMapping
    @Operation(summary = "Create deposit account", description = "Creates a new deposit account")
    fun createDeposit(
        @RequestBody depositDto: DepositAccountDto,
    ): DepositAccount {
        println(counter.incrementAndGet())
        return depositService.createDepositAccount(depositDto)
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer money", description = "Transfers money between deposit accounts")
    fun transferMoney(
        @RequestBody operationDto: OperationDto,
    ) {
        depositService.transferMoney(operationDto)
    }

    @PostMapping("/withdraw")
    @Operation(summary = "Withdraw money", description = "Withdraws money from a deposit account")
    fun withdrawMoney(
        @RequestBody operationDto: OperationDto,
    ): DepositAccount = depositService.withdrawMoney(operationDto)

    @GetMapping
    @Operation(summary = "Get deposits by user", description = "Retrieves all deposit accounts for a specific user")
    fun getDepositsByUser(
        @Parameter(description = "User passport ID") @RequestParam passport: String,
    ): List<DepositAccount> = depositService.getDepositsByUser(ClientDto(passport))

    @PostMapping("/add-money")
    @Operation(summary = "Add money", description = "Adds money to a deposit account")
    fun addMoney(
        @RequestBody operationDto: OperationDto,
    ): DepositAccount {
        println(counter.incrementAndGet())

        return depositService.addMoney(operationDto)
    }
}
