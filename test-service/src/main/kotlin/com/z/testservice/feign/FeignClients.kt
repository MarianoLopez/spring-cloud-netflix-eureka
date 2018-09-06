package com.z.testservice.feign

import com.z.testservice.domain.TokenResponse
import com.z.testservice.hystrix.JwtClientFallback
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient("user-service", fallback = JwtClientFallback::class)
interface JwtFeign {
    @GetMapping("/token-info/{token}") fun tokenInfo(@PathVariable token:String): TokenResponse
}