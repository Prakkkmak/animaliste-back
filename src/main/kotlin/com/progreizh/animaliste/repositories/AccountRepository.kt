package com.progreizh.animaliste.repositories

import com.progreizh.animaliste.entities.Account
import org.springframework.data.mongodb.repository.MongoRepository

interface AccountRepository : MongoRepository<Account, String> {
}