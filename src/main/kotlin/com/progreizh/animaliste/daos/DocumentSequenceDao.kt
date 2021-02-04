package com.progreizh.animaliste.daos

interface DocumentSequenceDao {
    fun generateSequence(name : String) : Number
}