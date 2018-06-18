package com.z.cloud.eurekaclient.services

import com.z.cloud.eurekaclient.data.Person
import org.springframework.stereotype.Service
import java.util.*

@Service
class PersonService{
    private val persons = mutableListOf(Person(UUID.randomUUID().toString(),"Fulano"),Person(UUID.randomUUID().toString(),"Sarasa"))

    fun getAll(): List<Person> = persons

    @Synchronized fun add(person: Person): Boolean{
        return if(!persons.contains(person)){
            person.id = UUID.randomUUID().toString()
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
    fun getById(id:String): Person? = persons.find { it.id == id }
}