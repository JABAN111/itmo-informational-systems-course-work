package lab.`is`.bank.authorization.handler

import lab.`is`.bank.common.exception.ObjectNotExistException
import org.springframework.http.HttpStatus
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthExceptionHandlers {

//    @ExceptionHandler(AuthorizationDeniedException::class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    fun objectNotExistException(e: ObjectNotExistException): String{
//        return "Object not exist, details: ${e.message}"
//    }

}