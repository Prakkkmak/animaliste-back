package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.dtos.PictureDto
import com.progreizh.animaliste.services.PictureStorageService
import org.springframework.core.io.Resource
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/pictures")
class PictureController(private val pictureStorageService: PictureStorageService) {
    /**
     * Enregistre une image dans sa catégorie
     */
    @PostMapping("/{category}")
    fun uploadImage(@PathVariable("category") category: String,
                    @RequestBody picture: PictureDto) : PictureDto {
        return pictureStorageService.save(category, picture)
    }

    /**
     * Revoie le chemin d'une image en fonction de sa catégprie
     */
    @GetMapping("/{category}/{name}")
    fun getPicturePath(@PathVariable("category") category: String, @PathVariable("name") name: String)
    : Resource{
        return pictureStorageService.load(category, name)
    }
}