package lab.`is`.bank.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lab.`is`.bank.authorization.database.entity.Staff
import lab.`is`.bank.authorization.database.entity.StaffRole
import lab.`is`.bank.authorization.service.interfaces.StaffService
import org.apache.commons.lang3.StringUtils
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
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        @NonNull request: HttpServletRequest,
        @NonNull response: HttpServletResponse,
        @NonNull filterChain: FilterChain,
    ) {
        try {
            val authHeader = request.getHeader(HEADER_NAME)
            if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, BEARER_PREFIX)) {
                filterChain.doFilter(request, response)
                return
            }
            val jwt = authHeader.substring(BEARER_PREFIX.length)

            val username: String = jwtServiceImpl.extractUserName(jwt)
            val role: String = jwtServiceImpl.extractRoles(jwt)
            val newStaff =
                Staff().apply {
                    staffName = username
                    if (role.isNotBlank()) {
                        this.role = StaffRole.ROLE_ARTIFACTER
                    }
                }

            if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().authentication == null) {
                try {
                    staffService.getOrCreateStaff(staff = newStaff)
                } catch (_: DataIntegrityViolationException) {
                }

                val userDetails: UserDetails? = staffService.getUserDetailsService().loadUserByUsername(username)

                if (jwtServiceImpl.isTokenValid(jwt, userDetails!!)) {
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
                }
            }
            filterChain.doFilter(request, response)
        } catch (ex: Exception) {
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
