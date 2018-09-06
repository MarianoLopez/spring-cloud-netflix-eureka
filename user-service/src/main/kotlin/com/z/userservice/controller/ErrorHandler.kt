package com.z.userservice.controller

import com.z.userservice.domain.Message
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class ErrorHandler{
    private val logger = LoggerFactory.getLogger(ErrorHandler::class.java)

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception::class)
    fun conflict(req: HttpServletRequest, ex:Exception): Message {
        logger.error(ex.toString())
        return Message(title = "ExceptionHandler", message = ex.message ?: ex.toString())
    }
}