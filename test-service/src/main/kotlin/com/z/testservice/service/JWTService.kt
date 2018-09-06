package com.z.testservice.service

import com.z.testservice.feign.JwtFeign
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service

@Service
class JWTService( private val jwtFeign: JwtFeign){

    //get auth from token
    fun getAuthentication(token: String): Authentication {
        if(token.isNotEmpty()){
            jwtFeign.tokenInfo(token).let {
                if(it.enabled){
                    val roles = it.claims["roles"]?.map { SimpleGrantedAuthority(it) } ?: emptyList()
                    return UsernamePasswordAuthenticationToken(it.subject,null,roles)
                }else{
                    throw object : DisabledException("User disabled") {}
                }
            }
        }
        throw object : AuthenticationCredentialsNotFoundException("Invalid token") {}
    }
}