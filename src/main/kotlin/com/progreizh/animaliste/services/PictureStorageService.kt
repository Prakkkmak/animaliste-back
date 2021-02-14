package com.progreizh.animaliste.services

import com.progreizh.animaliste.dtos.PictureDto
import com.progreizh.animaliste.exceptions.ResourceNotFoundException
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service
class PictureStorageService {
    /**
     * Enregistre une image dans un répertoire 'category'
     */
    fun save(category: String, picture: PictureDto) : PictureDto{
        val categoryPath : Path = Paths.get(category)
        val pictureNamePath : Path = Paths.get(picture.name)
        if(!Files.exists(categoryPath)) Files.createDirectory(categoryPath)
        if(!Files.exists(pictureNamePath)) Files.createDirectory(pictureNamePath)
        val picturePath : Path = Paths.get(picture.name + "/1.png")
        Files.copy(picture.file.inputStream, picturePath)
        return picture
    }

    /**
     * Récupère le chemin vers une image en fonction de sa catégorie et de son nom
     */
    fun load(category: String, name: String) : Resource {
        val categoryPath: Path = Paths.get(category)
        val picturePath: Path = categoryPath.resolve(name)
        if(!Files.exists(categoryPath) || !Files.exists(picturePath)) throw ResourceNotFoundException()
        return UrlResource(picturePath.toUri())
    }
}