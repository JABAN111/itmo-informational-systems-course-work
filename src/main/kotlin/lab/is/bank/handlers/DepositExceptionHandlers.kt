package lab.`is`.bank.handlers

import lab.`is`.bank.services.depositManagement.exception.MoneyTypeException
import lab.`is`.bank.services.depositManagement.exception.NotEnoughMoney
import lab.`is`.bank.services.depositManagement.exception.NotEnoughMoneyException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class DepositExceptionHandlers {

    @ExceptionHandler(MoneyTypeException::class)
    @ResponseStatus
    fun moneyTypeExceptionHandler(exception: MoneyTypeException): String {
        return "Money Type Error : ${exception.message}"
    }

    @ExceptionHandler(NotEnoughMoney::class)
    @ResponseStatus
    fun notEnoughMoneyExceptionHandler(exception: NotEnoughMoney): String {
        return "Not enough money to finish transaction. Details: ${exception.message}"
    }

    @ExceptionHandler(NotEnoughMoneyException::class)
    @ResponseStatus
    fun notEnoughMoneyExceptionHandler(exception: NotEnoughMoneyException): String {
        return "Not enough money to finish   transaction. Details: ${exception.message}"
    }

}
