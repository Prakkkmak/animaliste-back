package com.progreizh.animaliste.converters

import com.progreizh.animaliste.dtos.UserDto

abstract class Converter<S, T> {
    abstract fun convertToDto(source: S) : T
    abstract fun convertFromDto(source: T) : S
    fun convertListToDto(source: List<S>) : List<T> {
        val target : MutableList<T> = ArrayList()
        source.forEach {
            target += convertToDto(it);
        }
        return target
    }
    fun convertListFromDto(source: List<T>) : List<S>{
        val target : MutableList<S> = ArrayList()
        source.forEach {
            target += convertFromDto(it);
        }
        return target
    }
}