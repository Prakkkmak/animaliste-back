package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.entities.Animal
import com.progreizh.animaliste.services.AnimalService
import com.progreizh.animaliste.services.SequenceGenearatorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/animals")
class AnimalController(private val animalService: AnimalService,
                       private val sequenceGenearatorService: SequenceGenearatorService) {
    /**
     * Récupère tous les animaux dans une ResponseEntity.
     */
    @GetMapping
    fun getAllAnimals(): ResponseEntity<List<Animal>> {
        val animals = animalService.findAll()
        return ResponseEntity.ok(animals)
    }

    /**
     * Récupère l'animal en fonction de son identifiant.
     */
    @GetMapping("/{id}")
    fun getOneAnimals(@PathVariable("id") id: String): ResponseEntity<Animal> {
        val animalOptional = animalService.findById(id)
        return if (!animalOptional.isPresent)
            ResponseEntity(HttpStatus.NOT_FOUND)
        else
            ResponseEntity.ok(animalOptional.get())
    }

    @RequestMapping(params = ["specie"])
    fun getAnimalsBySpecie(@RequestParam("specie") specie: String): ResponseEntity<List<Animal>> {
        val animals = animalService.findAnimalsBySpecie(specie)
        return ResponseEntity.ok(animals)
    }

    /**
     * Ajoute un animal.
     */
    @PostMapping
    fun createAnimal(@RequestBody animalDto: Animal): ResponseEntity<Animal> {
        var animalInsert = animalDto
        animalInsert.sequencedId = sequenceGenearatorService.generateSequence("animalid")
        val newAnimal = animalService.create(animalDto)
        return ResponseEntity(newAnimal, HttpStatus.CREATED)
    }

    /**
     * Modifie l'animal fonction de son identifiant et des nouvelles données.
     */
    @PutMapping("/{id}")
    fun putAnimal(@PathVariable("id") id: String, @RequestBody animalDto: Animal): ResponseEntity<Animal> {
        val newAnimal = animalService.update(animalDto)
        return ResponseEntity(newAnimal, HttpStatus.CREATED)
    }

    /**
     * Supprime l'animal en fonction de son identifiant.
     */
    @DeleteMapping("/{id}")
    fun deleteAnimal(@PathVariable("id") id: String): ResponseEntity<Animal> {
        val animalOptional = animalService.findById(id)
        return if (!animalOptional.isPresent)
            ResponseEntity(HttpStatus.NOT_FOUND)
        else {
            val animal = animalOptional.get()
            animalService.delete(animal)
            ResponseEntity.ok(animal)
        }
    }
}