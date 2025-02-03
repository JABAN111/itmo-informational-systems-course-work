package lab.`is`.bank.services.depositManagement.exception

class NotEnoughMoneyException(message: String) : RuntimeException(message)
class MoneyTypeException(message: String): RuntimeException(message)
class NotEnoughMoney(message: String) : RuntimeException(message)
