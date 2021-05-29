import org.junit.Test

import org.junit.Assert.*
import ru.netology.*

class NoteServiceTest {
//add
    @Test
    fun add() {
        NoteService.add("Test", "Test text")

        assertEquals("Test", NoteService.getNotes().last().title)
        assertEquals("Test text", NoteService.getNotes().last().text)
    }
//createComment
    @Test
    fun createCommentWithTrue() {
        NoteService.add("Test","Test text")
        val result = NoteService.createComment(noteId = NoteService.getNotes().last().id)

        assertTrue(result)
    }

    @Test(expected = NoRightsToCommentException::class)
    fun shouldThrowNoRightsToCommentException() {
        NoteService.add("Test","Test text", privacyComment = arrayOf("nobody"))
        NoteService.createComment(noteId = NoteService.getNotes().last().id)
    }

    @Test(expected = NoteDoesnotExistExeption::class)
    fun shouldThrowNoteDoestnotExistException() {
        NoteService.createComment(noteId = 100)
    }
//delete
    @Test
    fun delete() {
        NoteService.add("Test", "Test text")
        val result = NoteService.delete(noteId = NoteService.getNotes().last().id)

        assertTrue(result)
    }

    @Test(expected = NoteHaveBeenDeletedYetException::class)
    fun shouldThrowNoteHaveBeenDeletedYetException() {
        NoteService.add("Test", "Test text")
        NoteService.delete(noteId = NoteService.getNotes().last().id)
        NoteService.delete(noteId = NoteService.getNotes().last().id)
    }

    @Test(expected = NoteDoesnotExistExeption::class)
    fun shouldThrowNoteDoesnotExistExeption() {
        NoteService.delete(noteId = 100)
    }
//deleteComment
    @Test
    fun deleteCommentProperWork() {
        NoteService.add("Test", "Test text")
        NoteService.createComment(noteId = NoteService.getNotes().last().id)
        val result = NoteService.deleteComment(commentId = NoteService.getComments().last().id)

        assertTrue(result)
    }

    @Test(expected = CommentHaveBeenDeletedYetException::class)
    fun shouldThrowCommentHaveBeenDeletedYetException() {
        NoteService.add("Test", "Test text")
        NoteService.createComment(noteId = NoteService.getNotes().last().id)
        NoteService.deleteComment(commentId = NoteService.getComments().last().id)
        NoteService.deleteComment(commentId = NoteService.getComments().last().id)
    }

    @Test(expected = CommentDoesnotExistException::class)
    fun shouldThrowCommentDoesnotExistException() {
        NoteService.add("Test", "Test text")
        NoteService.createComment(noteId = NoteService.getNotes().last().id)
        NoteService.deleteComment(commentId = 100)
    }
//edit
    @Test
    fun edit() {
        NoteService.add("Test", "Test text")
        val result = NoteService.edit(noteId = NoteService.getNotes().last().id,
            title = "newTitle", text = "newText")

        assertTrue(result)
        assertEquals("newTitle", NoteService.getNotes().last().title)
    }

    @Test(expected = NoteHaveBeenDeletedYetException::class)
    fun shouldThrowNoteHaveBeenDeletedYetExceptionInEdit() {
        NoteService.add("Test", "Test text")
        NoteService.delete(NoteService.getNotes().last().id)
        NoteService.edit(NoteService.getNotes().last().id, "test","test")
    }
    @Test(expected = NoteDoesnotExistExeption::class)
    fun shouldThrowNoteDoesnotExistExeptionInEdit() {
        NoteService.edit(100, "test","test")
    }
//editComment
    @Test
    fun editComment() {
        NoteService.add("Test", "Test text")
        NoteService.createComment(noteId = NoteService.getNotes().last().id, message = "text")
        val result = NoteService.editComment(
            commentId = NoteService.getComments().last().id,
            message = "newText")

        assertEquals("newText" , NoteService.getComments().last().message)
        assertTrue(result)
    }

    @Test(expected = CommentHaveBeenDeletedYetException::class)
    fun shouldThrowCommentHaveBeenDeletedYetExceptionInEdit() {
        NoteService.add("Test", "Test text")
        NoteService.createComment(noteId = NoteService.getNotes().last().id)
        NoteService.deleteComment(NoteService.getComments().last().id)
        NoteService.editComment(NoteService.getComments().last().id, "newText")
    }

