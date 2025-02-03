package lab.`is`.bank.handlers

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class UnpredictableExceptionHandlers {

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun methodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException): String {
        return "Argument type mismatch exception has been occurred: ${e.message}"
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun illegalArgumentException(e: IllegalArgumentException): String {
        return "Argument type mismatch exception has been occurred: ${e.message}"
    }

    @ExceptionHandler(RuntimeException::class)
    @ResponseStatus
    fun runtimeException(e: RuntimeException): String {
        return "Unpredictable exception has been handled: ${e.message}"
    }
}