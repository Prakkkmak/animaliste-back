package com.progreizh.animaliste.repositories

import com.progreizh.animaliste.entities.Animal
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface AnimalRepository : MongoRepository<Animal, String> {
    fun findAnimalsBySpecie(specie: String) : List<Animal>
}