package com.aneury1.biblia.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aneury1.biblia.Data.BibleFunction.BibleData
import com.aneury1.biblia.Data.BibleFunction.BibleData.getWholeBook
import com.aneury1.biblia.Data.BibleVerse

@Composable
fun BibleBook(navHostController: NavHostController, list: String)
{
    val book: List<BibleVerse>? = getWholeBook()["Genesis"]
    if(book?.size!! > 0){


        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(96.dp))
            Text(list, fontSize = 48.sp, color = Color.Red)

            LazyColumn() {
                items(count = book.size){
                    val ch = book[it]
                    ChapterParagraph(ch.chapter, ch.verse, ch.text)
                }
            }

        }


    }else{
        Text("Libro no encontrado...")
    }
}