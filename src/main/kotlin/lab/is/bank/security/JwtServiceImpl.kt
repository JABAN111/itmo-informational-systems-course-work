package lab.`is`.bank.security

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.net.URL
import java.security.interfaces.RSAPublicKey
import java.util.*
import java.util.function.Function

@Service
class JwtServiceImpl {
    private val log: Logger = LoggerFactory.getLogger(JwtServiceImpl::class.java)

    @Value("\${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private lateinit var jwkSetUri: String

    /**
     * Извлечение имени пользователя из токена
     */
    fun extractUserName(token: String): String {
        log.debug("Extracting username from JWT token")
        return extractClaim(token) { claims ->
            val username = claims["preferred_username"] as String
            log.info("Successfully extracted username from token: $username")
            username
        }
    }

    fun extractRoles(token: String): String =
        extractClaim(token) { claims ->
            log.debug("Extracting roles from JWT token")
            val realmAccess = claims["realm_access"] as? Map<*, *>
            val roles = realmAccess?.get("roles") as? List<*>

            val role = roles
                ?.filterIsInstance<String>()
                ?.find { it.equals("artifacter", ignoreCase = true) }
                ?: ""

            log.info("Extracted role from token: $role")
            role
        }

    /**
     * Проверка токена на валидность
     */
    fun isTokenValid(
        token: String,
        userDetails: UserDetails,
    ): Boolean {
        log.debug("Validating JWT token for user: ${userDetails.username}")
        val userName = extractUserName(token)
        val isValid = userName == userDetails.username && !isTokenExpired(token)
        if (isValid) {
            log.info("JWT token is valid for user: ${userDetails.username}")
        } else {
            log.warn("JWT token validation failed for user: ${userDetails.username}")
        }
        return isValid
    }

    /**
     * Проверка, истек ли срок действия токена
     */
    private fun isTokenExpired(token: String): Boolean = extractExpiration(token).before(Date())

    /**
     * Извлечение даты истечения токена
     */
    private fun extractExpiration(token: String): Date = extractClaim(token, Claims::getExpiration)

    /**
     * Извлечение данных из токена
     */
    private fun <T> extractClaim(
        token: String,
        claimsResolver: Function<Claims, T>,
    ): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    /**
     * Извлечение всех данных из токена
     */
    private fun extractAllClaims(token: String): Claims {
        try {
            log.debug("Extracting claims from JWT token")
            val publicKey = getPublicKeyFromJwkSet(jwkSetUri)
            val claims = Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .payload
            log.debug("Successfully extracted claims from token")
            return claims
        } catch (e: JwtException) {
            log.error("Invalid JWT token: ${e.message}", e)
            throw Exception("Invalid token")
        } catch (e: ExpiredJwtException) {
            log.warn("JWT token has expired: ${e.message}")
            throw Exception("Invalid token")
        }
    }

    /**
     * Получение ключа для подписи токена из Keycloak (JWK Set)
     */
    private fun getSigningKey(): RSAPublicKey {
        log.debug("Loading JWK Set from Keycloak: $jwkSetUri")
        try {
            val jwkSet = JWKSet.load(URL(jwkSetUri))
            log.info("Successfully loaded JWK Set from Keycloak, found ${jwkSet.keys.size} keys")

            for (jwk in jwkSet.keys) {
                val publicKey = (jwk as RSAKey).toRSAPublicKey()
                log.debug("Using RSA public key with algorithm: ${publicKey.algorithm}")
                return publicKey
            }

            log.error("No valid RSA keys found in JWK Set from Keycloak")
            throw NoSuchElementException("Invalid JWKSet")
        } catch (e: Exception) {
            log.error("Failed to load JWK Set from Keycloak: ${e.message}", e)
            throw e
        }
    }

    private fun getPublicKeyFromJwkSet(jwkSetUri: String): RSAPublicKey {
        log.debug("Fetching public key from JWK Set URI: $jwkSetUri")
        try {
            val jwkSet = JWKSet.load(URL(jwkSetUri))

            for (jwk in jwkSet.keys) {
                val publicKey = (jwk as RSAKey).toRSAPublicKey()
                log.debug("Retrieved public key from Keycloak JWK Set")
                return publicKey
            }

            log.error("No JWK found in Keycloak JWK Set")
            throw NoSuchElementException("JWK with the specified key ID not found.")
        } catch (e: Exception) {
            log.error("Error fetching public key from Keycloak: ${e.message}", e)
            throw e
        }
    }
}
