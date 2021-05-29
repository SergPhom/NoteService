package ru.netology

fun main() {
    NoteService.add("Test", "Test text")
    NoteService.createComment(noteId = NoteService.getNotes().last().id)
    println("${NoteService.getNotes().last().id} ${NoteService.getComments().last().noteId} " +
            "${NoteService.getComments().last().id}")
    NoteService.deleteComment(commentId = NoteService.getComments().last().id)
}