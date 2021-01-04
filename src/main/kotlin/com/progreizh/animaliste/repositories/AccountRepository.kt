package com.progreizh.animaliste.repositories

import com.progreizh.animaliste.entities.Account
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface AccountRepository : MongoRepository<Account, String> {
    fun findAccountByMailEquals(mail: String): Optional<Account>
    fun findAccountByMailEqualsAndPasswordEquals(mail: String, password: String): Optional<Account>
}