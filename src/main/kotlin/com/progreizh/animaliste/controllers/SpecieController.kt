package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.services.AnimalService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/species")
class SpecieController(private val service: AnimalService) {
    /**
     * Récupère toutes les éspeces différentes.
     */
    @GetMapping
    fun getAllSpecies(): ResponseEntity<Set<String>> {
        val species = service.getSpecies()
        return ResponseEntity.ok(species)
    }
}