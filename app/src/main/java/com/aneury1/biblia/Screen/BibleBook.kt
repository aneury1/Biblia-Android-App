package com.aneury1.biblia.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aneury1.biblia.Data.BibleFunction.BibleData
import com.aneury1.biblia.Data.BibleFunction.BibleData.getWholeBook
import com.aneury1.biblia.Data.BibleVerse
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun BibleBook(navHostController: NavHostController, bookname: String)
{
    val coroutineScope = rememberCoroutineScope() // provides a CoroutineScope tied to Compose lifecycle


    val book: List<BibleVerse>? = getWholeBook()[bookname]
    var currentChapter= 1
    val listState = rememberLazyListState()
    if(book?.size!! > 0){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(96.dp))
            Text(bookname, fontSize = 48.sp, color = Color.Red)
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ){
                Button(onClick = {
                    coroutineScope.launch {
                        listState.scrollToItem(index = 0)
                    }
                }) {
                    Text("inicio")
                }
                Button(onClick = {
                    coroutineScope.launch {
                        listState.scrollToItem(index = book.size/2)
                    }
                }) {
                    Text("mitad")
                }
                Button(onClick = {
                    coroutineScope.launch {
                        listState.scrollToItem(index = book.size-1)
                    }
                }) {
                    Text("fin")
                }
            }


            LazyColumn(state = listState) {
                items(count = book.size){
                    val ch = book[it]
                    var newc = false
                    if(ch.chapter!=currentChapter){
                        newc = true;
                        currentChapter = ch.chapter
                    }
                    ChapterParagraph(ch.chapter, ch.verse, ch.text,bookname,newc)
                }
            }

        }


    }else{
        Text("Libro no encontrado...")
    }
}