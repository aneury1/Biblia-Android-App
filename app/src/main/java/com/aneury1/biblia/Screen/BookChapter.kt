package com.aneury1.biblia.Screen

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.aneury1.biblia.Data.BibleNote
import com.aneury1.biblia.Data.BibleSqlite3
import com.aneury1.biblia.Data.getAllNotes
import com.aneury1.biblia.Data.insertNote
import com.aneury1.biblia.R
@Composable
fun TextInputDialog(
    title: String,
    initialText: String = "",
    placeholder: String = "Enter text...",
    confirmText: String = "Save",
    dismissText: String = "Cancel",
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by rememberSaveable { mutableStateOf(initialText) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .widthIn(min = 280.dp, max = 420.dp)
            ) {

                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                // TextArea
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text(placeholder) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp),
                    maxLines = Int.MAX_VALUE,
                    singleLine = false
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(dismissText)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = { onConfirm(text) },
                        enabled = text.isNotBlank()
                    ) {
                        Text(confirmText)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTextDialog(
    title: String,
    message: String,
    confirmText: String = "OK",
    dismissText: String = "Cancel",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .widthIn(min = 280.dp, max = 400.dp)
            ) {

                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Message
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(dismissText)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = onConfirm) {
                        Text(confirmText)
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleParagraph(number: Int, verseNo: Int, text: String,bookName: String){
    val context = LocalContext.current
    val recoverit = "${number}:${verseNo}"
    val prefslook = context.getSharedPreferences("bible_favorites_${bookName}", Context.MODE_PRIVATE)
    val key =prefslook.getStringSet("favorites", emptySet()) ?: emptySet()
    var showDialog by remember { mutableStateOf(false) }

    var isFavorite by remember { mutableStateOf(key.filter { it -> it == recoverit }.isNotEmpty()) }
    val dbHelper = BibleSqlite3(context)
    val db = dbHelper.writableDatabase
    val dbRead = dbHelper.readableDatabase

    val notes =  getAllNotes(db)

    val inlineContent = mapOf(
        "imageId" to InlineTextContent(
            Placeholder(
                width = 20.sp,
                height = 20.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.bible), // your image here
                contentDescription = null
            )
        }
    )

    val inlineContentNotes = mapOf(
        "imageIdForExtra" to InlineTextContent(
            Placeholder(
                width = 25.sp,
                height = 25.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.bible), // your image here
                contentDescription = null
            )
        }
    )

    if (showDialog) {
        TextInputDialog(
            title = "Agregar comentario",
            initialText = "",
            onConfirm = { enteredText ->
                showDialog = false
                Log.d("Dialog", "User entered: $enteredText")



                val noteId = insertNote(
                    db,
                    BibleNote(
                        book = "$bookName",
                        chapter = "$number",
                        verse = "$verseNo",
                        note = enteredText
                        ///createdAt = System.currentTimeMillis()
                    )
                )


            },
            onDismiss = {
                showDialog = false
            }
        )
    }

    Column(
        modifier = Modifier
            .pointerInput(Unit){
                detectTapGestures(
                    onDoubleTap = {
                        showDialog = false
                        val prefs = context.getSharedPreferences("bible_favorites_${bookName}", Context.MODE_PRIVATE)
                        val current = prefs.getStringSet("favorites", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
                        val saveit = "${number}:${verseNo}"

                        if(isFavorite ){
                           // current.removeIf {
                           //      isFavorite
                           // }
                            isFavorite=false
                            current.remove(saveit)
                            prefs.edit().putStringSet("favorites", current).apply()
                        }
                        else {
                            isFavorite=true
                            current.add(saveit)
                            prefs.edit().putStringSet("favorites", current).apply()
                        }
                    },
                    onPress = {
                        showDialog = true
                    },
                    onLongPress = {
                        showDialog = false
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, text)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }
                ) {

                }
            }
            .padding(4.dp).background(Color(0xe9edf5ff)).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 23.sp,
                    color = Color.Red
                )
                ) {
                    if(isFavorite){
                        appendInlineContent("imageId", "[icon]")
                       ////   appendInlineContent("imageIdForExtra", "[icon]")
                    }
                    append("$number. ")
                }
                withStyle(style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 23.sp,
                    color = Color. Blue
                )
                ) {
                    append("$verseNo. ")
                }
                append(text)
            },
            inlineContent = inlineContent,
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun ChapterParagraph(number: Int, verseNo: Int, text: String,bookName: String,newChapter: Boolean=false){
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ){

        Column{
            if(newChapter){
                Text("Capitulo: ${number}",
                    fontSize = 38.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Black)

            }

            SimpleParagraph(number, verseNo, text,bookName)

            /*
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 23.sp,
                        color = Color.Red
                    )
                    ) {
                        append("$number. ")
                    }
                    withStyle(style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 23.sp,
                        color = Color. Blue
                    )
                    ) {
                        append("$verseNo. ")
                    }
                    append(text)
                },
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )

            */
        }

    }
}