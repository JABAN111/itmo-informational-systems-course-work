//package lab.`is`.bank.security
//
//import jakarta.annotation.PostConstruct
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.core.convert.converter.Converter
//import org.springframework.lang.NonNull
//import org.springframework.security.authentication.AbstractAuthenticationToken
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.springframework.security.oauth2.jwt.*
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
//import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
//import org.springframework.stereotype.Component
//import java.util.stream.Collectors
//import java.util.stream.Stream
//
//@Component
//class JwtAuthConverter :
//    Converter<Jwt, AbstractAuthenticationToken> {
//    private val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
//
//    @Value("\${jwt.auth.converter.principle-attribute}")
//    private lateinit var principleAttribute: String
//
//    @Value("\${jwt.auth.converter.resource-id}")
//    private lateinit var resourceId: String
//
//    @PostConstruct
//    fun init() {
//        println(">>> JwtAuthConverter initialized") // Должно появиться в логах при старте приложения
//    }
//
//
//    override fun convert(@NonNull jwt: Jwt): AbstractAuthenticationToken {
//        println(">>> JwtAuthConverter invoked") // Если не появляется - метод вообще не вызывается
//
//        val authorities: Collection<GrantedAuthority> = Stream.concat(
//            jwtGrantedAuthoritiesConverter.convert(jwt)!!.stream(),
//            extractResourceRoles(jwt).stream()
//        ).collect(Collectors.toSet())
//
//        println("Granted Authorities: ${authorities.map { it.authority }}")  // <-- Добавил лог
//        println(">>> Granted Authorities: ${authorities.map { it.authority }}")
//        return JwtAuthenticationToken(
//            jwt,
//            authorities,
//            getPrincipleClaimName(jwt)
//        )
//    }
//
//    private fun getPrincipleClaimName(jwt: Jwt): String {
//        var claimName = JwtClaimNames.SUB
//        if (principleAttribute != null) {
//            claimName = principleAttribute
//        }
//        return jwt.getClaim(claimName)
//    }
//
//
//    //    private fun extractResourceRoles(jwt: Jwt): Collection<GrantedAuthority> {
////        val resource: Map<String, Any>?
////        if (jwt.getClaim<Any?>("resource_access") == null) {
////            return emptySet()
////        }
////        val resourceAccess = jwt.getClaim<Map<String?, Any?>>("resource_access")
////
////        if (resourceAccess[resourceId] == null) {
////            return emptySet()
////        }
////        resource = resourceAccess[resourceId] as Map<String, Any>?
////
////        val resourceRoles = resource!!["roles"] as Collection<String>?
////        return resourceRoles!!
////            .stream()
////            .map { role: String -> SimpleGrantedAuthority("ROLE_$role") }
////            .collect(Collectors.toSet())
////    }
//    private fun extractResourceRoles(jwt: Jwt): Collection<GrantedAuthority> {
//        val resourceAccess = jwt.getClaim<Map<String, Any>>("resource_access") ?: return emptySet()
//        val resource = resourceAccess[resourceId] as? Map<String, Any> ?: return emptySet()
//        val resourceRoles = resource["roles"] as? Collection<String> ?: return emptySet()
//
//        return resourceRoles.map { role -> SimpleGrantedAuthority("ROLE_$role") }.toSet()
//    }
//
//
//}