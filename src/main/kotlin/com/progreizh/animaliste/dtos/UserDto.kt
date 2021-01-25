package com.progreizh.animaliste.dtos

import org.bson.types.ObjectId

data class UserDto(
    val id: String = ObjectId.get().toString(),
    val mail: String,
    var password: String,
    val name: String? = null,
    val surname: String? = null,
    val phoneNumber: String? = null,
)
