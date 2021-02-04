package com.progreizh.animaliste.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document
data class Animal(
        @Id
        var id: String,
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
        )
