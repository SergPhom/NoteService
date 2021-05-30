import org.junit.Test

import org.junit.Assert.*
import ru.netology.*

class NoteAndCommentServicesTest {

    fun clear(){
        NoteService.clear()
        CommentService.clear()
    }
//add
    @Test
    fun add() {
        NoteService.add(Note(title = "Test", text = "Test text"))

        assertEquals("Test", NoteService.getNotes().last().title)
        assertEquals("Test text", NoteService.getNotes().last().text)
    }

    @Test
    fun createCommentWithTrue() {
        NoteService.add(Note())
        val result = CommentService.add(Comment(noteId = NoteService.getNotes().last().id))

        assertTrue(result)
    }

    @Test(expected = NoRightsToCommentException::class)
    fun shouldThrowNoRightsToCommentException() {
        NoteService.add(Note(privacyComment = arrayOf("nobody")))
        CommentService.add(Comment(noteId = NoteService.getNotes().last().id))
    }

    @Test(expected = NoteDoesNotExistException::class)
    fun shouldThrowNoteDoestNotExistException() {
        CommentService.add(Comment(noteId = 100))
    }

    @Test(expected = NoteHaveBeenDeletedYetException::class)
    fun shouldThrowNoteHaveBeenDeletedYetExceptionInCommentAdd() {
        NoteService.add(Note())
        NoteService.delete(id = NoteService.getNotes().last().id)
        CommentService.add(Comment(noteId = NoteService.getNotes().last().id))
    }
//delete
    @Test
    fun deleteNote() {
        clear()
        NoteService.add(Note())
        val result = NoteService.delete(id = NoteService.getNotes().last().id)

        assertTrue(result)
    }

    @Test(expected = NoteHaveBeenDeletedYetException::class)
    fun shouldThrowNoteHaveBeenDeletedYetException() {
        clear()
        NoteService.add(Note())
        NoteService.delete(id = NoteService.getNotes().last().id)
        NoteService.delete(id = NoteService.getNotes().last().id)
    }

    @Test(expected = NoteDoesNotExistException::class)
    fun shouldThrowNoteDoesNotExistException() {
        NoteService.delete(id = 100)
    }

    @Test
    fun deleteCommentProperWork() {
        NoteService.add(Note())
        CommentService.add(Comment(noteId = NoteService.getNotes().last().id))
        val result = CommentService.delete(id = CommentService.getComments().last().id)

        assertTrue(result)
    }

    @Test(expected = CommentHaveBeenDeletedYetException::class)
    fun shouldThrowCommentHaveBeenDeletedYetException() {
        NoteService.add(Note())
        CommentService.add(Comment(noteId = NoteService.getNotes().last().id))
        CommentService.delete(id = CommentService.getComments().last().id)

        CommentService.delete(id = CommentService.getComments().last().id)
    }

    @Test(expected = CommentDoesNotExistException::class)
    fun shouldThrowCommentDoesNotExistException() {
        CommentService.delete(id = 100)
    }
//edit
   @Test
    fun edit() {
        NoteService.add(Note())
        val result = NoteService.edit(Note(id = NoteService.getNotes().last().id,
            title = "newTitle", text = "newText"))

        assertTrue(result)
        assertEquals("newTitle", NoteService.getNotes().last().title)
    }

    @Test(expected = NoteHaveBeenDeletedYetException::class)
    fun shouldThrowNoteHaveBeenDeletedYetExceptionInEdit() {
        NoteService.add(Note())
        NoteService.delete(NoteService.getNotes().last().id)
        NoteService.edit(Note(id = NoteService.getNotes().last().id, title = "test",text = "test"))
    }
    @Test(expected = NoteDoesNotExistException::class)
    fun shouldThrowNoteDoesNotExistExceptionInEdit() {
        NoteService.edit(Note(id = 100, title = "test", text = "test"))
    }

    @Test
    fun editComment() {
        NoteService.add(Note())
        CommentService.add(Comment(noteId = NoteService.getNotes().last().id, message = "text"))
        val result = CommentService.edit(Comment(
            id = CommentService.getComments().last().id,
            message = "newText"))

        assertEquals("newText" , CommentService.getComments().last().message)
        assertTrue(result)
    }

    @Test(expected = CommentHaveBeenDeletedYetException::class)
    fun shouldThrowCommentHaveBeenDeletedYetExceptionInEdit() {
        NoteService.add(Note())
        CommentService.add(Comment(noteId = NoteService.getNotes().last().id))
        CommentService.delete(id = CommentService.getComments().last().id)

        CommentService.edit(Comment(id = CommentService.getComments().last().id, message = "newText"))
    }

    @Test(expected = CommentDoesNotExistException::class)
    fun shouldThrowCommentDoesNotExistExceptionInEdit() {
        CommentService.edit(Comment(100, message = "newText"))
    }
//get
    @Test
    fun getCorrectWork() {
        NoteService.add(Note())

        val result = NoteService.getList(listOf(NoteService.getNotes().last().id))

        assertFalse(result.isEmpty())
    }

