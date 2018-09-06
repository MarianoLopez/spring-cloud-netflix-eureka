package com.z.userservice.domain

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime


data class Message(val title:String, @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") val date: LocalDateTime = LocalDateTime.now(), val message: Any?= null)
data class AccountCredentials(val username: String, val password: String)
data class TokenResponse(val token:String, val expires:LocalDateTime, val claims:Map<String,Array<String>> = emptyMap(), val subject:String, val enabled:Boolean = true)