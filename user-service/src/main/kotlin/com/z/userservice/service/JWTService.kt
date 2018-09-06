package com.z.userservice.service


import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.z.userservice.domain.TokenResponse
import com.z.userservice.util.asDate
import com.z.userservice.util.asLocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.time.LocalDateTime

class JWTService(private val secret: String, private val prefix: String, private val issuer:String){
    @Qualifier("UserService") @Autowired private lateinit var userDetails: UserDetailsService


    @Throws(JWTCreationException::class)
    fun createToken(subject:String, expiration: LocalDateTime, claims:Map<String,Array<String>> = emptyMap()): TokenResponse {
        val token = JWT.create().apply {
            withSubject(subject)
            withIssuer(issuer)
            withExpiresAt(expiration.asDate())
            claims.entries.forEach { claim->  withArrayClaim(claim.key, claim.value)}
        }.sign(Algorithm.HMAC512(secret))
        return TokenResponse(token=token,expires = expiration, subject = subject,claims = claims)

    }

    //get auth from token
    @Throws(UsernameNotFoundException::class,JWTVerificationException::class)
    fun getAuthentication(token: String): Authentication? {
        decode(token).let {
            userDetails.loadUserByUsername(it.subject).let {
                return UsernamePasswordAuthenticationToken(it.username, it.password, it.authorities)
            }
        }
    }


    @Throws(JWTVerificationException::class)
    fun getInfo(token:String): TokenResponse {
        decode(token).let {jwt->
            userDetails.loadUserByUsername(jwt.subject).let {
                return TokenResponse(token = jwt.token, expires = jwt.expiresAt.asLocalDateTime(), claims = jwt.claims.mapValues { it.value.asArray(String::class.java) } , subject = jwt.subject, enabled = it.isEnabled)
            }
        }
    }

    @Throws(JWTVerificationException::class)
    private fun decode(token:String): DecodedJWT {
        val verifier = JWT.require(Algorithm.HMAC512(secret)).withIssuer(issuer).build() //config decoder
        return verifier.verify(token.replace(prefix, "").trim({ it <= ' ' })) //decode token
    }

    //private fun getIp(req:HttpServletRequest)=req.remoteAddr?.replace("0:0:0:0:0:0:0:1","127.0.0.1")?.replace("localhost","127.0.0.1") ?: "unknown"
}