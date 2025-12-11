package lab.`is`.bank.authorization.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import lab.`is`.bank.authorization.dto.StaffDto
import lab.`is`.bank.authorization.service.interfaces.StaffService
import lab.`is`.bank.security.JwtServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/registration")
@Tag(name = "Authentication", description = "User registration and authentication API")
class AuthController(
    private val staffService: StaffService,
    private val jwtServiceImpl: JwtServiceImpl,
) {
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
        val jwt = token.removePrefix("Bearer ")
        val username = jwtServiceImpl.extractUserName(jwt)
        val role = jwtServiceImpl.extractRoles(jwt)
        val dto = StaffDto(username, role)
        staffService.getOrCreateStaff(dto)

        return ResponseEntity.ok("Пользователь $username зарегистрирован")
    }
}
