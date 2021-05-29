package ru.netology

object NoteService{
    private val userNotes = arrayListOf<Note>()
    private val noteComments = arrayListOf<Comment>()

    fun clear(){
        userNotes.clear()
        noteComments.clear()
    }

    fun getNotes(): List<Note>{
        return userNotes
    }
    fun getComments(): List<Comment>{
        return noteComments
    }

    fun add(
        title: String,
        text: String,
        privacyView: Array<String> = arrayOf("all"),
        privacyComment: Array<String> = arrayOf("all")
    ):Note {
        val newNote = Note(
            id = 1, title = title, text = text,
            privacyView = privacyView, privacyComment = privacyComment
        )
        userNotes += newNote.copy(
            id = if (userNotes.isEmpty()) 1
            else userNotes.last().id + 1
        )
        return newNote
    }

    fun createComment(                  //guid пропущен (не совсем понятен смысл)
        noteId: Int = 0,
        ownerId: Int = 0,
        replyTo: Int = 0,
        message: String = ""
    ): Boolean{
        for(note in userNotes){
            if(note.id == noteId){
                when {
                    !getById(noteId).canComment -> throw NoRightsToCommentException
                    note.isDeleted -> throw NoteHaveBeenDeletedYetException
                    else -> {
                        val newComment = Comment(
                            noteId = noteId,
                            ownerId = ownerId,
                            replyTo = replyTo,
                            message = message
                        )
                        noteComments += newComment.copy(
                            id = if (noteComments.isEmpty()) 1
                            else noteComments.last().id + 1
                        )
                        return true
                    }
                }
            }
        }
        throw NoteDoesnotExistExeption
    }

    fun delete(noteId: Int): Boolean{
        for((index, note) in userNotes.withIndex()){
            if(note.id == noteId){
                if(note.isDeleted) throw NoteHaveBeenDeletedYetException
                val deletedNote = note.copy(isDeleted = true)
                userNotes[index] = deletedNote
                for(comment in noteComments){
                    if(comment.noteId == noteId) deleteComment(comment.id)
                }
                return true
            }
        }
        throw NoteDoesnotExistExeption
    }

    fun deleteComment(commentId: Int): Boolean{
        for((index, comment) in noteComments.withIndex()){
            if(comment.id == commentId){
                if(comment.isDeleted) throw CommentHaveBeenDeletedYetException
                val deletedComment = comment.copy(isDeleted = true)
                noteComments[index] = deletedComment
                return true
            }
        }
        throw CommentDoesnotExistException
    }

    fun edit( noteId: Int, title: String, text: String,
        privacyView: Array<String> = arrayOf("all"),
        privacyComment: Array<String> = arrayOf("all")
    ): Boolean{
        for((index, note) in userNotes.withIndex()){
            if(note.id == noteId){
                if(note.isDeleted) throw NoteHaveBeenDeletedYetException
                val editedNote = note.copy(title = title, text = text,
                    privacyComment =  privacyComment, privacyView =  privacyView)
                userNotes[index] = editedNote
                return true
            }
        }
        throw NoteDoesnotExistExeption
    }

    fun editComment(commentId: Int, message: String,
        ownerId: Int = 0
    ): Boolean{
        for((index, comment) in noteComments.withIndex()){
            if(comment.id == commentId){
                if(comment.isDeleted) throw CommentHaveBeenDeletedYetException
                val editedComment = comment.copy(ownerId = ownerId,
                    message = message)
                noteComments[index] = editedComment
                return true
            }
        }
        throw CommentDoesnotExistException
    }

    fun get(noteIDs: List<Int>, userId: Int = 0,
            offset: Int = 0, count: Int = 20, sort: Int = 0
    ): List<Note>{
        var noteList: List<Note> = listOf()
        for (note in userNotes){
            when{
                note.isDeleted -> continue
                note.privacyView.contains("nobody") -> continue
                note.privacyView.contains("onlyMe") -> continue
                noteIDs.contains(note.id) -> noteList += note
                (note.ownerId == userId) -> noteList += note
            }
        }
        return noteList
    }

    fun getById(noteId: Int, ownerId: Int = 0,
                needWiki: Boolean = false
    ):Note{
        for(note in userNotes){
            if((noteId == note.id) and !note.isDeleted){
                return note.copy(canComment =
                when{
                    note.privacyComment.contains("nobody") -> false
                    note.privacyComment.contains("onlyMe") -> false
                    else -> true
                }
                )
            }
        }
        throw NoteDoesnotExistExeption
    }

    fun getComment(noteID: Int, ownerId: Int = 0,
                   offset: Int = 0, count: Int = 20, sort: Int = 0
    ): List<Comment>{
        val note = getById(noteID)
        var commentsList: List<Comment> = listOf()
        for (comment in noteComments){
            if((comment.noteId == noteID) and !comment.isDeleted){
                commentsList += comment
            }
        }
        return commentsList
    }

    fun restoreComment(commentId: Int): Boolean{
        for((index, comment) in noteComments.withIndex()){
            if(comment.id == commentId){
                val note = getById(comment.noteId)
                if(!note.canComment) throw NoRightsToCommentException
                if(comment.isDeleted){
                    val restoredComment = comment.copy(isDeleted = false)
                    noteComments[index] = restoredComment
                    return true
                }
                throw CommentHaveNotBeenDeletedException
            }
        }
        throw CommentDoesnotExistException
    }
}

object CommentHaveNotBeenDeletedException : RuntimeException ("Comment with this ID have not been deleted")

object CommentDoesnotExistException : RuntimeException ("Comment with such ID doesn't exist")

object CommentHaveBeenDeletedYetException : RuntimeException ("Comment with this ID have been deleted yet")

object NoteHaveBeenDeletedYetException : RuntimeException ("Note with this ID have been deleted yet")

object NoteDoesnotExistExeption :RuntimeException ("Note with such ID doesn't exist")

object NoRightsToCommentException : RuntimeException ("You can't comment thisNote")

