package com.progreizh.animaliste.services

import com.progreizh.animaliste.converters.UserConverter
import com.progreizh.animaliste.dtos.UserDto
import com.progreizh.animaliste.entities.User
import com.progreizh.animaliste.repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.io.Serializable
import java.lang.RuntimeException
import java.util.*

@Service
class UserService(val repository: UserRepository, val converter: UserConverter) {

    fun findAll() : List<UserDto> {
        return converter.convertListToDto(repository.findAll())
    }

    fun findUserByMail (mail : String) : UserDto{
        val userOptional = repository.findUserByMail(mail)
        if(!userOptional.isPresent){
            throw RuntimeException()
        }
        else {
            return converter.convertToDto(userOptional.get())
        }
    }

    fun findById(id: String): UserDto {
        val userOptional = repository.findById(id)
        if (!userOptional.isPresent)
            throw RuntimeException()
        else
           return converter.convertToDto(userOptional.get())
    }

    fun findUserByMailAndPassword(mail: String, password: String): UserDto {
        val userOptional: Optional<User> = repository.findUserByMailAndPassword(mail, password)
        if(!userOptional.isPresent)
            throw RuntimeException()
        else
            return converter.convertToDto(userOptional.get())
    }

    fun create(userDto: UserDto): UserDto {
        val newUser = repository.insert(converter.convertFromDto(userDto))
        return converter.convertToDto(newUser)
    }

    fun update(userDto: UserDto): UserDto {
        val newUser = repository.save(converter.convertFromDto(userDto))
        return converter.convertToDto(newUser)
    }

    fun delete(id: String): UserDto {
        val userOptional = repository.findById(id)
        if (!userOptional.isPresent)
            throw RuntimeException()
        else {
            val account = userOptional.get()
            repository.delete(account)
            return converter.convertToDto(userOptional.get())
        }
    }
}