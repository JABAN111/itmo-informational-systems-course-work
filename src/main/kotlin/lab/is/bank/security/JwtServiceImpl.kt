package lab.`is`.bank.security

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.net.URL
import java.security.interfaces.RSAPublicKey
import java.util.*
import java.util.function.Function
import javax.crypto.SecretKey

@Service
class JwtServiceImpl {

    @Value("\${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private lateinit var jwkSetUri: String

    /**
     * Извлечение имени пользователя из токена
     */
    fun extractUserName(token: String): String {
        return extractClaim(token) { claims -> claims["preferred_username"] as String }
    }

    fun extractRoles(token: String): String {
        return extractClaim(token) { claims ->
            val realmAccess = claims["realm_access"] as? Map<*, *>
            val roles = realmAccess?.get("roles") as? List<*>

            roles?.filterIsInstance<String>()
                ?.find { it.equals("artifacter", ignoreCase = true) }
                ?: ""
        }
    }
    /**
     * Проверка токена на валидность
     */
     fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val userName = extractUserName(token)
        return userName == userDetails.username && !isTokenExpired(token)
    }

    /**
     * Проверка, истек ли срок действия токена
     */
    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    /**
     * Извлечение даты истечения токена
     */
    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    /**
     * Извлечение данных из токена
     */
    private fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    /**
     * Извлечение всех данных из токена
     */
    private fun extractAllClaims(token: String): Claims {
        try {
            val publicKey = getPublicKeyFromJwkSet(jwkSetUri)
            return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: JwtException) {
            throw Exception("Invalid token")
        } catch (e: ExpiredJwtException) {
            throw Exception("Invalid token")
        }
    }

    /**
     * Получение ключа для подписи токена из Keycloak (JWK Set)
     */
//    private fun getSigningKey(): SecretKey {
//        val jwkSet = JWKSet.load(URL(jwkSetUri))
//        val jwk: JWK = jwkSet.keys.first() // Берем первый ключ (обычно используется один)
//        return (jwk as RSAKey).toRSAPublicKey()
//    }
//    private fun getSigningKey(): SecretKey {
//        val keyBytes = Decoders.BASE64.decode(jwkSetUri)
//        return Keys.hmacShaKeyFor(keyBytes)
//    }
    private fun getSigningKey(): RSAPublicKey {
        val jwkSet = JWKSet.load(URL(jwkSetUri))

        for (jwk in jwkSet.keys) {
            return (jwk as RSAKey).toRSAPublicKey()
        }

        throw NoSuchElementException("Invalid JWKSet")
    }

    private fun getPublicKeyFromJwkSet(jwkSetUri: String): RSAPublicKey {
        val jwkSet = JWKSet.load(URL(jwkSetUri))

        for (jwk in jwkSet.keys) {
                return (jwk as RSAKey).toRSAPublicKey()
        }

        throw NoSuchElementException("JWK with the specified key ID not found.")
    }
}