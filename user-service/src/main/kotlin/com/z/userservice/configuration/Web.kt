package com.z.userservice.configuration

import com.z.userservice.configuration.middlewares.CustomAuthenticationProvider
import com.z.userservice.configuration.middlewares.CustomRequestFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
@EnableWebSecurity
class Web(val customRequestFilter: CustomRequestFilter, val customAuthenticationProvider: CustomAuthenticationProvider) : WebSecurityConfigurerAdapter(), WebMvcConfigurer {
    @Value("\${request.filter.cors.allow-origin}") private lateinit var allowOrigin: String
    @Value("\${request.filter.cors.allow-methods}") private lateinit var allowMethods: String
    @Value("\${request.filter.cors.max-age}") private lateinit var maxAge: String
    @Value("\${request.filter.cors.allow-headers}") private lateinit var allowHeaders: String

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()//no csrf
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()//no session
                .authorizeRequests().antMatchers(HttpMethod.POST, "/login").permitAll().and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/token-info").access("hasIpAddress('0:0:0:0:0:0:0:1') or hasIpAddress('127.0.0.1') or hasIpAddress('192.168.94.1')").and()
                .authorizeRequests().antMatchers(HttpMethod.GET,"/user/**").hasAnyRole("TESTER","ADMIN").and()
                .authorizeRequests().antMatchers("/user/**").hasAnyRole("ADMIN").and()
                .authorizeRequests().antMatchers(HttpMethod.GET,"/role/**").hasAnyRole("TESTER","ADMIN").and()
                .authorizeRequests().antMatchers("/role/**").hasAnyRole("ADMIN").and()
        .addFilterBefore(customRequestFilter, UsernamePasswordAuthenticationFilter::class.java)// And filter check the presence of JWT in header & stuffs
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) { auth?.authenticationProvider(customAuthenticationProvider)}//spring custom auth provider

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedOrigins(allowOrigin)
                .allowedHeaders(allowHeaders)
                .allowedMethods(allowMethods)
                .maxAge(maxAge.toLong())
    }
}