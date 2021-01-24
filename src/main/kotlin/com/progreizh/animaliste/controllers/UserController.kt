package com.progreizh.animaliste.controllers

import com.progreizh.animaliste.dtos.UserCredentialsDto
import com.progreizh.animaliste.dtos.UserDto
import com.progreizh.animaliste.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.Optional

@RestController
@RequestMapping("/accounts")
class UserController(private val service: UserService) {
    /**
     * Récupère tous les comptes.
     */
    @GetMapping
    fun getAccounts(): ResponseEntity<List<UserDto>>{
        val accounts = service.findAll()
        return ResponseEntity.ok(accounts)
    }

    /**
     * Récupère le compte en fonction de son identifiant.
     */
    @GetMapping("/{id}")
    fun getAccount(@PathVariable("id") id: String): ResponseEntity<UserDto> {
        return ResponseEntity.ok(service.findById(id))
    }

    /**
     * Retourne le compte correspondant au couple mail/mot de passe ssi celui-ci existe ou un message d'erreur.
     */
    @PostMapping("/login")
    fun getAccountByMailAndPassword(@RequestBody userCredentialsDto: UserCredentialsDto): ResponseEntity<UserDto> {
        return ResponseEntity.ok(service.findUserByMailAndPassword(userCredentialsDto.mail, userCredentialsDto.password))
    }

    /**
     * Ajoute un compte.
     */
    @PostMapping
    fun createAccount(@RequestBody userDto: UserDto): ResponseEntity<UserDto> {
        return ResponseEntity.ok(service.create(userDto))
    }

    /**
     * Modifie le compte en fonction de son identifiant et des nouvelles données.
     */
    @PutMapping("/{id}")
    fun updateAccount(@PathVariable("id") id: String, @RequestBody userDto: UserDto): ResponseEntity<UserDto> {
        return ResponseEntity.ok(service.update(userDto))
    }

    /**
     * Supprime le compte en fonction de son identifiant.
     */
    @DeleteMapping("/{id}")
    fun deleteAccount(@PathVariable("id") id: String): ResponseEntity<UserDto> {
        return ResponseEntity.ok(service.delete(id))
    }
}
