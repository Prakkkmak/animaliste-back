package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.dtos.AnimalDto
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
    fun getAllAnimals(): ResponseEntity<List<AnimalDto>> {
        val animals = animalService.findAll()
        return ResponseEntity.ok(animals)
    }

    /**
     * Récupère l'animal en fonction de son identifiant.
     */
    @GetMapping("/{id}")
    fun getOneAnimals(@PathVariable("id") id: String): ResponseEntity<AnimalDto> {
        return ResponseEntity.ok(animalService.findById(id))
    }

    @RequestMapping(params = ["specie"])
    fun getAnimalsBySpecie(@RequestParam("specie") specie: String): ResponseEntity<List<AnimalDto>> {
        val animals = animalService.findAnimalsBySpecie(specie)
        return ResponseEntity.ok(animals)
    }

    /**
     * Ajoute un animal.
     */
    @PostMapping
    fun createAnimal(@RequestBody animalDto: AnimalDto): ResponseEntity<AnimalDto> {
        var animalInsert = animalDto
        animalInsert.sequencedId = sequenceGenearatorService.generateSequence("animalid")
        val newAnimal = animalService.create(animalDto)
        return ResponseEntity(newAnimal, HttpStatus.CREATED)
    }

    /**
     * Modifie l'animal fonction de son identifiant et des nouvelles données.
     */
    @PutMapping("/{id}")
    fun putAnimal(@PathVariable("id") id: String, @RequestBody animalDto: AnimalDto): ResponseEntity<AnimalDto> {
        val newAnimal = animalService.update(animalDto)
        return ResponseEntity(newAnimal, HttpStatus.CREATED)
    }

    /**
     * Supprime l'animal en fonction de son identifiant.
     */
    @DeleteMapping("/{id}")
    fun deleteAnimal(@PathVariable("id") id: String): ResponseEntity<AnimalDto> {
        return ResponseEntity.ok(animalService.delete(id))
    }
}