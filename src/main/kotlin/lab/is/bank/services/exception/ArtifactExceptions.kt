package lab.`is`.bank.services.exception

class ArtifactExceptions(message: String?) : RuntimeException(message)
class UsedBanWord(message: String?) : RuntimeException(message)
class ArtifactAlreadySaved(message: String?) : RuntimeException(message)