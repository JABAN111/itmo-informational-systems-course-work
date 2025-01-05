package lab.`is`.bank.controller.deposit

import lab.`is`.bank.database.entity.depositManagement.DepositAccount
import lab.`is`.bank.dto.ClientDto
import lab.`is`.bank.dto.deposit.DepositAccountDto
import lab.`is`.bank.dto.deposit.OperationDto
import lab.`is`.bank.services.depositManagement.interfaces.DepositService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v0/deposit")
class DepositController(
    val depositService: DepositService
) {

    @PostMapping("/create")
    fun createDeposit(@RequestBody depositDto: DepositAccountDto): DepositAccount {
        return depositService.createDepositAccount(depositDto)
    }

    @PostMapping("/transfer")
    fun transferMoney(@RequestBody operationDto: OperationDto) {
        depositService.transferMoney(operationDto)
    }

    @PostMapping("/withdraw")
    fun withdrawMoney(@RequestBody operationDto: OperationDto) : DepositAccount {
        return depositService.withdrawMoney(operationDto)
    }

    @GetMapping("/get-all/{passport}")
    fun getDepositsByUser(@PathVariable passport: String): List<DepositAccount> {
        return depositService.getDepositsByUser(ClientDto(passport))
    }


    @PostMapping("/add-money")
    fun addMoney(@RequestBody operationDto: OperationDto) : DepositAccount {
        return depositService.addMoney(operationDto)
    }

}


