package lab.`is`.bank.artifact.service.exceptions

class ArtifactExceptions(message: String?) : RuntimeException(message)
class UsedBanWord(message: String?) : RuntimeException(message)
class ArtifactAlreadySaved(message: String?) : RuntimeException(message)