package com.aneury1.biblia.reader

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aneury1.biblia.R
import com.aneury1.biblia.ui.theme.BibliaTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibleApp() {
    val context = LocalContext.current
    val store = remember { ReadingStore(context.applicationContext) }
    val repo = remember { NoteRepository(context) }
    DisposableEffect(repo) { onDispose { repo.close() } }
    val scope = rememberCoroutineScope()
    var theme by remember { mutableStateOf(store.theme) }
    var size by remember { mutableStateOf(store.fontSize) }
    var page by rememberSaveable { mutableStateOf(0) }
    var reading by rememberSaveable { mutableStateOf(false) }
    var book by rememberSaveable { mutableStateOf(store.book) }
    var chapter by rememberSaveable { mutableStateOf(store.chapter) }
    var targetVerse by rememberSaveable { mutableStateOf(1) }
    var favorites by remember { mutableStateOf(store.favorites()) }
    var completed by remember { mutableStateOf(store.completed()) }
    var selected by remember { mutableStateOf<Passage?>(null) }
    var note by remember { mutableStateOf<SavedNote?>(null) }
    var saving by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf(emptyList<SavedNote>()) }
    var deleteNote by remember { mutableStateOf<SavedNote?>(null) }
    var themePicker by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var picker by remember { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }
    val errorText = stringResource(R.string.save_error)
    var ready by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) { Scripture.passages; Scripture.index }
        chapter = chapter.coerceIn(1, Scripture.data[book]!!.last().chapter)
        store.chapter = chapter
        try { notes = repo.all() } catch (_: Exception) { scope.launch { snackbar.showSnackbar(errorText) } }
        ready = true
    }
    fun open(p: Passage) {
        book = p.book; chapter = p.verse.chapter; targetVerse = p.verse.verse
        store.book = book; store.chapter = chapter; reading = true
    }
    val dark = when (theme) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
    BibliaTheme(darkTheme = dark) {
        BackHandler(reading) { reading = false }
        Scaffold(
            snackbarHost = { SnackbarHost(snackbar) },
            floatingActionButton = {
                if (ready && (reading || page == 2)) ExtendedFloatingActionButton(
                    onClick = { note = SavedNote(0, book, chapter.toString(), "1", "") },
                    icon = { Icon(Icons.Default.NoteAdd, null) },
                    text = { Text(stringResource(R.string.add_note)) }
                )
            },
            topBar = { TopAppBar(
                title = { Column {
                    Text(if (reading) displayBook(book) else stringResource(R.string.app_name), style = MaterialTheme.typography.titleLarge)
                    Text(if (reading) stringResource(R.string.chapter_number, chapter) else stringResource(R.string.offline_bible),
                        style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                } },
                navigationIcon = { if (reading) IconButton(onClick = { reading = false }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back)) } },
                actions = {
                    IconButton(onClick = { themePicker = true }) {
                        Icon(if (dark) Icons.Default.DarkMode else Icons.Default.LightMode, stringResource(R.string.appearance))
                    }
                    if (reading) IconButton(onClick = { picker = true }) { Icon(Icons.Default.GridView, stringResource(R.string.choose_chapter)) }
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, stringResource(R.string.open_menu))
                        }
                        DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.search_bible)) },
                                leadingIcon = { Icon(Icons.Default.Search, null) },
                                onClick = { menuExpanded = false; reading = false; page = 1 }
                            )
                        }
                    }
                }
            ) },
            bottomBar = { if (!reading) NavigationBar {
                val labels = listOf(R.string.home, R.string.search, R.string.saved, R.string.settings)
                val icons = listOf(Icons.AutoMirrored.Filled.MenuBook, Icons.Default.Search, Icons.Default.Bookmark, Icons.Default.Settings)
                labels.forEachIndexed { i, label -> NavigationBarItem(selected = page == i, onClick = { page = i },
                    icon = { Icon(icons[i], null) }, label = { Text(stringResource(label)) }) }
            } }
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                if (!ready) CircularProgressIndicator(Modifier.align(Alignment.Center))
                else if (reading) ReaderScreen(book, chapter, targetVerse, size, favorites, completed,
                    onVerse = { selected = it }, onChapter = { next ->
                        chapter = next; targetVerse = 1; store.chapter = next
                    }, onComplete = { store.toggleChapter(book, chapter); completed = store.completed() })
                else when (page) {
                    0 -> HomeScreen(book, chapter, completed, ::open)
                    1 -> SearchScreen(searchQuery, { searchQuery = it }, ::open)
                    2 -> SavedScreen(favorites, notes, ::open, { note = it }, { deleteNote = it })
                    else -> SettingsScreen(theme, size, { theme = it; store.theme = it }, { size = it; store.fontSize = it })
                }
            }
        }
        if (themePicker) AlertDialog(
            onDismissRequest = { themePicker = false },
            title = { Text(stringResource(R.string.appearance)) },
            text = { ThemeOptions(theme) { theme = it; store.theme = it } },
            confirmButton = { TextButton(onClick = { themePicker = false }) { Text(stringResource(R.string.close)) } }
        )
        if (picker && ready) ChapterPicker(book, chapter, onDismiss = { picker = false }) { next ->
            chapter = next; targetVerse = 1; store.chapter = next; picker = false
        }
        selected?.let { passage ->
            ModalBottomSheet(onDismissRequest = { selected = null }) {
                Column(Modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(passage.reference, style = MaterialTheme.typography.titleLarge)
                    Text(passage.verse.text, style = MaterialTheme.typography.bodyLarge)
                    Button(onClick = { store.toggle(passage.key); favorites = store.favorites(); selected = null }, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(if (passage.key in favorites) R.string.remove_saved else R.string.save_verse))
                    }
                    OutlinedButton(onClick = { note = SavedNote(0, passage.book, passage.verse.chapter.toString(), passage.verse.verse.toString(), ""); selected = null }, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.add_note)) }
                    OutlinedButton(onClick = {
                        context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"; putExtra(Intent.EXTRA_TEXT, "${passage.reference}\n${passage.verse.text}")
                        }, null)); selected = null
                    }, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.share)) }
                }
            }
        }
        note?.let { editing -> NoteEditor(editing, saving, { if (!saving) note = null }) { updated ->
            if (!saving) {
            saving = true
            scope.launch {
                try { repo.save(updated); notes = repo.all(); note = null }
                catch (_: Exception) { snackbar.showSnackbar(errorText) }
                finally { saving = false }
            }
            }
        } }
        deleteNote?.let { removing -> AlertDialog(onDismissRequest = { deleteNote = null },
            title = { Text(stringResource(R.string.delete_note)) }, text = { Text(stringResource(R.string.delete_confirm)) },
            confirmButton = { TextButton(onClick = { scope.launch {
                try { repo.delete(removing.id); notes = repo.all(); deleteNote = null }
                catch (_: Exception) { snackbar.showSnackbar(errorText) }
            } }) { Text(stringResource(R.string.delete)) } },
            dismissButton = { TextButton(onClick = { deleteNote = null }) { Text(stringResource(R.string.cancel)) } }) }
    }
}

