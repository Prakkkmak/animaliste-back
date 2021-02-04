package com.progreizh.animaliste.repositories

import com.progreizh.animaliste.daos.DocumentSequenceDao
import com.progreizh.animaliste.entities.DocumentSequence
import org.springframework.data.mongodb.core.FindAndModifyOptions.options
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.where
import org.springframework.stereotype.Repository

@Repository
class SequenceGenearatorDaoImpl(private val mongoOperations: MongoOperations) : DocumentSequenceDao {
    override fun generateSequence(name : String) : Number{
        val counter : DocumentSequence? = mongoOperations.findAndModify(
            query(where(DocumentSequence::id).`is`(name)),
            Update().inc("value", 1), options().returnNew(true).upsert(true),
            DocumentSequence::class.java
        )

        return if (counter != null)
            counter.value
        else
            1
    }
}