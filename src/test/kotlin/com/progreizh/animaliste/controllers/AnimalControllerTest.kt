package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.entities.Animal
import com.progreizh.animaliste.repositories.AnimalRepository
import com.progreizh.animaliste.services.JwtTokenService
import com.progreizh.animaliste.services.SequenceGenearatorService
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
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*
import org.apache.coyote.http11.Constants.a
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate


class AnimalControllerTest @Autowired constructor(
    private val animalRepository: AnimalRepository,
) : ControllerTest() {

    private val defaultAnimalId: String = ObjectId.get().toHexString()

    private val defaultAnimalDto: Animal = Animal(
        defaultAnimalId,
        "Watson",
        "Chat",
        true
    )

    private val animalDtos: List<Animal> = listOf(
        defaultAnimalDto,
        Animal(
            ObjectId.get().toHexString(),
            "Bubule",
            "Chat",
            false
        ),
        Animal(
            ObjectId.get().toHexString(),
            "Roxy",
            "Chien",
            true
        ),
        Animal(
            ObjectId.get().toHexString(),
            "Coboy",
            "Hamster",
            true
        )
    )


    override fun getRootUrl(): String = "http://localhost:$port/animals"

    private fun saveDefaultAnimal() = animalRepository.save(defaultAnimalDto)

    @BeforeEach
    fun setUp() {
        animalRepository.deleteAll()
    }

    @Test
    fun `should return all animals`() {
        saveDefaultAnimal()
        val response = rest(HttpMethod.GET, List::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(1, response.body?.size)
    }

    @Test
    fun `should return single animal by id`() {
        saveDefaultAnimal()
        val response = rest(HttpMethod.GET, "/$defaultAnimalId", Animal::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(defaultAnimalId, response.body?.id)
    }

    @Test
    fun `should not found single animal by incorrect id`() {
        val response =  rest(HttpMethod.GET, "/NOT_AN_ID")
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `should return a new saved animal`() {
        val response = rest(HttpMethod.POST, defaultAnimalDto, Animal::class.java)
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
        assertEquals(defaultAnimalId, response.body?.id)
    }

    @Test
    fun `should return single animal with new information`() {
        val oldAnimal = saveDefaultAnimal()
        val oldSpecie = oldAnimal.specie
        val newAnimal = Animal(defaultAnimalId, "Roxy", "Chien", true)
        rest(HttpMethod.PUT, "/$defaultAnimalId", newAnimal, Animal::class.java)
        val response = rest(HttpMethod.GET, "/$defaultAnimalId", Animal::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals("Chien", response.body?.specie)
        assertNotEquals(oldSpecie, response.body?.specie)
    }

    @Test
    fun `should not found after deletion`() {
        saveDefaultAnimal()
        rest(HttpMethod.DELETE, "/$defaultAnimalId")
        //restTemplate.delete(getRootUrl() + "/$defaultAnimalId", defaultAnimalId)
        val responseAfterDelete = rest(HttpMethod.GET,"/$defaultAnimalId")
        assertEquals(HttpStatus.NOT_FOUND, responseAfterDelete.statusCode)
    }

    @Test
    fun `should find animals by specie`(){
        animalRepository.saveAll(animalDtos)
        val response = rest(HttpMethod.GET, "?specie=Chat", List::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(2, response.body?.size)
    }

    @Test
    fun `should not find animals by unknow specie`(){
        animalRepository.save(animalDtos[2])
        val response = rest(HttpMethod.GET, "?specie=CAMAMBERT", List::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(0, response.body?.size)
    }
}