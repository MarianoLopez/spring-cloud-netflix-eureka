package com.z.cloud.eurekaclient2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.openfeign.EnableFeignClients


@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
class Eurekaclient2Application()
fun main(args: Array<String>) {
    runApplication<Eurekaclient2Application>(*args)
}



