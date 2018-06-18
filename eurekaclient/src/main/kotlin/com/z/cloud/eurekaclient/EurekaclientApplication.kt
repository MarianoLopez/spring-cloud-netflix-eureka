package com.z.cloud.eurekaclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication
@EnableEurekaClient
class EurekaclientApplication
fun main(args: Array<String>) {
    runApplication<EurekaclientApplication>(*args)
}
