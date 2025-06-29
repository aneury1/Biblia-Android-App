package com.aneury1.biblia.Data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


data class BibleNote(

    val book: String,
    val chapter: String,
    val verse: String,
    val note: String
)

fun insertNote(db: SQLiteDatabase, note: BibleNote): Long {
    val values = ContentValues().apply {
        put("book", note.book)
        put("chapter", note.chapter)
        put("verse", note.verse)
        put("note", note.note)
       /// put("created_at", note.create_at)
    }
    return db.insert("bible_notes", null, values)
}
fun getAllNotes(db: SQLiteDatabase): List<BibleNote> {
    val notes = mutableListOf<BibleNote>()

    val cursor = db.rawQuery(
        "SELECT * FROM bible_notes",
        null
    )

    cursor.use {
        while (it.moveToNext()) {
            val book = it.getColumnIndex("book")
            val chapter = it.getColumnIndex("chapter")
            val verse = it.getColumnIndex("verse")
            val note = it.getColumnIndex("note")
            notes.add(
                BibleNote(
                    ///id = it.getLong(0),
                    book = it.getString(book),
                    chapter = it.getString(chapter),
                    verse = it.getString(verse) ,
                    note = it.getString(note),
                    //created_at = 0/// it.getLong(5)
                )
            )
        }
    }
    return notes
}
class BibleSqlite3(context: Context) :
    SQLiteOpenHelper(context,
        "bible_notes_v1.db",
        null,
        1){

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("""
            CREATE TABLE if not exists bible_notes(
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                book TEXT NOT NULL,
                chapter TEXT NOT NULL,
                verse TEXT NOT NULL,
                note TEXT NOT NULL,
                created_at INTEGER
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}