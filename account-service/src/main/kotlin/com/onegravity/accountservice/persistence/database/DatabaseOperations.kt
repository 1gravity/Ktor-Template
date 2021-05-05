package com.onegravity.accountservice.persistence.database

interface DatabaseOperations<ENTITY> {

    fun getAll(): List<ENTITY>

    fun get(uuid: String): ENTITY?

    fun upsert(entity: ENTITY): ENTITY

    fun delete(uuid: String): ENTITY?

}