package com.z.testservice.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
@EnableWebSecurity
class WebSecurity(private val customRequestFilter: CustomRequestFilter): WebSecurityConfigurerAdapter(),WebMvcConfigurer{
    @Value("\${request.filter.cors.allow-origin}") private lateinit var allowOrigin: String
    @Value("\${request.filter.cors.allow-methods}") private lateinit var allowMethods: String
    @Value("\${request.filter.cors.max-age}") private lateinit var maxAge: String
    @Value("\${request.filter.cors.allow-headers}") private lateinit var allowHeaders: String


    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()//no csrf
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()//no session
            .authorizeRequests()
                .antMatchers("/api/**").hasAnyRole("ADMIN","TESTER").and()
            .addFilterAt(customRequestFilter,UsernamePasswordAuthenticationFilter::class.java)
    }
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedOrigins(allowOrigin)
                .allowedHeaders(allowHeaders)
                .allowedMethods(allowMethods)
                .maxAge(maxAge.toLong())
    }
}