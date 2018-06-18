package com.z.cloud.eurekaclient2.controllers

import com.z.cloud.eurekaclient2.data.Person
import com.z.cloud.eurekaclient2.services.PersonFeignService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/feignPerson")
class PersonController(private val personFeignService: PersonFeignService) {
    @GetMapping fun getAll(): List<Person> = personFeignService.getAll()
    @GetMapping("/{name}") fun getByName(@PathVariable name:String): List<Person> = personFeignService.getByName(name)
    @GetMapping("/byId/{id}") fun getById(@PathVariable id:String): Person? = personFeignService.getById(id)
    @PostMapping fun add(@RequestBody person: Person): Boolean = personFeignService.add(person)
    @PutMapping fun update(@RequestBody person: Person): Boolean = personFeignService.update(person)
    @DeleteMapping("/{id}") fun delete(@PathVariable id:String): Boolean = personFeignService.delete(id)

}