package com.progreizh.animaliste.repositories

import com.progreizh.animaliste.entities.Animal
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface AnimalRepository : MongoRepository<Animal, String> {
    fun findOneById(id: ObjectId): Animal
}