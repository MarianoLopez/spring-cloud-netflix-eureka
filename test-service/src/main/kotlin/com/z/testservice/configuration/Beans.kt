package com.z.testservice.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Beans {
    @Bean
    fun customRequestFilter() = CustomRequestFilter()
}