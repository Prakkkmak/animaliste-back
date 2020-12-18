package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.entities.Animal
import com.progreizh.animaliste.repositories.AnimalRepository
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/animals")
class AnimalController(private val repository: AnimalRepository) {
    /**
     * Récupère tous les animaux.
     */
    @GetMapping
    fun getAllAnimals(): ResponseEntity<List<Animal>> {
        val animals = repository.findAll()
        return ResponseEntity.ok(animals)
    }

    /**
     * Récupère l'animal en fonction de son identifiant.
     */
    @GetMapping("/{id}")
    fun getOneAnimals(@PathVariable("id") id: String): ResponseEntity<Animal> {
        val animal = repository.findOneById(ObjectId(id))
        return ResponseEntity.ok(animal)
    }

    @PostMapping
    fun createAnimal(@RequestBody animal: Animal) : ResponseEntity<Animal> {
        val animal = repository.save(animal)
        return ResponseEntity(animal, HttpStatus.CREATED)
    }
}