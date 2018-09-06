package com.z.userservice.controller

import com.z.userservice.configuration.middlewares.CustomAuthenticationProvider
import com.z.userservice.domain.AccountCredentials
import com.z.userservice.domain.TokenResponse
import com.z.userservice.service.JWTService
import io.swagger.annotations.ApiOperation
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

@RestController("/")
class LoginController(private val tokenAuth: JWTService, private val authProvider: CustomAuthenticationProvider) {
    @ApiOperation(value = "Solicitar token de autenticación",produces = "application/json;charset=UTF-8")
    @PostMapping("login") fun login(@RequestBody credentials: AccountCredentials): TokenResponse {
        authProvider.authenticate(UsernamePasswordAuthenticationToken(credentials.username, credentials.password, Collections.emptyList())).let {
            return tokenAuth.createToken(subject = it.name, expiration = LocalDateTime.now().plusWeeks(2),
                    claims = mapOf("roles" to it.authorities.map { it.authority }.toTypedArray()))
        }
    }

    @ApiOperation(value = "Solicitar información de un token particular (IPs permitidas: 127.0.0.1)",produces = "application/json;charset=UTF-8")
    @GetMapping("token-info/{token}") fun tokenInfo(@PathVariable token:String):TokenResponse = tokenAuth.getInfo(token)
}