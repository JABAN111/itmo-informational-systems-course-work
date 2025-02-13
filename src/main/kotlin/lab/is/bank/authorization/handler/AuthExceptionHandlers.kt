package lab.`is`.bank.authorization.handler

import lab.`is`.bank.common.exception.ObjectNotExistException
import org.springframework.http.HttpStatus
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthExceptionHandlers {

    @ExceptionHandler(AuthorizationDeniedException::class)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    fun accessDenied(e: AuthorizationDeniedException): String{
        return "Access denied"
    }

}