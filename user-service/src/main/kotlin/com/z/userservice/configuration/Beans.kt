package com.z.userservice.configuration


import com.z.userservice.service.JWTService
import com.z.userservice.configuration.middlewares.CustomAuthenticationProvider
import com.z.userservice.configuration.middlewares.CustomRequestFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class Beans {
    //read from application.properties
    @Value("\${jsonWebToken.secret}") private lateinit var secret: String
    @Value("\${jsonWebToken.token-prefix}") private lateinit var tokenPrefix: String
    @Value("\${jsonWebToken.issuer}") private lateinit var issuer: String

    @Bean
    @Primary
    fun authProvider() = CustomAuthenticationProvider()//auth provider implementation

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun jwtService()= JWTService(secret = secret, prefix = tokenPrefix, issuer = issuer)//init jwt Services

    @Bean
    fun customRequestFilter() = CustomRequestFilter()
}