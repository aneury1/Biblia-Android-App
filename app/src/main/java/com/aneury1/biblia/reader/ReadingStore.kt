package com.aneury1.biblia.reader

import android.content.Context
import com.aneury1.biblia.Data.BibleFunction.BibleData
import com.aneury1.biblia.Data.BibleVerse
import java.text.Normalizer
import java.util.Locale

fun normalize(text: String): String = Normalizer.normalize(text, Normalizer.Form.NFD)
    .replace("\\p{M}+".toRegex(), "").lowercase(Locale.ROOT).trim()
fun displayBook(name: String): String {
    val numbered = "(.+) ([123])".toRegex().matchEntire(name)
    val base = numbered?.groupValues?.get(1) ?: name
    val accents = mapOf("Genesis" to "Génesis", "Exodo" to "Éxodo", "Levitico" to "Levítico",
        "Numeros" to "Números", "Josue" to "Josué", "Isaias" to "Isaías", "Malaquias" to "Malaquías",
        "Sofonias" to "Sofonías", "Galatas" to "Gálatas", "Abdias" to "Abdías", "Miqueas" to "Miqueas",
        "Jonas" to "Jonás", "Filemon" to "Filemón")
    val title = accents[base] ?: base.replaceFirstChar { it.titlecase(Locale.ROOT) }
    return if (numbered == null) title else "${numbered.groupValues[2]} $title"
}
data class Passage(val book: String, val verse: BibleVerse) {
    val key get() = "$book|${verse.chapter}|${verse.verse}"
    val reference get() = "${displayBook(book)} ${verse.chapter}:${verse.verse}"
}
object Scripture {
    val books by lazy { BibleData.getBooksNames().take(66) }
    val data by lazy { BibleData.getWholeBook() }
    val passages by lazy { books.flatMap { book -> data[book].orEmpty().map { Passage(book, it) } } }
    val index by lazy { passages.map { it to normalize(it.verse.text) } }
    fun search(query: String): List<Passage> {
        val terms = normalize(query).split(" ").filter { it.isNotEmpty() }
        if (terms.isEmpty()) return emptyList()
        return index.asSequence().filter { (_, text) -> terms.all { it in text } }.map { it.first }.toList()
    }
}
class ReadingStore(context: Context) {
    private val prefs = context.getSharedPreferences("reading_v2", Context.MODE_PRIVATE)
    var book: String
        get() = prefs.getString("book", "Genesis")!!.takeIf { it in Scripture.books } ?: "Genesis"
        set(value) { prefs.edit().putString("book", value).apply() }
    var chapter: Int
        get() = prefs.getInt("chapter", 1)
        set(value) { prefs.edit().putInt("chapter", value).apply() }
    var theme: String
        get() = prefs.getString("theme", "system")!!
        set(value) { prefs.edit().putString("theme", value).apply() }
    var fontSize: Float
        get() = prefs.getFloat("font", 20f).coerceIn(16f, 30f)
        set(value) { prefs.edit().putFloat("font", value.coerceIn(16f, 30f)).apply() }
    fun favorites(): Set<String> = prefs.getStringSet("favorites", emptySet())!!.toSet()
    fun toggle(key: String) {
        val values = favorites().toMutableSet()
        if (!values.add(key)) values.remove(key)
        prefs.edit().putStringSet("favorites", values).apply()
    }
    fun completed(): Set<String> = prefs.getStringSet("completed", emptySet())!!.toSet()
    fun toggleChapter(book: String, chapter: Int) {
        val values = completed().toMutableSet()
        val key = "$book|$chapter"
        if (!values.add(key)) values.remove(key)
        prefs.edit().putStringSet("completed", values).apply()
    }
}
