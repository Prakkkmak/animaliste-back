package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.dtos.PictureStorageDto
import com.progreizh.animaliste.services.PictureStorageService
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/pictures")
class PictureController(private val pictureStorageService: PictureStorageService) {
    /**
     * Enregistre une image dans sa catégorie
     */
    @PostMapping("/{category}")
    fun uploadImage(@PathVariable("category") category: String,
                    @RequestBody pictureStorageDto: PictureStorageDto) : ResponseEntity<PictureStorageDto> {
        val newPicture = pictureStorageService.store(category, pictureStorageDto)
        return ResponseEntity(newPicture, HttpStatus.CREATED)
    }

    /**
     * Revoie le chemin d'une image en fonction de sa catégprie
     */
    @GetMapping("/{category}/{name}")
    fun getPicturePath(@PathVariable("category") category: String, @PathVariable("name") name: String)
    : ResponseEntity<Resource>{
        val picturePath = pictureStorageService.load(category, name)
        return ResponseEntity(picturePath, HttpStatus.OK)
    }
}