package com.z.cloud.eurekaclient2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*


@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@RestController
@RequestMapping("/feign")
class Eurekaclient2Application(val personFeignService: PersonFeignService){
    @GetMapping fun getAll(): List<Person> = personFeignService.getAll()
    @GetMapping("/{name}") fun getByName(@PathVariable name:String): List<Person> = personFeignService.getByName(name)
    @GetMapping("/byId/{id}") fun getById(@PathVariable id:String): Person? = personFeignService.getById(id)
    @PostMapping fun add(@RequestBody person: Person): Boolean = personFeignService.add(person)
    @PutMapping fun update(@RequestBody person: Person): Boolean = personFeignService.update(person)
    @DeleteMapping("/{id}") fun delete(@PathVariable id:String): Boolean = personFeignService.delete(id)

}

fun main(args: Array<String>) {
    runApplication<Eurekaclient2Application>(*args)
}

@FeignClient("spring-cloud-eureka-person-service")
interface PersonFeignService {
    @GetMapping("/person") fun getAll(): List<Person>
    @GetMapping("/person/{name}") fun getByName(@PathVariable name:String): List<Person>
    @GetMapping("/person/byId/{id}") fun getById(@PathVariable id:String): Person?
    @PostMapping("/person") fun add(@RequestBody person: Person): Boolean
    @PutMapping("/person") fun update(@RequestBody person: Person): Boolean
    @DeleteMapping("/person/{id}") fun delete(@PathVariable id:String): Boolean
}


data class Person(var id:String?,val name:String){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as Person
        if(other.id!=id) return false
        return true
    }
}
