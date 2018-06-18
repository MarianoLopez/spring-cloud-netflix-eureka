package com.z.cloud.eurekaclient.controllers

import com.z.cloud.eurekaclient.data.Person
import com.z.cloud.eurekaclient.services.PersonService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/person")
class PersonController(val personService: PersonService){
    @GetMapping
    fun getAll(): List<Person> = personService.getAll()
    @GetMapping("/{name}") fun getByName(@PathVariable name:String): List<Person> = personService.getByName(name)
    @GetMapping("/byId/{id}") fun getById(@PathVariable id:String): Person? = personService.getById(id)
    @PostMapping
    fun add(@RequestBody person: Person): Boolean = personService.add(person)
    @PutMapping
    fun update(@RequestBody person: Person): Boolean = personService.update(person)
    @DeleteMapping("/{id}") fun delete(@PathVariable id:String): Boolean = personService.delete(id)
}