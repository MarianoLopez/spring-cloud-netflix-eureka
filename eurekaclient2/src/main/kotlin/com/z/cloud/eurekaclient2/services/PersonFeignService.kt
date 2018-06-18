package com.z.cloud.eurekaclient2.services

import com.z.cloud.eurekaclient2.data.Person
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*

@FeignClient("spring-cloud-eureka-person-service")
interface PersonFeignService {
    @GetMapping("/person") fun getAll(): List<Person>
    @GetMapping("/person/{name}") fun getByName(@PathVariable name:String): List<Person>
    @GetMapping("/person/byId/{id}") fun getById(@PathVariable id:String): Person?
    @PostMapping("/person") fun add(@RequestBody person: Person): Boolean
    @PutMapping("/person") fun update(@RequestBody person: Person): Boolean
    @DeleteMapping("/person/{id}") fun delete(@PathVariable id:String): Boolean
}