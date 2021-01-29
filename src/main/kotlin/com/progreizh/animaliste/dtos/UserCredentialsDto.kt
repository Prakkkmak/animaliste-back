package com.progreizh.animaliste.dtos;

import java.io.Serializable

data class UserCredentialsDto(
        val mail: String,
        val password: String,
        ) : Serializable{
                constructor() : this("", "")
        }
