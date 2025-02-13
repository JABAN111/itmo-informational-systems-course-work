package lab.`is`.bank.deposit.handler

import lab.`is`.bank.deposit.service.exception.MoneyTypeException
import lab.`is`.bank.deposit.service.exception.NotEnoughMoney
import lab.`is`.bank.deposit.service.exception.NotEnoughMoneyException
import lab.`is`.bank.deposit.service.exception.TransferFailed
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class DepositExceptionHandlers {
    @ExceptionHandler(MoneyTypeException::class)
    @ResponseStatus
    fun moneyTypeExceptionHandler(exception: MoneyTypeException): String = "Money Type Error : ${exception.message}"

    @ExceptionHandler(NotEnoughMoney::class)
    @ResponseStatus
    fun notEnoughMoneyExceptionHandler(exception: NotEnoughMoney): String =
        "Not enough money to finish transaction. Details: ${exception.message}"

    @ExceptionHandler(NotEnoughMoneyException::class)
    @ResponseStatus
    fun notEnoughMoneyExceptionHandler(exception: NotEnoughMoneyException): String =
        "Not enough money to finish   transaction. Details: ${exception.message}"

    @ExceptionHandler(TransferFailed::class)
    @ResponseStatus
    fun transferFailedExceptionHandler(exception: TransferFailed): String = "Transfer failed. Details: ${exception.message}"
}
