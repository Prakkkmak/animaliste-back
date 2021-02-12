package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.services.JwtTokenService
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = ["spring.data.mongodb.database=animaliste_test"])
abstract class ControllerTest {
    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var jwtTokenService: JwtTokenService

    @LocalServerPort
    protected var port: Int = 0

    protected abstract fun getRootUrl() : String

    fun <T> rest(method: HttpMethod, route: String, body: Any?, responseType : Class<T> ) : ResponseEntity<T>{
        val url = getRootUrl() + route
        val headers = HttpHeaders()
        headers.set("Authorization" , jwtTokenService.generateToken("test@test.fr", "12345678"))
        val entity = HttpEntity<Any>(body, headers)
        return restTemplate.exchange(url, method, entity, responseType)
    }

    fun <T> rest(method: HttpMethod,body: Any, responseType : Class<T>) : ResponseEntity<T>{
        return rest(method,"", body, responseType)
    }

    fun <T> rest(method: HttpMethod,responseType : Class<T>) : ResponseEntity<T>{
        return rest(method,"", null, responseType)
    }

    fun <T> rest(method: HttpMethod,route: String, responseType : Class<T>) : ResponseEntity<T>{
        return rest(method, route, null, responseType)
    }

    fun rest(method: HttpMethod,route: String) : ResponseEntity<Any>{
        return rest(method, route, null, Any::class.java)
    }

    fun rest(method: HttpMethod) : ResponseEntity<Any>{
        return rest(method, "", null, Any::class.java)
    }

    fun rest(method: HttpMethod, body: Any) : ResponseEntity<Any>{
        return rest(method, "", body, Any::class.java)
    }
}