package com.progreizh.animaliste.converters

import com.progreizh.animaliste.dtos.AnimalDto
import org.springframework.stereotype.Component

@Component
class AnimalConverter : Converter<com.progreizh.animaliste.entities.Animal, AnimalDto>() {
    override fun convertToDto(source: com.progreizh.animaliste.entities.Animal): AnimalDto {
        return AnimalDto(
            source.id,
            source.name,
            source.specie,
            source.sex,
            source.registerDate,
            source.race,
            source.chip,
            source.tattoo,
            source.description,
            source.likes,
            source.dislikes,
            source.vaccines,
            source.nutrition,
            source.origin,
            source.sequencedId,
        )
    }

    override fun convertFromDto(source: AnimalDto): com.progreizh.animaliste.entities.Animal {
        return com.progreizh.animaliste.entities.Animal(
            source.id,
            source.name,
            source.specie,
            source.sex,
            source.registerDate,
            source.race,
            source.chip,
            source.tattoo,
            source.description,
            source.likes,
            source.dislikes,
            source.vaccines,
            source.nutrition,
            source.origin,
            source.sequencedId
        )
    }
}