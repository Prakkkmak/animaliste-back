package com.progreizh.animaliste.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class DocumentSequence(
    @Id
    val id: String,
    val value: Number
)
