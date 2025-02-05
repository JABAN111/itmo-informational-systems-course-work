package lab.`is`.bank.deposit.service.exception

class NotEnoughMoneyException(message: String) : RuntimeException(message)
class MoneyTypeException(message: String): RuntimeException(message)
class NotEnoughMoney(message: String) : RuntimeException(message)
class TransferFailed(message: String): RuntimeException(message)