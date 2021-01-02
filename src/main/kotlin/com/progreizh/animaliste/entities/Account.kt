package com.progreizh.animaliste.entities

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import javax.annotation.processing.Generated

@Document
data class Account(
    @Id
    @Generated
    val id: String = ObjectId.get().toString(),
    val name: String,
    val surname: String,
    val contact: String,
    val legalStatusName: String?,
    val registerDate: Date = Date(),

    )
