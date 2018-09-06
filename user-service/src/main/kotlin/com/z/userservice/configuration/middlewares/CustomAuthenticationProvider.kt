package com.z.userservice.configuration.middlewares

import com.z.userservice.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationProvider: AuthenticationProvider {//spring security auth implementation
    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var encoder: PasswordEncoder

    @Throws(AuthenticationException::class,UsernameNotFoundException::class)
    override fun authenticate(authentication: Authentication): Authentication{
        val name = authentication.name
        val password = authentication.credentials.toString()
        val dbUser = userService.loadUserByUsername(name)
        if(dbUser.isEnabled){
            if(dbUser.username == name && encoder.matches(password,dbUser.password)){
                return UsernamePasswordAuthenticationToken(name, password, dbUser.authorities)
            }else{
                throw object : BadCredentialsException("bad credentials") {}
            }
        }else{
            throw object : DisabledException("user disabled") {}
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}