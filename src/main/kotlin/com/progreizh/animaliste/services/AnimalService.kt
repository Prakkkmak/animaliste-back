package com.progreizh.animaliste.services

import com.progreizh.animaliste.converters.AnimalConverter
import com.progreizh.animaliste.dtos.AnimalDto
import com.progreizh.animaliste.exceptions.ResourceNotFoundException
import com.progreizh.animaliste.repositories.AnimalRepository
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import java.lang.RuntimeException

@Service
class AnimalService(private val animalRepository: AnimalRepository, val converter: AnimalConverter) {
    /**
     * Récupère les éspèces différentes des animaux.
     */
    fun getSpecies() : Set<String>{
        val animals = animalRepository.findAll()
        val species = HashSet<String>()
        animals.forEach {
            if(!species.contains(it.specie)){
                species.add(it.specie);
            }
        }
        return species;
    }

    /**
     * Récupère tous les animaux
     */
    fun findAll() : List<AnimalDto>{
        return converter.convertListToDto(animalRepository.findAll());
    }

    /**
     * Récupère un animal en fonction de son ID
     */
    fun findById(id : String) : AnimalDto {
        val animalOptional =  animalRepository.findById(id)
        if (!animalOptional.isPresent)
            throw ResourceNotFoundException()
        else
            return converter.convertToDto(animalOptional.get())
    }

    /**
     * Récupère tous les animaux correspondants à une espèce
     */
    fun findAnimalsBySpecie(specie : String) : List<AnimalDto> {
        return converter.convertListToDto(animalRepository.findAnimalsBySpecie(specie))
    }

    /**
     * Ajoute un nouvel animal
     */
    fun create(animalDto: AnimalDto) : AnimalDto {
        val newAnimal = animalRepository.insert(converter.convertFromDto(animalDto))
        return converter.convertToDto(newAnimal)
    }

    /**
     * Modifie un animal ou l'ajoute si non existant
     */
    fun update(animalDto: AnimalDto) : AnimalDto {
        val newAnimal = animalRepository.save(converter.convertFromDto(animalDto))
        return converter.convertToDto(newAnimal)
    }

    /**
     * Supprime un animal en fonction de son ID
     */
    fun delete(id : String) : AnimalDto {
        val animalOptional = animalRepository.findById(id)
        if (!animalOptional.isPresent)
            throw RuntimeException()
        else {
            val animal = animalOptional.get()
            animalRepository.delete(animal)
            return converter.convertToDto(animal)
        }
    }
}