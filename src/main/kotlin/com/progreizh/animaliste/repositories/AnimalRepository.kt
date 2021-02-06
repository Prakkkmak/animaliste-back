package com.progreizh.animaliste.repositories

import com.progreizh.animaliste.entities.Animal
import org.springframework.data.mongodb.repository.MongoRepository

interface AnimalRepository : MongoRepository<Animal, String> {
    fun findAnimalsBySpecie(specie: String) : List<Animal>
}