@Composable
private fun HomeScreen(book: String, chapter: Int, completed: Set<String>, open: (Passage) -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    var testament by rememberSaveable { mutableStateOf(0) }
    val today = remember { Scripture.passages[Math.floorMod(LocalDate.now().toEpochDay(), Scripture.passages.size.toLong()).toInt()] }
    val filtered = Scripture.books.filterIndexed { i, name ->
        (testament == 0 || (testament == 1 && i < 39) || (testament == 2 && i >= 39)) && normalize(query) in normalize(displayBook(name))
    }
    LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { Text(stringResource(R.string.welcome), style = MaterialTheme.typography.headlineMedium) }
        item { Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), shape = RoundedCornerShape(24.dp), onClick = { open(today) }) {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(stringResource(R.string.daily_verse), style = MaterialTheme.typography.labelLarge)
                Text(today.verse.text, style = MaterialTheme.typography.titleLarge, fontFamily = FontFamily.Serif)
                Text(today.reference, style = MaterialTheme.typography.labelLarge)
            }
        } }
        item { FilledTonalButton(onClick = {
            val verses = Scripture.data[book].orEmpty()
            open(Passage(book, verses.firstOrNull { it.chapter == chapter } ?: verses.first()))
        }, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.continue_reading, displayBook(book), chapter)) } }
        item { Text(stringResource(R.string.progress, completed.size), style = MaterialTheme.typography.bodyMedium)
            LinearProgressIndicator(progress = { completed.size / 1189f }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) }
        item { OutlinedTextField(query, { query = it }, label = { Text(stringResource(R.string.find_book)) }, singleLine = true, modifier = Modifier.fillMaxWidth(), leadingIcon = { Icon(Icons.Default.Search, null) }) }
        item { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(R.string.all, R.string.old_testament, R.string.new_testament).forEachIndexed { i, id -> FilterChip(selected = testament == i, onClick = { testament = i }, label = { Text(stringResource(id)) }) }
        } }
        if (filtered.isEmpty()) item { Text(stringResource(R.string.no_results)) }
        items(filtered, key = { it }) { name ->
            val verses = Scripture.data[name].orEmpty()
            OutlinedCard(onClick = { open(Passage(name, verses.first())) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.MenuBook, null, tint = MaterialTheme.colorScheme.primary)
                    Column(Modifier.weight(1f).padding(start = 16.dp)) {
                        Text(displayBook(name), style = MaterialTheme.typography.titleMedium)
                        Text(stringResource(R.string.chapter_count, verses.last().chapter), style = MaterialTheme.typography.bodySmall)
                    }
                    Icon(Icons.Default.ChevronRight, null)
                }
            }
        }
    }
}

