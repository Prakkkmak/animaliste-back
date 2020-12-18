package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.entities.Animal
import com.progreizh.animaliste.repositories.AnimalRepository
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
        val animalOptional = repository.findById(id)
        return if (animalOptional.isEmpty)
            ResponseEntity(HttpStatus.NOT_FOUND)
        else
            ResponseEntity.ok(animalOptional.get())
    }

    @PostMapping
    fun createAnimal(@RequestBody animal: Animal): ResponseEntity<Animal> {
        val newAnimal = repository.insert(animal)
        return ResponseEntity(newAnimal, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun putAnimal(@PathVariable("id") id: String, @RequestBody newAnimal: Animal): ResponseEntity<Animal> {
        val animal = repository.save(newAnimal)
        return ResponseEntity(animal, HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}")
    fun deleteAnimal(@PathVariable("id") id: String): ResponseEntity<Animal> {
        val animalOptional = repository.findById(id)
        return if (animalOptional.isEmpty)
            ResponseEntity(HttpStatus.NOT_FOUND)
        else {
            repository.delete(animalOptional.get())
            ResponseEntity.ok(animalOptional.get())
        }


    }
}