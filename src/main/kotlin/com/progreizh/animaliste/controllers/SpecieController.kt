package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.services.AnimalService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/species")
class SpecieController(private val animalService: AnimalService) {
    /**
     * Récupère toutes les éspeces différentes.
     */
    @GetMapping
    fun getAllSpecies(): ResponseEntity<Set<String>> {
        val species = animalService.getSpecies()
        return ResponseEntity.ok(species)
    }
}