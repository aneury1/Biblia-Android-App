package com.aneury1.biblia.Screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavHostController
import com.aneury1.biblia.R


@Composable
fun SimpleParagraph(number: Int, verseNo: Int, text: String,bookName: String){
    val context = LocalContext.current
    val recoverit = "${number}:${verseNo}"
    val prefslook = context.getSharedPreferences("bible_favorites_${bookName}", Context.MODE_PRIVATE)
    val key =prefslook.getStringSet("favorites", emptySet()) ?: emptySet()


    var isFavorite by remember { mutableStateOf(key.filter { it -> it == recoverit }.isNotEmpty()) }

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


    Column(
        modifier = Modifier
            .pointerInput(Unit){
                detectTapGestures(
                    onDoubleTap = {
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
                    onLongPress = {
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
           /* .pointerInput(Unit){
                detectTapGestures {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, text)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
            }*/
            .padding(4.dp)
            ///.background(Color(0xe9edf5ff))
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