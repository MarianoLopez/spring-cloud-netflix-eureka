package com.z.userservice.configuration


import com.google.common.base.Predicates.or
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.builders.PathSelectors.regex
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


/*Configuración de la documentación de la API*/
@Configuration
@EnableSwagger2
class Swagger {
    val basePackage = "com.z.userservice"
    val metaData = ApiInfo("API Docs", "REST API", "1.0", "Terms of service", Contact("Mariano Lopez", "-", "m_villa@hotmail.com"), "Apache License Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0")

    @Bean
    fun login(): Docket = Docket(DocumentationType.SWAGGER_2).select()
            .apis(RequestHandlerSelectors.basePackage("$basePackage.controller"))
            .paths(regex("/login|/token-info"))
            .build()
            .groupName("1-login")
            .apiInfo(metaData)

    @Bean
    fun userRole(): Docket = Docket(DocumentationType.SWAGGER_2).select()
            .apis(RequestHandlerSelectors.basePackage("$basePackage.controller"))
            .paths(or(regex("/role.*"), regex("/user.*")))
            .build()
            .globalOperationParameters(
                    listOf(ParameterBuilder()
                            .name("Authorization")
                            .description("Token auth")
                            .modelRef(ModelRef("string"))
                            .parameterType("header")
                            .required(true)
                            .build())
            )
            .groupName("2-UserRole")
            .apiInfo(metaData)
}