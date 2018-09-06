package com.z.eurekaservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter


@EnableEurekaServer
@SpringBootApplication
class EurekaServiceApplication: WebSecurityConfigurerAdapter(){
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
                .withUser("eureken").password("{noop}3ur3k4!").authorities("ADMIN")
    }

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .authorizeRequests()
                .anyRequest().authenticated().and()
            .httpBasic()
    }
}


fun main(args: Array<String>) {
    runApplication<EurekaServiceApplication>(*args)
}
