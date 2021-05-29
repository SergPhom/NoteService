package ru.netology

data class Comment(
    val id: Int = 0,
    val noteId: Int = 0,
    val ownerId: Int = 0,
    val date: Int = 0,
    val replyTo: Int = 0,
    val message: String = "",
    val isDeleted: Boolean = false
)