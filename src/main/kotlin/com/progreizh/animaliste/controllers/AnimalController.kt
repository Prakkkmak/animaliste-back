package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.daos.DocumentSequenceDao
import com.progreizh.animaliste.entities.Animal
import com.progreizh.animaliste.repositories.AnimalRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/animals")
class AnimalController(private val repository: AnimalRepository,
                       private val documentSequenceDao: DocumentSequenceDao) {
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
        return if (!animalOptional.isPresent)
            ResponseEntity(HttpStatus.NOT_FOUND)
        else
            ResponseEntity.ok(animalOptional.get())
    }

    @RequestMapping(params = ["specie"])
    fun getAnimalsBySpecie(@RequestParam("specie") specie: String): ResponseEntity<List<Animal>> {
        val animals = repository.findAnimalsBySpecie(specie)
        return ResponseEntity.ok(animals)
    }

    /**
     * Ajoute un animal.
     */
    @PostMapping
    fun createAnimal(@RequestBody animal: Animal): ResponseEntity<Animal> {
        var animalInsert = animal
        animalInsert.id = documentSequenceDao.generateSequence("animalid").toString()
        val newAnimal = repository.insert(animal)
        return ResponseEntity(newAnimal, HttpStatus.CREATED)
    }

    /**
     * Modifie l'animal fonction de son identifiant et des nouvelles données.
     */
    @PutMapping("/{id}")
    fun putAnimal(@PathVariable("id") id: String, @RequestBody animal: Animal): ResponseEntity<Animal> {
        val newAnimal = repository.save(animal)
        return ResponseEntity(newAnimal, HttpStatus.CREATED)
    }

    /**
     * Supprime l'animal en fonction de son identifiant.
     */
    @DeleteMapping("/{id}")
    fun deleteAnimal(@PathVariable("id") id: String): ResponseEntity<Animal> {
        val animalOptional = repository.findById(id)
        return if (!animalOptional.isPresent)
            ResponseEntity(HttpStatus.NOT_FOUND)
        else {
            val animal = animalOptional.get()
            repository.delete(animal)
            ResponseEntity.ok(animal)
        }
    }
}