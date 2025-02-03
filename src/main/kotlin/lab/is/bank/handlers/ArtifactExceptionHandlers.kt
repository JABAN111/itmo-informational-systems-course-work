package lab.`is`.bank.handlers

import lab.`is`.bank.services.exception.ArtifactAlreadySaved
import lab.`is`.bank.services.exception.ArtifactExceptions
import lab.`is`.bank.services.exception.UsedBanWord
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ArtifactExceptionHandlers {

    @ExceptionHandler(ArtifactExceptions::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun artifactException(e: ArtifactExceptions): String {
        return "Artifact exception has been occurred: ${e.message}"
    }

    @ExceptionHandler(UsedBanWord::class)
    @ResponseStatus
    fun usedBanWord(e: UsedBanWord): String {
        return "User used ban word in field reasonToSave: ${e.message}, " +
                "according to this - he will be banned"
    }

    @ExceptionHandler(ArtifactAlreadySaved::class)
    @ResponseStatus
    fun artifactAlreadySaved(e: ArtifactAlreadySaved): String {
        return "User used already banned artifact, exception details: ${e.message}"
    }
}