package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.entities.Animal
import com.progreizh.animaliste.repositories.AnimalRepository
import org.bson.types.ObjectId
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

class SpecieControllerTest @Autowired constructor(
    private val animalRepository: AnimalRepository,
) : ControllerTest(){


    private val animalDto1: Animal = Animal(
        ObjectId.get().toHexString(),
        "Watson",
        "Chat",
        true
    )
    private val animalDto2: Animal = Animal(
        ObjectId.get().toHexString(),
        "Robert",
        "Chien",
        true
    )

    private val animalDto3: Animal = Animal(
        ObjectId.get().toHexString(),
        "CJ",
        "Chien",
        false
    )

    override fun getRootUrl(): String = "http://localhost:$port/species"

    private fun saveAnimals(){
        animalRepository.save(animalDto1)
        animalRepository.save(animalDto2)
        animalRepository.save(animalDto3)
    }

    @BeforeEach
    fun setUp() {
        animalRepository.deleteAll()
        saveAnimals()
    }

    @Test
    fun `should return all species`() {
        val response = rest(HttpMethod.GET, Set::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertTrue(response.body!!.contains("Chat"))
        assertTrue(response.body!!.contains("Chien"))
        assertEquals(2, response.body?.size)
    }
}