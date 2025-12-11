package lab.`is`.bank.authorization.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import lab.`is`.bank.authorization.dto.StaffDto
import lab.`is`.bank.authorization.service.interfaces.StaffService
import lab.`is`.bank.security.JwtServiceImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/registration")
@Tag(name = "Authentication", description = "User registration and authentication API")
class AuthController(
    private val staffService: StaffService,
    private val jwtServiceImpl: JwtServiceImpl,
) {
    private val log: Logger = LoggerFactory.getLogger(AuthController::class.java)
    /**
     * Ужасный костыль, по идее его должен был бы заменить какой-нибудь брокер сообщений
     *
     * Проблема следующая, нужно синхронизировать Staff с данными из keycloak, но нормально это сделать нереально или
     * сумасшедше больно
     *
     * В принципе, приложение сконфигурировано так, что запросы пройдут только с фронтового адреса, то есть
     * http://localhost:5173, но все равно не есть хорошо
     *
     */
    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Synchronizes staff data from Keycloak token")
    fun register(
        @Parameter(description = "JWT Bearer token") @RequestHeader("Authorization") token: String,
    ): ResponseEntity<String> {
        log.info("Received registration request")
        try {
            val jwt = token.removePrefix("Bearer ")
            log.debug("Extracted JWT token from Authorization header")

            val username = jwtServiceImpl.extractUserName(jwt)
            log.info("Registering user from Keycloak token: $username")

            val role = jwtServiceImpl.extractRoles(jwt)
            log.debug("User role from Keycloak: $role")

            val dto = StaffDto(username, role)
            staffService.getOrCreateStaff(dto)

            log.info("Successfully registered/synchronized user: $username")
            return ResponseEntity.ok("Пользователь $username зарегистрирован")
        } catch (e: Exception) {
            log.error("Failed to register user from Keycloak token: ${e.message}", e)
            throw e
        }
    }
}
