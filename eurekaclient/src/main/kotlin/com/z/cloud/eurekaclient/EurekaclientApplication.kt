package com.z.cloud.eurekaclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.util.*

@SpringBootApplication
@EnableEurekaClient
@RestController
@RequestMapping("/person")
class EurekaclientApplication(val personService: PersonService){
    @GetMapping fun getAll(): List<Person> = personService.getAll()
    @GetMapping("/{name}") fun getByName(@PathVariable name:String): List<Person> = personService.getByName(name)
    @GetMapping("/byId/{id}") fun getById(@PathVariable id:String): Person? = personService.getById(id)
    @PostMapping fun add(@RequestBody person: Person): Boolean = personService.add(person)
    @PutMapping fun update(@RequestBody person: Person): Boolean = personService.update(person)
    @DeleteMapping("/{id}") fun delete(@PathVariable id:String): Boolean = personService.delete(id)
}

@Service
class PersonService(){
    private val persons = mutableListOf(Person(UUID.randomUUID().toString(),"Fulano"),Person(UUID.randomUUID().toString(),"Sarasa"))

    fun getAll(): List<Person> = persons

    @Synchronized fun add(person: Person): Boolean{
        return if(!persons.contains(person)){
            person.id =UUID.randomUUID().toString()
            persons.add(person)
        }else false
    }
    @Synchronized fun delete(id:String): Boolean = persons.remove(Person(id = id,name = ""))

    @Synchronized fun update(person: Person): Boolean{
        return if(persons.contains(person)){
            persons[persons.indexOf(person)] = person
            true
        }else false
    }
    fun getByName(name:String): List<Person> = persons.filter { it.name == name }
    fun getById(id:String):Person? = persons.find { it.id == id }
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

fun main(args: Array<String>) {
    runApplication<EurekaclientApplication>(*args)
}
