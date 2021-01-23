package com.progreizh.animaliste.converters

import com.progreizh.animaliste.dtos.UserDto
import com.progreizh.animaliste.entities.User
import org.springframework.stereotype.Component

@Component
class UserConverter : Converter<User, UserDto>() {
    override fun convertToDto(source: User): UserDto {
        return UserDto(
            source.id,
            source.mail,
            source.password,
            source.name,
            source.surname,
            source.phoneNumber
        )
    }

    override fun convertFromDto(source: UserDto): User {
        return User(
            source.id,
            source.mail,
            source.password,
            source.name,
            source.surname,
            source.phoneNumber
        )
    }

}