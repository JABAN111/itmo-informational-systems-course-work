package lab.`is`.bank.authorization.controller

import lab.`is`.bank.authorization.dto.StaffDto
import lab.`is`.bank.authorization.service.interfaces.StaffService
import lab.`is`.bank.security.JwtServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/registration")
class AuthController(
    private val staffService: StaffService,
    private val jwtServiceImpl: JwtServiceImpl
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
    fun register(@RequestHeader("Authorization") token: String): ResponseEntity<String> {
        val jwt = token.removePrefix("Bearer ")
        val username = jwtServiceImpl.extractUserName(jwt)
        val role = jwtServiceImpl.extractRoles(jwt)
        val dto = StaffDto(username, role)
        staffService.getOrCreateStaff(dto)

        return ResponseEntity.ok("Пользователь $username зарегистрирован")
    }
}