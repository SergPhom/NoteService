package ru.netology

interface CrudService<E> {
    fun add(entity: E): Boolean
    fun delete(id: Int): Boolean
    fun edit(entity: E): Boolean
    fun getList(entityIDs: List<Int>, userId: Int = 0,
             offset: Int = 0, count: Int = 20,
             sort: Int = 0): List<E>
    fun getById(id: Int, ownerId: Int = 0,
                needWiki: Boolean = false): E
    fun restore(id: Int): Boolean
}