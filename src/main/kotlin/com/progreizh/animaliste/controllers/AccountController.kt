package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.entities.Account
import com.progreizh.animaliste.repositories.AccountRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
class AccountController(private val repository: AccountRepository) {
    /**
     * Récupère tous les comptes
     */
    @GetMapping
    fun getAllAccounts(): ResponseEntity<List<Account>>{
        val accounts = repository.findAll()
        return ResponseEntity.ok(accounts)
    }

    /**
     * Récupère le compte en fonction de son identifiant.
     */
    @GetMapping("/{id}")
    fun getOneAccount(@PathVariable("id") id: String): ResponseEntity<Account> {
        val accountOptional = repository.findById(id)
        return if (accountOptional.isEmpty)
            ResponseEntity(HttpStatus.NOT_FOUND)
        else
            ResponseEntity.ok(accountOptional.get())
    }

    /**
     * Ajoute un compte
     */
    @PostMapping
    fun createAccount(@RequestBody account: Account): ResponseEntity<Account> {
        val newAccount = repository.insert(account)
        return ResponseEntity(newAccount, HttpStatus.CREATED)
    }

    /**
     * Modifie le compte en fonction de son identifiant et des nouvelles données
     */
    @PutMapping("/{id}")
    fun updateAccount(@PathVariable("id") id: String, @RequestBody account: Account): ResponseEntity<Account> {
        val newAccount = repository.save(account)
        return ResponseEntity(newAccount, HttpStatus.CREATED)
    }

    /**
     * Supprime le compte en fonction de son identifiant
     */
    @DeleteMapping("/{id}")
    fun deleteAccount(@PathVariable("id") id: String): ResponseEntity<Account> {
        val accountOptional = repository.findById(id)
        return if (accountOptional.isEmpty)
            ResponseEntity(HttpStatus.NOT_FOUND)
        else {
            val account = accountOptional.get()
            repository.delete(account)
            ResponseEntity.ok(account)
        }
    }
}