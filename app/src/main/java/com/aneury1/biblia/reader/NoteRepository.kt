package com.aneury1.biblia.reader

import android.content.ContentValues
import android.content.Context
import com.aneury1.biblia.Data.BibleSqlite3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class SavedNote(val id: Long, val book: String, val chapter: String, val verse: String, val text: String)
class NoteRepository(context: Context) {
    private val helper = BibleSqlite3(context.applicationContext)
    suspend fun all(): List<SavedNote> = withContext(Dispatchers.IO) {
        helper.readableDatabase.rawQuery("SELECT ID, book, chapter, verse, note FROM bible_notes ORDER BY ID DESC", null).use { cursor ->
            buildList {
                while (cursor.moveToNext()) add(SavedNote(cursor.getLong(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4)))
            }
        }
    }
    suspend fun save(note: SavedNote) = withContext(Dispatchers.IO) {
        val values = ContentValues().apply {
            put("book", note.book); put("chapter", note.chapter); put("verse", note.verse); put("note", note.text.trim())
        }
        val db = helper.writableDatabase
        if (note.id == 0L) check(db.insert("bible_notes", null, values) != -1L)
        else check(db.update("bible_notes", values, "ID = ?", arrayOf(note.id.toString())) == 1)
    }
    suspend fun delete(id: Long) = withContext(Dispatchers.IO) {
        helper.writableDatabase.delete("bible_notes", "ID = ?", arrayOf(id.toString()))
    }
    fun close() = helper.close()
}
