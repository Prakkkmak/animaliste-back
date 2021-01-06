package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.entities.Animal
import com.progreizh.animaliste.repositories.AnimalRepository
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.*
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
class AnimalControllerTest @Autowired constructor(
    private val animalRepository: AnimalRepository,
    private val restTemplate: TestRestTemplate
) {

    @LocalServerPort
    protected var port: Int = 0

    private val defaultAnimalId: String = ObjectId.get().toHexString()

    private val defaultAnimal: Animal = Animal(
        defaultAnimalId,
        "Watson",
        "Chat",
        true
    )

    private fun getRootUrl(): String = "http://localhost:$port/animals"

    private fun saveOneAnimal() = animalRepository.save(defaultAnimal)

    @BeforeEach
    fun setUp() {
        animalRepository.deleteAll()
    }

    @Test
    fun `should return all animals`() {
        saveOneAnimal()
        val response = restTemplate.getForEntity(
            getRootUrl(),
            List::class.java
        )
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(1, response.body?.size)
    }

    @Test
    fun `should return single animal by id`() {
        saveOneAnimal()

        val response = restTemplate.getForEntity(
            getRootUrl() + "/$defaultAnimalId",
            Animal::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(defaultAnimalId, response.body?.id)
    }

    @Test
    fun `should not found single animal by incorrect id`() {
        val response = restTemplate.getForEntity(
            getRootUrl() + "/not_an_id",
            Any::class.java
        )
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `should return a new saved animal`() {
        val response = restTemplate.postForEntity<Animal>(getRootUrl(), defaultAnimal)
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
        assertEquals(defaultAnimalId, response.body?.id)
    }

    @Test
    fun `should return single animal with new information`() {
        val oldAnimal = saveOneAnimal()
        val oldSpecie = oldAnimal.specie
        val newSpecie = "Chien"
        val newAnimal = Animal(
            defaultAnimalId,
            "Watson",
            newSpecie,
            true
        )

        restTemplate.put(
            getRootUrl() + "/$defaultAnimalId",
            newAnimal,
            defaultAnimalId)

        val response = restTemplate.getForEntity(
            getRootUrl() + "/$defaultAnimalId",
            Animal::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(response.body?.specie, newSpecie)
        assertNotEquals(response.body?.specie, oldSpecie)
    }

    @Test
    fun `should not found after deletion`() {
        saveOneAnimal()
        restTemplate.delete(getRootUrl() + "/$defaultAnimalId", defaultAnimalId)

        val responseAfterDelete = restTemplate.getForEntity(
            getRootUrl() + "/$defaultAnimalId",
            Animal::class.java
        )

        assertEquals(HttpStatus.NOT_FOUND, responseAfterDelete.statusCode)
    }
}