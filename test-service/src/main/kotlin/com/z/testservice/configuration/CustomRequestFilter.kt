package com.z.testservice.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.hystrix.exception.HystrixRuntimeException
import com.z.testservice.domain.Message
import com.z.testservice.service.JWTService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.regex.Pattern
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class CustomRequestFilter : OncePerRequestFilter(){
    @Autowired private lateinit var tokenService: JWTService
    @Autowired private lateinit var mapper: ObjectMapper

    @Value("\${jsonWebToken.header:Authorization}") private lateinit var headerString: String
    @Value("\${request.filter.jsonWebToken.do-not-eval}") private lateinit var doNotEval:String
    @Value("\${request.filter.resources-regex}") private lateinit var resourcesRegex:String
    @Value("\${request.filter.swagger-redirect}") private lateinit var swaggerRedirect:Array<String>
    private val _logger = LoggerFactory.getLogger(CustomRequestFilter::class.java)


    @Throws(IOException::class)
    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, filterChain: FilterChain) {
        _logger.debug("${req.requestURI} - ${req.queryString} - ip: ${req.remoteHost}")
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

    private fun getAuthentication(req: HttpServletRequest, res: HttpServletResponse, filterChain: FilterChain){
        try{
            val authentication = tokenService.getAuthentication(req.getHeader(headerString)?:req.getParameter("token")?:"")//get auth from token
            //_logger.debug("username: ${authentication.name}")
            SecurityContextHolder.getContext().authentication = authentication//set auth on spring security context
            filterChain.doFilter(req, res)//continue request
        }catch(exception: RuntimeException ){
            if(exception is HystrixRuntimeException){
                _logger.debug("hystrix error : ${exception.failureType.name} - ${exception.fallbackException.localizedMessage} - ${exception.localizedMessage} - ${exception.cause?.message}")
            }
            _logger.debug("auth error: ${exception.message?:exception.toString()}")
            res.status = HttpServletResponse.SC_BAD_REQUEST
            res.addHeader("Content-Type", "application/json")
            res.writer.write(mapper.writeValueAsString(Message("auth error", message = exception.message?:exception.toString())))
        }
    }

    private fun match(uri:String,regex:String) = Pattern.compile(regex).matcher(uri).matches()

}