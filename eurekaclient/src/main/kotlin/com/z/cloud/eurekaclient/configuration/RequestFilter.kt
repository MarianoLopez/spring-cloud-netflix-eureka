package com.z.cloud.eurekaclient.configuration

import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
class RequestFilter: Filter {
    private val logger = LoggerFactory.getLogger(RequestFilter::class.java)
    override fun destroy() {

    }

    override fun init(p0: FilterConfig?) {

    }

    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        req as HttpServletRequest
        logger.debug("${req.method} - ${req.requestURL}")
        chain.doFilter(req,res)
    }
}