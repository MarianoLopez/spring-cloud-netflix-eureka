package com.z.testservice.hystrix

import com.z.testservice.domain.TokenResponse
import com.z.testservice.feign.JwtFeign
import org.springframework.stereotype.Component


@Component
class JwtClientFallback : JwtFeign{
    override fun tokenInfo(token: String): TokenResponse {
        println("hystrix!")
        throw object : Exception("Feign client exception"){}
    }
}