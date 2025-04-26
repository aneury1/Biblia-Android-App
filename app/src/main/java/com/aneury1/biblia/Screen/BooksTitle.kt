package com.aneury1.biblia.Screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun BookTitle(navHostController: NavHostController,title: String){
    Row(){

        Text(title,
          modifier= Modifier.padding(10.dp)
              .clickable {
                  print(title)
                  navHostController.navigate("detail/${title}")
              },
          fontSize = 24.sp,
          fontWeight = FontWeight.Black,
          )
    }
}

@Composable
fun ChapterParagraph(number: Int, verseNo: Int, text: String){
    val context = LocalContext.current
    Row(
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
        horizontalArrangement = Arrangement.Center,
    ){

        Column{
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 23.sp,
                    color = Color.Red
                )) {
                    append("$number. ")
                }
                withStyle(style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 23.sp,
                    color = Color. Blue
                )) {
                    append("$verseNo. ")
                }
                append(text)
            },
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp)
        )
    }
        Column {
            Button({

            }) {
                
            }
        }
    }
}