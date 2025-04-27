package com.aneury1.biblia.Screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun SimpleParagraph(number: Int, verseNo: Int, text: String){
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .pointerInput(Unit){
                detectTapGestures {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, text)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
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
    }
}

@Composable
fun ChapterParagraph(number: Int, verseNo: Int, text: String,newChapter: Boolean=false){
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

            SimpleParagraph(number, verseNo, text)

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