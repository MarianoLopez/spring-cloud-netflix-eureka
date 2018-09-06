package com.z.testservice.controller

import com.z.testservice.domain.Message
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.PostConstruct
import java.net.InetAddress
import java.util.Enumeration
import java.net.NetworkInterface



@RestController
@RequestMapping("api")
class Main{
   /* @PostConstruct
    fun test(){
        println("---------------------------------------------------")
        val e = NetworkInterface.getNetworkInterfaces()
        while (e.hasMoreElements()) {
            val n = e.nextElement() as NetworkInterface
            println(n.name)
            val ee = n.inetAddresses
            while (ee.hasMoreElements()) {
                val i = ee.nextElement() as InetAddress
                println("\t ${i.hostAddress}")
            }
        }
        println("---------------------------------------------------")
    }*/

    private val test = listOf(Message(title = "this is a test"), Message(title = "Another test"))
    @GetMapping("/messages") fun index() = this.test
}