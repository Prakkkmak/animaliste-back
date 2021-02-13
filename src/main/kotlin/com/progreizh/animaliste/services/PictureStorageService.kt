package com.progreizh.animaliste.services

import com.progreizh.animaliste.dtos.PictureStorageDto
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
    fun store(category: String, pictureStorageDto: PictureStorageDto) : PictureStorageDto{
        val categoryPath : Path = Paths.get(category)
        val pictureNamePath : Path = Paths.get(pictureStorageDto::name.get())
        if(!Files.exists(categoryPath)) Files.createDirectory(categoryPath)
        if(!Files.exists(pictureNamePath)) Files.createDirectory(pictureNamePath)
        Files.copy(pictureStorageDto::file.get().inputStream, pictureNamePath)
        return pictureStorageDto
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