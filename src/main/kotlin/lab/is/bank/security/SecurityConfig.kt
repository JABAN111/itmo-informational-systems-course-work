package lab.`is`.bank.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig : WebMvcConfigurer {

    @Throws(Exception::class)
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { obj -> obj.disable() }
            .cors { cors ->
                cors.configurationSource { request ->
                    val corsConfiguration = CorsConfiguration()
                    corsConfiguration.allowedOriginPatterns = listOf("*")
                    corsConfiguration.allowedMethods = listOf("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS")
                    corsConfiguration.allowedHeaders = listOf("*")
                    corsConfiguration.allowCredentials = true
                    corsConfiguration
                }
            }
            .authorizeHttpRequests { request ->
                request
                    .anyRequest().permitAll()
            }
            .sessionManagement { manager ->
                manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Без сессий
            }

        return http.build()
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api/**") // Разрешаем для путей, начинающихся с /api/
            .allowedOrigins("http://localhost:5174") // Разрешаем только ваш фронтенд
            .allowedMethods("GET", "POST", "PUT", "DELETE") // Указываем разрешенные методы
            .allowedHeaders("*") // Разрешаем все заголовки
            .allowCredentials(true)
    }
}