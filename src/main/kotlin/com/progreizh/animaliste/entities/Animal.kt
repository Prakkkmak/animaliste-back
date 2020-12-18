package com.progreizh.animaliste.entities

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Animal(

        /**
         * Id MongoDB de l'animal
         */
        @Id
        val id: ObjectId = ObjectId.get(),

        //region administratif
        /**
         * Numero de puce
         */
        val chip: String,
        /**
         * Numero de tatouage
         */
        val tatoo: String,
        /**
         * Numero de dossier de son ancien systeme
         */
        val fileNumber: String,
        /**
         * Date d'entrée dans le systeme
         */
        val registerDate: Date,
        //endregion

        //region physique
        /**
         * Déscription physique
         */
        val physicalDescription: String,
        /**
         * Animal est-il un mâle ?
         */
        val male: Boolean,
        /**
         * Espece / race / suspicions
         */
        val specie: String,
        //endregion

        //region psychologique
        /**
         * Déscription comportementale de l'animal
         */
        val attitudeDescription: String,
        /**
         * Ce qu'il aime.
         */
        val likes : String,
        /**
         * Ce qu'il n'aime pas
         */
        val dislikes: String,
        //endregion

        //region medical
        /**
         * Vaccins
         */
        val vaccine: String,
        /**
         * Infos pour une alimentation spéciale
         */
        val nutrition: String,
        //endregion

        //region infos
        /**
         * Nom.
         */
        val name: String,
        /**
         * Description
         */
        val description: String,
        /**
         * Origine
         */
        val origin: String,
        //endregion
        
        //TODO Historique de l'animal
)
