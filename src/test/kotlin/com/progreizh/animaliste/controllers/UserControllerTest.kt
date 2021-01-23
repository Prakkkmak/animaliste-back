package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.converters.UserConverter
import com.progreizh.animaliste.dtos.UserDto
import com.progreizh.animaliste.repositories.UserRepository
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
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = ["spring.data.mongodb.database=animaliste_test"])
class UserControllerTest @Autowired constructor(
    private val repository: UserRepository,
    private val converter: UserConverter,
    private val restTemplate: TestRestTemplate
) {
    @LocalServerPort
    protected var port: Int = 0

    private val defaultAccountId: String = ObjectId.get().toHexString()

    private val defaultUser: UserDto = UserDto(
        defaultAccountId,
        "default@mail.com",
        "p@ssw0rd",
        "Dupond"
    )

    private fun getRootUrl(): String = "http://localhost:$port/accounts"

    private fun saveOneAccount() = repository.save(converter.convertFromDto(defaultUser))

    @BeforeEach
    fun setUp() {
        repository.deleteAll()
    }

    @Test
    fun `should return all accounts`() {
        saveOneAccount()
        val response = restTemplate.getForEntity(
            getRootUrl(),
            List::class.java
        )
        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertNotNull(response.body)
        Assertions.assertEquals(1, response.body?.size)
    }

    @Test
    fun `should return single account by id`() {
        saveOneAccount()

        val response = restTemplate.getForEntity(
            getRootUrl() + "/$defaultAccountId",
            UserDto::class.java
        )

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertNotNull(response.body)
        Assertions.assertEquals(defaultAccountId, response.body?.id)
    }

    @Test
    fun `should return single account by mail and password`(){
        saveOneAccount()

        val response = restTemplate.getForEntity(
            getRootUrl() + "/login?mail={mail}&password={password}",
            UserDto::class.java,
            defaultUser.mail,
            defaultUser.password
        )

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertNotNull(response.body)
        Assertions.assertEquals(defaultAccountId, response.body?.id)
    }

    @Test
    fun `should not found single account by incorrect id`() {
        val response = restTemplate.getForEntity(
            getRootUrl() + "/not_an_id",
            Any::class.java
        )
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `should not found single account by incorrect password`(){
        saveOneAccount()

        val badPassword = "badpassword"

        val response = restTemplate.getForEntity(
            getRootUrl() + "/login?mail={mail}&password={password}",
            Any::class.java,
            defaultUser.mail,
            badPassword
        )

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
    }

    @Test
    fun `should not found single account by incorrect mail`(){
        saveOneAccount()

        val badMail = "badmail"

        val response = restTemplate.getForEntity(
            getRootUrl() + "/login?mail={mail}&password={password}",
            Any::class.java,
            badMail,
            defaultUser.password
        )

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.statusCode)
    }

    @Test
    fun `should return a new saved account`() {
        val response = restTemplate.postForEntity<UserDto>(getRootUrl(), defaultUser)
        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
        Assertions.assertNotNull(response.body)
        Assertions.assertEquals(defaultAccountId, response.body?.id)
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

        restTemplate.put(
            getRootUrl() + "/$defaultAccountId",
            newAccount,
            defaultAccountId)

        val response = restTemplate.getForEntity(
            getRootUrl() + "/$defaultAccountId",
            UserDto::class.java
        )

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
        Assertions.assertNotNull(response.body)
        Assertions.assertEquals(response.body?.name, newName)
        Assertions.assertNotEquals(response.body?.name, oldName)
    }

    @Test
    fun `should not found after deletion`() {
        saveOneAccount()
        restTemplate.delete(getRootUrl() + "/$defaultAccountId", defaultAccountId)

        val responseAfterDelete = restTemplate.getForEntity(
            getRootUrl() + "/$defaultAccountId",
            UserDto::class.java
        )

        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseAfterDelete.statusCode)
    }
}