    @Test(expected = CommentDoesnotExistException::class)
    fun shouldThrowCommentDoesnotExistExceptionInEdit() {
        NoteService.editComment(100, "newText")
    }
//get
    @Test
    fun getCorrectWork() {
        NoteService.add("test", "test")

        val result = NoteService.get(listOf(NoteService.getNotes().last().id))

        assertFalse(result.isEmpty())
    }

    @Test
    fun getWhenIsPrivate() {
        NoteService.clear()
        NoteService.add("test", "test", privacyView = arrayOf("nobody"))

        val result = NoteService.get(listOf(NoteService.getNotes().last().id))

        assertTrue(result.isEmpty())
    }

    @Test
    fun getWhenIsDeleted() {
        NoteService.clear()
        NoteService.add("test", "test")
        NoteService.delete(NoteService.getNotes().last().id)

        val result = NoteService.get(listOf(NoteService.getNotes().last().id))

        assertTrue(result.isEmpty())
    }
//getById
    @Test
    fun getByIdWhenPrivacyComment() {
        NoteService.add("test", "test", privacyComment = arrayOf("nobody"))

        val result = NoteService.getById(NoteService.getNotes().last().id)

        assertFalse(result.canComment)
    }

    @Test
    fun getByIdWhenNotPrivacyComment() {
        NoteService.add("test", "test", privacyComment = arrayOf("all"))

        val result = NoteService.getById(NoteService.getNotes().last().id)

        assertTrue(result.canComment)
    }

    @Test(expected = NoteDoesnotExistExeption::class)
    fun shouldThrowNoteDoesnotExistExeptionInGetById() {
        NoteService.getById(100)
    }
//getComment
    @Test
    fun getCommentCorrectWork() {
         NoteService.add("Test", "Test text")
         NoteService.createComment(noteId = NoteService.getNotes().last().id)

         val result = NoteService.getComment(NoteService.getNotes().last().id)

         assertFalse(result.isEmpty())
    }

    @Test(expected = NoteDoesnotExistExeption::class)
    fun shouldThrowNoteDoesnotExistExceptionInGetComment() {
        NoteService.clear()
        NoteService.add("Test", "Test text")
        NoteService.delete(NoteService.getNotes().last().id)
        NoteService.getComment(NoteService.getNotes().last().id)
    }
//restoreComment
    @Test
    fun restoreComment() {
        NoteService.clear()
        NoteService.add("Test", "Test text")
        NoteService.createComment(noteId = NoteService.getNotes().last().id)
        NoteService.deleteComment(NoteService.getComments().last().id)

        val result = NoteService.restoreComment(NoteService.getComments().last().id)

        assertTrue(result)
    }

    @Test(expected = NoteDoesnotExistExeption::class)
    fun shouldThrowNoteDoesnotExistExceptionInRestoreComment() {
        NoteService.clear()
        NoteService.add("Test", "Test text")
        NoteService.createComment(noteId = NoteService.getNotes().last().id)
        NoteService.delete(NoteService.getNotes().last().id)
        NoteService.restoreComment(NoteService.getComments().last().id)
    }

    @Test(expected = NoRightsToCommentException::class)
    fun shouldThrowNoRightsToCommentExceptionInRestoreComment() {
        NoteService.clear()
        NoteService.add("Test", "Test text")
        NoteService.createComment(noteId = NoteService.getNotes().last().id)
        NoteService.deleteComment(NoteService.getComments().last().id)
        NoteService.edit(NoteService.getNotes().last().id, title = "text",
            text = "text", privacyComment = arrayOf("nobody"))
        NoteService.restoreComment(NoteService.getComments().last().id)
    }

    @Test(expected = CommentHaveNotBeenDeletedException::class)
    fun shouldThrowCommentHaveNotBeenDeletedException() {
        NoteService.clear()
        NoteService.add("Test", "Test text")
        NoteService.createComment(noteId = NoteService.getNotes().last().id)

        NoteService.restoreComment(NoteService.getComments().last().id)
    }

    @Test(expected = CommentDoesnotExistException::class)
    fun shouldThrowCommentDoesnotExistExceptionInRestoreComment() {
        NoteService.clear()
        NoteService.add("Test", "Test text")
        NoteService.restoreComment(10)
    }
}