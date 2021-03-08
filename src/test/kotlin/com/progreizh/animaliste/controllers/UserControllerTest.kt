package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.converters.UserConverter
import com.progreizh.animaliste.dtos.UserCredentialsDto
import com.progreizh.animaliste.dtos.UserDto
import com.progreizh.animaliste.entities.User
import com.progreizh.animaliste.repositories.UserRepository
import com.progreizh.animaliste.services.UserService
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

class UserControllerTest @Autowired constructor(
    private val repository: UserRepository,
    private val service: UserService,
) : ControllerTest() {

    private val defaultAccountId: String = ObjectId.get().toHexString()

    private val defaultUser = UserDto(
        defaultAccountId,
        "default@mail.com",
        "password",
        "Dupond"
    )

    override fun getRootUrl(): String = "http://localhost:$port/users"

    private fun saveOneAccount() = service.create(defaultUser)

    @BeforeEach
    fun setUp() {
        repository.deleteAll()
    }

    @Test
    fun `should return all accounts`() {
        saveOneAccount()
        val response = rest(HttpMethod.GET, List::class.java)
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertNotNull(response.body)
        Assertions.assertEquals(1, response.body?.size)
    }

    @Test
    fun `should return single account by id`() {
        saveOneAccount()

        val response = rest(HttpMethod.GET, "/$defaultAccountId", UserDto::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertNotNull(response.body)
        Assertions.assertEquals(defaultAccountId, response.body?.id)
    }

    @Test
    fun `should return single account by mail and password`(){
        saveOneAccount()
        val userCredentialsDto = UserCredentialsDto("default@mail.com", "password")
        val response = rest(HttpMethod.POST, "/login", userCredentialsDto, String::class.java)
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertNotNull(response.body)
    }

    @Test
    fun `should not found single account by incorrect id`() {
        val response = rest(HttpMethod.GET, "/not_an_id")
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `should not found single account by incorrect password`(){
        saveOneAccount()
        val userCredentialsDto = UserCredentialsDto("default@mail.com", "password")
        val response = rest(HttpMethod.POST, "/login", userCredentialsDto, Any::class.java)
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.statusCode)
    }

    @Test
    fun `should not found single account by incorrect mail`(){
        saveOneAccount()
        val response = rest(HttpMethod.GET, "/login?mail=badmail&password=password")
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.statusCode)
    }

    @Test
    fun `should return a new saved account`() {
        val response = rest(HttpMethod.POST, "/register", defaultUser, UserDto::class.java)
        print(response)
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertNotNull(response.body)
        // Assertions.assertEquals(defaultAccountId, response.body?.id)
    }

    @Test
    fun `should return single acount with new information`() {
        val oldAccount = saveOneAccount()
        val oldName = oldAccount.name
        val newName = "Martin"

        val newAccount = UserDto(
            defaultAccountId,
            "default@mail.fr",
            "p@ssw0rd",
            newName
        )
        rest(HttpMethod.PUT, "/$defaultAccountId", newAccount, UserDto::class.java)
        val response = rest(HttpMethod.GET, "/$defaultAccountId", UserDto::class.java)
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertNotNull(response.body)
        Assertions.assertEquals(response.body?.name, newName)
        Assertions.assertNotEquals(response.body?.name, oldName)
    }

    @Test
    fun `should not found after deletion`() {
        saveOneAccount()
        rest(HttpMethod.DELETE, "/$defaultAccountId", UserDto::class.java)
        val responseAfterDelete = rest(HttpMethod.GET, "/$defaultAccountId")
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseAfterDelete.statusCode)
    }
}