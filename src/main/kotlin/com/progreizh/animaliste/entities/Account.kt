package com.progreizh.animaliste.entities

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date
import javax.annotation.Generated

@Document
data class Account(
    @Id
    @Generated
    val id: String = ObjectId.get().toString(),
    val mail: String,
    val password: String,
    val name: String,
    val surname: String? = null,
    val phoneNumber: String? = null,
    val legalStatusName: String? = null,
    val registerDate: Date = Date(),
    )
