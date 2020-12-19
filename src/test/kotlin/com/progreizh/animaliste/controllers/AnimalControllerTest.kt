package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.entities.Animal
import com.progreizh.animaliste.repositories.AnimalRepository
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
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
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AnimalControllerTest @Autowired constructor(
    private val animalRepository: AnimalRepository,
    private val restTemplate: TestRestTemplate
) {

    @LocalServerPort
    protected var port: Int = 0

    private val defaultAnimalId: String = ObjectId.get().toHexString()

    private val defaultAnimal: Animal = Animal(
        defaultAnimalId,
        "123456789012345",
        "123ABC",
        "0000001",
        Date(),
        "Description physique",
        true,
        "Chat",
        "Siamoix",
        "Description attitude",
        "Chiens",
        "Enfants",
        "Non",
        "Oeufs",
        "Mimic",
        "Mimic est un chat",
        "Imaginaire"
    )

    @BeforeEach
    fun setUp() {
        animalRepository.deleteAll()
    }

    private fun getRootUrl(): String = "http://localhost:$port/animals"


    fun saveOneAnimal() = animalRepository.save(defaultAnimal)


    @Test
    fun `should return all animals`() {
        saveOneAnimal()
        val response = restTemplate.getForEntity(
            getRootUrl(),
            List::class.java
        )

        assertEquals(200, response.statusCode.value())
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

        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(defaultAnimalId, response.body?.id)

        val responseIncorrectId = restTemplate.getForEntity(
            getRootUrl() + "test",
            Any::class.java
        )

        assertEquals(HttpStatus.NOT_FOUND, responseIncorrectId.statusCode)
    }

    @Test
    fun `should return single animal with new id`() {
        val response = restTemplate.postForEntity<Animal>(getRootUrl(), defaultAnimal)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
        assertEquals(defaultAnimalId, response.body?.id)
    }

    @Test
    fun `should return single animal with new information`() {
        saveOneAnimal()

        val newAnimal = Animal(
            defaultAnimalId,
            "123456789012345",
            "123ABC",
            "0000001",
            Date(),
            "Description physique",
            true,
            "Chien",
            "roux",
            "Description attitude",
            "Chiens",
            "Enfants",
            "Non",
            "Oeufs",
            "Mimic",
            "Mimic est un chien",
            "Imaginaire"
        )

        restTemplate.put(getRootUrl() + "/$defaultAnimalId", newAnimal, defaultAnimalId)

        val response = restTemplate.getForEntity(
            getRootUrl() + "/$defaultAnimalId",
            Animal::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(response.body!!.specie, "Chien")
        assertEquals(response.body!!.description, "Mimic est un chien")
        assertEquals(response.body!!.race, "roux")
    }

    @Test
    fun `should return a not found error after get request on deleted animal`() {
        saveOneAnimal()
        restTemplate.delete(getRootUrl() + "/$defaultAnimalId", defaultAnimalId)

        val responseAfterDelete = restTemplate.getForEntity(
            getRootUrl() + "/$defaultAnimalId",
            Animal::class.java
        )

        assertEquals(HttpStatus.NOT_FOUND, responseAfterDelete.statusCode)

        val responseIncorrectId = restTemplate.getForEntity(
            getRootUrl() + "123",
            Any::class.java
        )

        assertEquals(HttpStatus.NOT_FOUND, responseIncorrectId.statusCode)
    }
}