    @Test
    fun getWhenIsPrivate() {
        clear()
        NoteService.add(Note( privacyView = arrayOf("nobody")))

        val result = NoteService.getList(listOf(NoteService.getNotes().last().id))

        assertTrue(result.isEmpty())
    }

    @Test
    fun getWhenIsDeleted() {
        clear()
        NoteService.add(Note())
        NoteService.delete(NoteService.getNotes().last().id)

        val result = NoteService.getList(listOf(NoteService.getNotes().last().id))

        assertTrue(result.isEmpty())
    }

    //getComment
    @Test
    fun getCommentCorrectWork() {
        clear()
        NoteService.add(Note())
        CommentService.add(Comment(noteId = NoteService.getNotes().last().id))

        val result = CommentService.getList(listOf(CommentService.getComments().last().id))

        assertFalse(result.isEmpty())
    }

    @Test
    fun getCommentWhenIsDeleted() {
        clear()
        NoteService.add(Note())
        CommentService.add(Comment(noteId = NoteService.getNotes().last().id))
        CommentService.delete(id = CommentService.getComments().last().id)

        val result = CommentService.getList(entityIDs = listOf(CommentService.getComments().last().id))

        assertTrue(result.isEmpty())
    }
//getById
    @Test
    fun getByIdWhenPrivacyComment() {
        NoteService.add(Note( privacyComment = arrayOf("nobody")))

        val result = NoteService.getById(NoteService.getNotes().last().id)

        assertFalse(result.canComment)
    }

    @Test
    fun getByIdWhenNotPrivacyComment() {
        NoteService.add(Note(privacyComment = arrayOf("all")))

        val result = NoteService.getById(NoteService.getNotes().last().id)

        assertTrue(result.canComment)
    }

    @Test(expected = NoteDoesNotExistException::class)
    fun shouldThrowNoteDoesNotExistExceptionInGetById() {
        NoteService.getById(100)
    }

    @Test(expected = NoteHaveBeenDeletedYetException::class)
    fun shouldThrowNoteHaveBeenDeletedYetExceptionInGetById() {
        NoteService.add(Note())
        NoteService.delete(id = NoteService.getNotes().last().id)

        NoteService.getById(id = NoteService.getNotes().last().id)
    }

    @Test
    fun getByIdComment() {
        NoteService.add(Note())
        CommentService.add(Comment(noteId = NoteService.getNotes().last().id,
            message = "getByIdComment test"))

        val result = CommentService.getById(CommentService.getComments().last().id)

        assertEquals("getByIdComment test", result.message)
    }

    @Test(expected = CommentHaveBeenDeletedYetException::class)
    fun shouldThrowCommentHaveBeenDeletedYetExceptionInGetById() {
        NoteService.add(Note())
        CommentService.add(Comment(noteId = NoteService.getNotes().last().id))
        CommentService.delete(id = CommentService.getComments().last().id)

        CommentService.getById(id = CommentService.getComments().last().id)
    }

    @Test(expected = CommentDoesNotExistException::class)
    fun shouldThrowCommentDoesNotExistExceptionInGetById() {
        clear()
        CommentService.getById(id = 100)
    }

//restoreComment
    @Test
    fun restoreComment() {
        clear()
        NoteService.add(Note())
        CommentService.add(Comment(noteId = NoteService.getNotes().last().id))
        CommentService.delete(CommentService.getComments().last().id)

        val result = CommentService.restore(id = CommentService.getComments().last().id)

        assertTrue(result)
    }

    @Test(expected = CommentDoesNotExistException::class)
    fun shouldThrowCommentDoesNotExistExceptionInRestoreComment() {
        clear()
        CommentService.restore(id = 100)
    }

    @Test(expected = NoRightsToCommentException::class)
    fun shouldThrowNoRightsToCommentExceptionInRestoreComment() {
        clear()
        NoteService.add(Note())
        CommentService.add(Comment(noteId = NoteService.getNotes().last().id))
        CommentService.delete(id = CommentService.getComments().last().id)
        NoteService.edit(Note(id = NoteService.getNotes().last().id, title = "text",
            text = "text", privacyComment = arrayOf("nobody")))
        CommentService.restore(CommentService.getComments().last().id)
    }

   @Test(expected = CommentHaveNotBeenDeletedException::class)
    fun shouldThrowCommentHaveNotBeenDeletedException() {
       NoteService.add(Note())
       CommentService.add(Comment(noteId = NoteService.getNotes().last().id))

       CommentService.restore(CommentService.getComments().last().id)
    }

    @Test
    fun restoreNoteCorrectWork() {
        clear()
        NoteService.add(Note(title = "Will be deleted and restored"))
        NoteService.delete(NoteService.getNotes().last().id)

        val result = NoteService.restore(NoteService.getNotes().last().id)

        assertTrue(result)
    }

    @Test(expected = NoteHaveNotBeenDeletedException::class)
    fun shouldThrowNoteHaveNotBeenDeletedException() {
        NoteService.add(Note())

        NoteService.restore(NoteService.getNotes().last().id)
    }

    @Test(expected = NoteDoesNotExistException::class)
    fun shouldThrowNoteDoesNotExistExceptionInRestore() {

        NoteService.restore(100)
    }

}