package com.aneury1.biblia

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.aneury1.biblia.Data.BibleSqlite3
import com.aneury1.biblia.Data.BibleNote
import com.aneury1.biblia.Data.insertNote
import com.aneury1.biblia.reader.NoteRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotePersistenceTest {
    @Test fun existingNoteCanBeLoadedEditedAndDeletedByItsId() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val helper = BibleSqlite3(context)
        val id = insertNote(helper.writableDatabase, BibleNote("Genesis", "1", "1", "Legacy note fixture"))
        helper.close()
        val repo = NoteRepository(context)
        try {
            val original = repo.all().single { it.id == id }
            assertEquals("Legacy note fixture", original.text)
            repo.save(original.copy(text = "Updated reflection"))
            assertEquals("Updated reflection", repo.all().single { it.id == id }.text)
            repo.delete(id)
            assertFalse(repo.all().any { it.id == id })
        } finally { repo.delete(id); repo.close() }
    }
}
