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
){

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
    }

    @Test
    fun `should return single animal with new id`() {
        val response = restTemplate.postForEntity<Animal>(getRootUrl(), defaultAnimal)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
        assertEquals(defaultAnimalId, response.body?.id)
    }

    @Test
    fun `should return animal list without animal` (){
        saveOneAnimal()
        restTemplate.delete(getRootUrl()+"", defaultAnimalId)

        val response = restTemplate.getForEntity(
            getRootUrl() + "/$defaultAnimalId",
            Animal::class.java
        )

        assertEquals(200, response.statusCode.value())
    }
}