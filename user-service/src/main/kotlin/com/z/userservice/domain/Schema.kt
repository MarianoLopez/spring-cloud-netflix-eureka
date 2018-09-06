package com.z.userservice.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDateTime

/*Schemas*/
@Document
data class User (
        @Id val id: String?, var name:String,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) var password:String,
        var state:Boolean=true, var roles:List<Role>, val createdDate: LocalDateTime = LocalDateTime.now())

@Document data class Role(@Id val id: String?, val name:String)
//@Document data class WebRule(@Id val id:String,var httpMethod: HttpMethod?=null,var pattern:String, var roles:List<String> = emptyList())

/*DAO*/
//interface WebRuleDAO: MongoRepository<WebRule,String>
interface RoleDAO : MongoRepository<Role, String> { fun findFirstByName(name: String): Role? }
interface UserDAO: MongoRepository<User, String> {
    fun findFirstByName(name: String): User?
    fun findByState(state: Boolean): List<User>?
}