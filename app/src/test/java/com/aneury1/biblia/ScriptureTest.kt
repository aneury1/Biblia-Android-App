package com.aneury1.biblia

import com.aneury1.biblia.reader.*
import org.junit.Assert.*
import org.junit.Test

class ScriptureTest {
    @Test fun corpusKeepsCanonicalStructure() {
        assertEquals(66, Scripture.books.size)
        assertEquals(Scripture.books.toSet(), Scripture.data.keys)
        assertEquals(1189, Scripture.data.values.sumOf { verses -> verses.map { it.chapter }.distinct().size })
        assertEquals(Scripture.passages.size, Scripture.passages.map { it.key }.distinct().size)
    }
    @Test fun searchMatchesEveryWordIgnoringAccentsAndCase() {
        val result = Scripture.search("PRINCIPIO creó")
        assertTrue(result.any { it.book == "Genesis" && it.verse.chapter == 1 && it.verse.verse == 1 })
        assertTrue(result.all { "principio" in normalize(it.verse.text) && "creo" in normalize(it.verse.text) })
        assertTrue(Scripture.search(" ").isEmpty())
        assertTrue(Scripture.search("zzzznonexistent").isEmpty())
    }
    @Test fun bookLabelsKeepInternalKeysSeparate() {
        assertEquals("1 Corintios", displayBook("corintios 1"))
        assertEquals("Génesis", displayBook("Genesis"))
        assertEquals("3 Juan", displayBook("juan 3"))
    }
}
