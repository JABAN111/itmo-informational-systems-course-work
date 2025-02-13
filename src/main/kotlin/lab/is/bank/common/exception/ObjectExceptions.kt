package lab.`is`.bank.common.exception

class ObjectNotExistException(message: String) : Exception(message)
class ObjectAlreadyExistException(message: String) : Exception(message)
class ObjectNecessaryFieldEmptyException(message: String) : Exception(message)