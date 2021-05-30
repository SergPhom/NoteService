package ru.netology

object CommentService: CrudService<Comment>{
    private val noteComments = arrayListOf<Comment>()

    fun clear(){
        noteComments.clear()
    }

    fun getComments(): List<Comment>{
        return noteComments
    }

    override fun add(entity: Comment): Boolean {
        for(note in NoteService.getNotes()){
            if(note.id == entity.noteId){
                when {
                    !NoteService.getById(entity.noteId).canComment -> throw NoRightsToCommentException
                    note.isDeleted -> throw NoteHaveBeenDeletedYetException
                    else -> {
                        noteComments += entity.copy(
                            id = if (noteComments.isEmpty()) 1
                            else noteComments.last().id + 1
                        )
                        return true
                    }
                }
            }
        }
        throw NoteDoesNotExistException
    }

    override fun delete(id: Int): Boolean {
        for((index, comment) in noteComments.withIndex()){
            if(comment.id == id){
                if(comment.isDeleted) throw CommentHaveBeenDeletedYetException
                val deletedComment = comment.copy(isDeleted = true)
                noteComments[index] = deletedComment
                return true
            }
        }
        throw CommentDoesNotExistException
    }

    override fun edit(entity: Comment): Boolean {          //3 tests
        for((index, comment) in noteComments.withIndex()){
            if(comment.id == entity.id){
                if(comment.isDeleted) throw CommentHaveBeenDeletedYetException
                noteComments[index] = entity
                return true
            }
        }
        throw CommentDoesNotExistException
    }

    override fun getList(entityIDs: List<Int>, userId: Int,        //2 tests
                         offset: Int, count: Int,
                         sort: Int
    ): List<Comment> {
        var commentsList: List<Comment> = listOf()
        for(noteID in entityIDs){
            for (comment in noteComments){
                if((comment.noteId == noteID) and !comment.isDeleted){
                    commentsList += comment
                }
            }
        }
        return commentsList
    }

    override fun getById(id: Int, ownerId: Int,                 //3 tests
                         needWiki: Boolean
    ): Comment {
        for(comment in noteComments){
            if(id == comment.id) {
                if (comment.isDeleted) throw CommentHaveBeenDeletedYetException
                return comment
            }
        }
        throw CommentDoesNotExistException
    }

    override fun restore(id: Int): Boolean {                     //4 tests
        for((index, comment) in noteComments.withIndex()){
            if(comment.id == id){
                val note = NoteService.getById(comment.noteId)
                if(!note.canComment) throw NoRightsToCommentException
                if(comment.isDeleted){
                    val restoredComment = comment.copy(isDeleted = false)
                    noteComments[index] = restoredComment
                    return true
                }
                throw CommentHaveNotBeenDeletedException
            }
        }
        throw CommentDoesNotExistException
    }
}

object NoRightsToCommentException : RuntimeException ("You can't comment thisNote")

object CommentHaveNotBeenDeletedException : RuntimeException ("Comment with this ID have not been deleted")

object CommentDoesNotExistException : RuntimeException ("Comment with such ID doesn't exist")

object CommentHaveBeenDeletedYetException : RuntimeException ("Comment with this ID have been deleted yet")