package com.progreizh.animaliste.dtos

import org.bson.types.ObjectId
import java.util.Date

data class AnimalDto(
    val id: String = ObjectId.get().toString(),
    val name: String,
    val specie: String,
    val sex: Boolean,
    val registerDate: Date = Date(),
    val race: String? = null,
    val chip: String? = null,
    val tattoo: String? = null,
    val description: String? = null,
    val likes : String? = null,
    val dislikes: String? = null,
    val vaccines: String? = null,
    val nutrition: String? = null,
    val origin: String? = null,
    var sequencedId: Number? = null
)
