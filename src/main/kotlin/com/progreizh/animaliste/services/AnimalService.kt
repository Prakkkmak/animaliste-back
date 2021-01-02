package com.progreizh.animaliste.services

import com.progreizh.animaliste.repositories.AnimalRepository

class AnimalService(private val animalRepository: AnimalRepository) {
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
}