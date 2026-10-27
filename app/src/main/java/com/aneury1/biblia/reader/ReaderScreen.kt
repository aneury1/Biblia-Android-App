package com.aneury1.biblia.reader

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aneury1.biblia.R

@Composable
fun ReaderScreen(book: String, chapter: Int, target: Int, size: Float, favorites: Set<String>,
                 completed: Set<String>, onVerse: (Passage) -> Unit, onChapter: (Int) -> Unit, onComplete: () -> Unit) {
    val all = Scripture.data[book].orEmpty()
    val chapters = remember(book) { all.map { it.chapter }.distinct() }
    val actual = chapter.coerceIn(1, chapters.last())
    val verses = remember(book, actual) { all.filter { it.chapter == actual } }
    val state = rememberLazyListState()
    LaunchedEffect(book, actual, target) {
        val index = verses.indexOfFirst { it.verse == target }.coerceAtLeast(0)
        state.scrollToItem(index + 1)
    }
    Column(Modifier.fillMaxSize()) {
        LazyColumn(state = state, modifier = Modifier.weight(1f), contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 100.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item {
                Text(stringResource(R.string.chapter_number, actual), style = MaterialTheme.typography.headlineMedium)
                Text(stringResource(R.string.verse_actions_hint), style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 12.dp))
            }
            items(verses, key = { it.verse }) { verse ->
                val p = Passage(book, verse)
                Card(onClick = { onVerse(p) }, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                    containerColor = if (p.key in favorites) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface)) {
                    Row(Modifier.padding(horizontal = 12.dp, vertical = 14.dp)) {
                        Text(verse.verse.toString(), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge, modifier = Modifier.width(32.dp))
                        Text(verse.text, fontSize = size.sp, lineHeight = (size * 1.55f).sp, fontFamily = FontFamily.Serif, modifier = Modifier.weight(1f))
                        if (p.key in favorites) Icon(Icons.Default.Bookmark, stringResource(R.string.saved), modifier = Modifier.padding(start = 6.dp).size(18.dp))
                    }
                }
            }
            item { FilledTonalButton(onClick = onComplete, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(if ("$book|$chapter" in completed) R.string.mark_unread else R.string.mark_read))
            } }
        }
        Surface(tonalElevation = 3.dp) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = { onChapter(actual - 1) }, enabled = actual > 1) { Text(stringResource(R.string.previous)) }
                TextButton(onClick = { onChapter(actual + 1) }, enabled = actual < chapters.last()) { Text(stringResource(R.string.next)) }
            }
        }
    }
}

@Composable
fun ChapterPicker(book: String, chapter: Int, onDismiss: () -> Unit, choose: (Int) -> Unit) {
    val chapters = remember(book) { Scripture.data[book].orEmpty().map { it.chapter }.distinct() }
    AlertDialog(onDismissRequest = onDismiss, title = { Text(stringResource(R.string.choose_chapter)) },
        text = {
            LazyVerticalGrid(columns = GridCells.Adaptive(56.dp), modifier = Modifier.fillMaxWidth().heightIn(max = 360.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                items(chapters) { c ->
                    if (c == chapter) Button(onClick = { choose(c) }, contentPadding = PaddingValues(0.dp)) { Text(c.toString()) }
                    else OutlinedButton(onClick = { choose(c) }, contentPadding = PaddingValues(0.dp)) { Text(c.toString()) }
                }
            }
        }, confirmButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.close)) } })
}

@Composable
fun NoteEditor(note: SavedNote, saving: Boolean, dismiss: () -> Unit, save: (SavedNote) -> Unit) {
    var text by remember(note) { mutableStateOf(note.text) }
    var book by remember(note) { mutableStateOf(note.book.takeIf { it in Scripture.books } ?: "Genesis") }
    var chapter by remember(note) { mutableStateOf(note.chapter.toIntOrNull() ?: 1) }
    var verse by remember(note) { mutableStateOf(note.verse.toIntOrNull() ?: 1) }
    val chapters = Scripture.data[book].orEmpty().map { it.chapter }.distinct()
    val validChapter = chapter.coerceIn(1, chapters.last())
    val verses = Scripture.data[book].orEmpty().filter { it.chapter == validChapter }.map { it.verse }
    val validVerse = verse.coerceIn(1, verses.last())
    AlertDialog(onDismissRequest = { if (!saving) dismiss() },
        title = { Text(stringResource(if (note.id == 0L) R.string.add_note else R.string.edit_note)) },
        text = { Column(Modifier.heightIn(max = 420.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ReferenceMenu(displayBook(book), Scripture.books.map { it to displayBook(it) }, !saving) {
                book = it; chapter = 1; verse = 1
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.weight(1f)) { ReferenceMenu(stringResource(R.string.chapter_number, validChapter),
                    chapters.map { it.toString() to it.toString() }, !saving) { chapter = it.toInt(); verse = 1 } }
                Box(Modifier.weight(1f)) { ReferenceMenu(stringResource(R.string.verse_number, validVerse),
                    verses.map { it.toString() to it.toString() }, !saving) { verse = it.toInt() } }
            }
            OutlinedTextField(text, { text = it }, enabled = !saving,
                label = { Text(stringResource(R.string.note_hint)) }, minLines = 4, maxLines = 8, modifier = Modifier.fillMaxWidth())
        } }, confirmButton = { TextButton(onClick = { save(note.copy(book = book, chapter = validChapter.toString(), verse = validVerse.toString(), text = text)) },
            enabled = text.isNotBlank() && !saving) { Text(stringResource(if (saving) R.string.saving else R.string.save)) } },
        dismissButton = { TextButton(onClick = dismiss, enabled = !saving) { Text(stringResource(R.string.cancel)) } })
}

@Composable
private fun ReferenceMenu(label: String, options: List<Pair<String, String>>, enabled: Boolean, select: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }, enabled = enabled, modifier = Modifier.fillMaxWidth()) { Text(label) }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.heightIn(max = 280.dp)) {
            options.forEach { (value, title) -> DropdownMenuItem(text = { Text(title) }, onClick = { select(value); expanded = false }) }
        }
    }
}
