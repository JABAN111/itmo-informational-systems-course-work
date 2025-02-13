package lab.`is`.bank.artifact.exception

class ArtifactExceptions(
    message: String?,
) : RuntimeException(message)

class UsedBanWord(
    message: String?,
) : RuntimeException(message)

class ArtifactAlreadySaved(
    message: String?,
) : RuntimeException(message)

class ArtifactBelongToAnotherPersonException(
    message: String?,
) : RuntimeException(message)
