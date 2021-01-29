package com.progreizh.animaliste.repositories

import com.progreizh.animaliste.entities.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface UserRepository : MongoRepository<User, String> {
    fun findUserByMail(mail: String): Optional<User>
    fun findUserByMailAndPassword(mail: String, password: String): Optional<User>
}