@Composable
private fun SearchScreen(query: String, onQueryChange: (String) -> Unit, open: (Passage) -> Unit) {
    var results by remember { mutableStateOf(emptyList<Passage>()) }
    var searching by remember { mutableStateOf(false) }
    LaunchedEffect(query) {
        if (query.trim().length < 2) { results = emptyList(); searching = false; return@LaunchedEffect }
        searching = true
        results = emptyList()
        delay(250)
        results = withContext(Dispatchers.Default) { Scripture.search(query) }
        searching = false
    }
    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Text(stringResource(R.string.search_bible), style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(vertical = 16.dp))
        OutlinedTextField(query, onQueryChange, label = { Text(stringResource(R.string.search_hint)) }, modifier = Modifier.fillMaxWidth(), singleLine = true,
            trailingIcon = { if (query.isNotEmpty()) IconButton(onClick = { onQueryChange("") }) { Icon(Icons.Default.Close, stringResource(R.string.clear)) } })
        if (searching) LinearProgressIndicator(Modifier.fillMaxWidth().padding(vertical = 12.dp))
        else Text(stringResource(if (query.trim().length < 2) R.string.search_help else if (results.isEmpty()) R.string.no_results else R.string.results, results.size), modifier = Modifier.padding(vertical = 12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 20.dp)) {
            items(results, key = { it.key }) { p -> PassageCard(p, { open(p) }) }
        }
    }
}

@Composable
private fun SavedScreen(favorites: Set<String>, notes: List<SavedNote>, open: (Passage) -> Unit,
                        edit: (SavedNote) -> Unit, delete: (SavedNote) -> Unit) {
    var tab by rememberSaveable { mutableStateOf(0) }
    Column(Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = tab) {
            listOf(R.string.saved_verses, R.string.notes).forEachIndexed { i, label -> Tab(selected = tab == i, onClick = { tab = i }, text = { Text(stringResource(label)) }) }
        }
        LazyColumn(contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 100.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (tab == 0) {
                val saved = Scripture.passages.filter { it.key in favorites }
                if (saved.isEmpty()) item { Text(stringResource(R.string.empty_saved)) }
                items(saved, key = { it.key }) { p -> PassageCard(p, { open(p) }) }
            } else {
                if (notes.isEmpty()) item { Text(stringResource(R.string.empty_notes)) }
                items(notes, key = { it.id }) { n -> OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        val p = Scripture.data[n.book]?.firstOrNull { it.chapter.toString() == n.chapter && it.verse.toString() == n.verse }?.let { Passage(n.book, it) }
                        Text("${displayBook(n.book)} ${n.chapter}:${n.verse}", style = MaterialTheme.typography.titleMedium)
                        Text(n.text)
                        Row {
                            TextButton(onClick = { edit(n) }) { Text(stringResource(R.string.edit)) }
                            if (p != null) TextButton(onClick = { open(p) }) { Text(stringResource(R.string.read)) }
                            IconButton(onClick = { delete(n) }) { Icon(Icons.Default.DeleteOutline, stringResource(R.string.delete_note)) }
                        }
                    }
                } }
            }
        }
    }
}

@Composable
private fun PassageCard(p: Passage, onClick: () -> Unit) {
    OutlinedCard(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(p.reference, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleSmall)
            Text(p.verse.text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun SettingsScreen(theme: String, size: Float, setTheme: (String) -> Unit, setSize: (Float) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item { Text(stringResource(R.string.read_your_way), style = MaterialTheme.typography.headlineMedium) }
        item { Text(stringResource(R.string.appearance), style = MaterialTheme.typography.titleMedium) }
        item { ThemeOptions(theme, setTheme) }
        item { Text(stringResource(R.string.font_size, size.toInt()))
            Slider(value = size, onValueChange = setSize, valueRange = 16f..30f, steps = 13)
            Text(stringResource(R.string.font_preview), fontFamily = FontFamily.Serif, fontSize = size.sp, lineHeight = (size * 1.55f).sp) }
        item { Text(stringResource(R.string.about_offline), style = MaterialTheme.typography.bodyMedium) }
    }
}

@Composable
private fun ThemeOptions(theme: String, select: (String) -> Unit) {
    Column {
        listOf("system" to R.string.system_theme, "light" to R.string.light_theme,
            "dark" to R.string.dark_theme).forEach { (value, label) ->
            Row(Modifier.fillMaxWidth().selectable(selected = theme == value,
                role = androidx.compose.ui.semantics.Role.RadioButton, onClick = { select(value) })
                .padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = theme == value, onClick = null)
                Text(stringResource(label), Modifier.padding(start = 12.dp))
            }
        }
    }
}
