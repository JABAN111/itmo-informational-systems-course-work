package lab.`is`.bank.artifact.handler

import lab.`is`.bank.artifact.exception.ArtifactAlreadySaved
import lab.`is`.bank.artifact.exception.ArtifactExceptions
import lab.`is`.bank.artifact.exception.UsedBanWord
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ArtifactExceptionHandlers {
    @ExceptionHandler(ArtifactExceptions::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun artifactException(e: ArtifactExceptions): String = "Artifact exception has been occurred: ${e.message}"

    @ExceptionHandler(UsedBanWord::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun usedBanWord(e: UsedBanWord): String =
        "User used ban word in field reasonToSave: ${e.message}, " +
            "according to this - he will be banned"

    @ExceptionHandler(ArtifactAlreadySaved::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun artifactAlreadySaved(e: ArtifactAlreadySaved): String = "User used already banned artifact, exception details: ${e.message}"
}
