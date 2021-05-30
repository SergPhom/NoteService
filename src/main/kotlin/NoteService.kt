package ru.netology

object NoteService: CrudService<Note>{
    private val userNotes = arrayListOf<Note>()

    fun clear(){
        userNotes.clear()
    }

    fun getNotes(): List<Note>{
        return userNotes
    }

    override fun add(entity: Note): Boolean {
        userNotes += entity.copy(
            id = if (userNotes.isEmpty()) 1
            else userNotes.last().id + 1
        )
        return true
    }

    override fun delete(id: Int): Boolean{
        for((index, note) in userNotes.withIndex()){
            if(note.id == id){
                if(note.isDeleted) throw NoteHaveBeenDeletedYetException
                val deletedNote = note.copy(isDeleted = true)
                userNotes[index] = deletedNote
                for(comment in CommentService.getComments()){
                    if(comment.noteId == id) CommentService.delete(comment.id)
                }
                return true
            }
        }
        throw NoteDoesNotExistException
    }

    override fun edit(entity: Note           //3 tests
    ): Boolean{
        for((index, note) in userNotes.withIndex()){
            if(note.id == entity.id){
                if(note.isDeleted) throw NoteHaveBeenDeletedYetException
                userNotes[index] = entity
                return true
            }
        }
        throw NoteDoesNotExistException
    }

    override fun getList(entityIDs: List<Int>, userId: Int,        //3 tests
                         offset: Int, count: Int, sort: Int
    ): List<Note>{
        var noteList: List<Note> = listOf()
        for (note in userNotes){
            when{
                note.isDeleted -> continue
                note.privacyView.contains("nobody") -> continue
                note.privacyView.contains("onlyMe") -> continue
                entityIDs.contains(note.id) -> noteList += note
                (note.ownerId == userId) -> noteList += note
            }
        }
        return noteList
    }

    override fun getById(id: Int, ownerId: Int,            //4 tests
                         needWiki: Boolean
    ):Note{
        for(note in userNotes){
            if(id == note.id){
                if(note.isDeleted) throw NoteHaveBeenDeletedYetException
                return note.copy(canComment =
                when{
                    note.privacyComment.contains("nobody") -> false
                    note.privacyComment.contains("onlyMe") -> false
                    else -> true
                }
                )
            }
        }
        throw NoteDoesNotExistException
    }

    override fun restore(id: Int): Boolean{                      //3 tests
        for((index, note) in userNotes.withIndex()){
            if(note.id == id){
                if(!note.isDeleted) throw NoteHaveNotBeenDeletedException
                val restoredNote = note.copy(isDeleted = false)
                userNotes[index] = restoredNote
                return true
            }
        }
        throw NoteDoesNotExistException
    }
}

object NoteHaveNotBeenDeletedException : RuntimeException ("Note with this ID have not been deleted")

object NoteHaveBeenDeletedYetException : RuntimeException ("Note with this ID have been deleted yet")

object NoteDoesNotExistException :RuntimeException ("Note with such ID doesn't exist")

