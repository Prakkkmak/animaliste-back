package com.progreizh.animaliste.dtos

data class UserDto(
    val id: String,
    val mail: String,
    val password: String,
    val name: String? = null,
    val surname: String? = null,
    val phoneNumber: String? = null,
)
