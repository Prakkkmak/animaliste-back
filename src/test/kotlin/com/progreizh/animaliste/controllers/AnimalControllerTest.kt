package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.entities.Animal
import com.progreizh.animaliste.repositories.AnimalRepository
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
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

    @BeforeEach
    fun setUp() {
        animalRepository.deleteAll()
    }

    private fun getRootUrl(): String? = "http://localhost:$port/animals"


    fun saveOneAnimal() = animalRepository.save(
            Animal(
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
    )


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

}