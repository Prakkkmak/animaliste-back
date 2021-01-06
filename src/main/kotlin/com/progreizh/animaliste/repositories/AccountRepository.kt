package com.progreizh.animaliste.repositories

import com.progreizh.animaliste.entities.Account
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface AccountRepository : MongoRepository<Account, String> {
    fun findAccountByMail(mail: String): Optional<Account>
    fun findAccountByMailEqualsAndPassword(mail: String, password: String): Optional<Account>
}
