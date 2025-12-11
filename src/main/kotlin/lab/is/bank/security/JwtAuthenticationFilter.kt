package lab.`is`.bank.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lab.`is`.bank.authorization.database.entity.Staff
import lab.`is`.bank.authorization.database.entity.StaffRole
import lab.`is`.bank.authorization.service.interfaces.StaffService
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.lang.NonNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class JwtAuthenticationFilter(
    private val jwtServiceImpl: JwtServiceImpl,
    private val staffService: StaffService,
) : OncePerRequestFilter() {
    private val log: Logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        @NonNull request: HttpServletRequest,
        @NonNull response: HttpServletResponse,
        @NonNull filterChain: FilterChain,
    ) {
        val requestUri = request.requestURI
        log.debug("Processing authentication filter for URI: $requestUri")

        try {
            val authHeader = request.getHeader(HEADER_NAME)
            if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, BEARER_PREFIX)) {
                log.debug("No JWT token found in request to $requestUri, continuing filter chain")
                filterChain.doFilter(request, response)
                return
            }

            val jwt = authHeader.substring(BEARER_PREFIX.length)
            log.debug("JWT token found in Authorization header for $requestUri")

            val username: String = jwtServiceImpl.extractUserName(jwt)
            log.info("Processing authentication for user: $username from Keycloak JWT")

            val role: String = jwtServiceImpl.extractRoles(jwt)
            val newStaff =
                Staff().apply {
                    staffName = username
                    if (role.isNotBlank()) {
                        this.role = StaffRole.ROLE_ARTIFACTER
                        log.debug("User $username has ROLE_ARTIFACTER from Keycloak")
                    } else {
                        log.debug("User $username has no artifacter role from Keycloak")
                    }
                }

            if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().authentication == null) {
                log.debug("No existing authentication found, creating new authentication for user: $username")

                try {
                    staffService.getOrCreateStaff(staff = newStaff)
                    log.debug("Staff entity created/retrieved for user: $username")
                } catch (e: DataIntegrityViolationException) {
                    log.warn("Data integrity violation when creating staff for user $username: ${e.message}")
                }

                val userDetails: UserDetails? = staffService.getUserDetailsService().loadUserByUsername(username)

                if (jwtServiceImpl.isTokenValid(jwt, userDetails!!)) {
                    log.info("JWT token validated successfully for user: $username, setting authentication context")
                    val context = SecurityContextHolder.createEmptyContext()
                    val authToken =
                        UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.authorities,
                        )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    context.authentication = authToken
                    SecurityContextHolder.setContext(context)
                    log.debug("Authentication context set for user: $username")
                } else {
                    log.warn("JWT token validation failed for user: $username")
                }
            } else if (SecurityContextHolder.getContext().authentication != null) {
                log.debug("User $username already authenticated, skipping authentication setup")
            }

            filterChain.doFilter(request, response)
        } catch (ex: Exception) {
            log.error("Authentication filter error for URI $requestUri: ${ex.message}", ex)
            response.status = HttpStatus.BAD_REQUEST.value()
            response.writer.write("message: ")
            response.writer.flush()
        }
    }

    companion object {
        const val BEARER_PREFIX: String = "Bearer "
        const val HEADER_NAME: String = "Authorization"
    }
}
