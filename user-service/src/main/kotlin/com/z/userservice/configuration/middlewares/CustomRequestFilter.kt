package com.z.userservice.configuration.middlewares

import com.fasterxml.jackson.databind.ObjectMapper
import com.z.userservice.domain.Message
import com.z.userservice.service.JWTService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.regex.Pattern
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class CustomRequestFilter : OncePerRequestFilter(){
    @Autowired private lateinit var tokenService: JWTService
    @Autowired private lateinit var mapper: ObjectMapper

    @Value("\${jsonWebToken.header}") private val headerString: String = "Authorization"
    @Value("\${request.filter.jsonWebToken.do-not-eval}") private val doNotEval:String = ""
    @Value("\${request.filter.resources-regex}") private val resourcesRegex:String = ""
    @Value("\${request.filter.swagger-redirect}") private val swaggerRedirect:Array<String> = arrayOf("/api","/swagger","/")
    private val _logger = LoggerFactory.getLogger(CustomRequestFilter::class.java)

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, filterChain: FilterChain) {
        _logger.debug("ip: ${req.remoteHost} - ${req.requestURI} - ${req.queryString}")
        if (swaggerRedirect.contains(req.requestURI)){//check for redirect to swagger
            _logger.debug("swagger redirect")
            res.sendRedirect(req.contextPath+ "/swagger-ui.html")
        }else{
            val dne = match(req.requestURI,doNotEval)
            val r = match(req.requestURI,resourcesRegex)
            _logger.debug("evaluation: do not eval-> $dne, resources-> $r")
            if(dne||r){//check forward
                filterChain.doFilter(req, res)//continue request
            }else{
                _logger.debug("token check")
                getAuthentication(req, res, filterChain)
            }
        }
    }
    private fun match(uri:String,regex:String) = Pattern.compile(regex).matcher(uri).matches()

    private fun getAuthentication(req: HttpServletRequest, res: HttpServletResponse, filterChain: FilterChain){
        try{
            val authentication = tokenService.getAuthentication(req.getHeader(headerString)?:req.getParameter("token"))//get auth from token
            SecurityContextHolder.getContext().authentication = authentication//set auth on spring security context
            filterChain.doFilter(req, res)//continue request
        }catch(exception: RuntimeException ){
            _logger.debug("auth: ${exception.message?:exception.toString()}")
            res.status = HttpServletResponse.SC_BAD_REQUEST
            res.addHeader("Content-Type", "application/json")
            res.writer.write(mapper.writeValueAsString(Message("auth error", message = exception.message?:exception.toString())))
        }
    }

}