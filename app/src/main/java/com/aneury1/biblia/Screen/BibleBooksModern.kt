package com.aneury1.biblia.Screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aneury1.biblia.Data.BibleFunction.BibleData
import com.aneury1.biblia.Data.BibleFunction.BibleData.getWholeBook
import com.aneury1.biblia.Data.BibleVerse
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun BookTitleV2(navHostController: NavHostController, title: String, clicked: Boolean){
    Row(){
        val textTitle = replaceUnderscore(title)
        Text(textTitle,
            modifier= Modifier.padding(10.dp)
                .clickable {
                    if(clicked)
                        navHostController.navigate("detail/${title}")
                    else
                        navHostController.navigate("/notes")
                },
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
        )
    }
}

@Composable
fun ShowBibleBooks(navHostController: NavHostController)
{
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp)
    ){
        Spacer(modifier=Modifier.height(10.dp))
        Text("Biblia Reina Valera 1960", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier=Modifier.height(10.dp))
        LazyColumn(){
            val books = BibleData.getBooksNames()
            items(count = books.size){
                Log.d("Bible","Book ${books[it]}");
                if(books[it]=="Mateo"){
                    Spacer(modifier=Modifier.height(10.dp))
                    Text("Nuevo Testamento", fontSize = 32.sp, fontWeight = FontWeight.Normal)
                    Spacer(modifier=Modifier.height(10.dp))
                    BookTitleV2(navHostController, books[it], true)

                }
                else if(books[it]=="Genesis")
                {
                    Spacer(modifier=Modifier.height(10.dp))
                    Text("Antiguo Testamento", fontSize = 32.sp, fontWeight = FontWeight.Normal)
                    Spacer(modifier=Modifier.height(10.dp))
                    BookTitleV2(navHostController, books[it], true)
                }
                else if(books[it].contains("Notas Guardadas"))
                {
                    BookTitleV2(navHostController, books[it], false)
                }
                else if(books[it].contains("Configuracion"))
                {
                    BookTitleV2(navHostController, books[it], false)
                }
                else
                {
                    BookTitleV2(navHostController, books[it], true)
                }
            }
        }
        Spacer(modifier=Modifier.height(10.dp))
    }
}

@Composable
fun ShowGridOfChapter(bookName: String, onItemClick: (Int)->Unit)
{
    val books = getWholeBook()
    val booksNumbers = books[bookName]
    var setOfNumber = mutableSetOf<Int>()
    booksNumbers?.forEach {
        print(it.chapter)
        setOfNumber.add(it.chapter)
    }
    val columns = 8
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(2.dp)
    ) {
        val count = setOfNumber.toList().size
        items(count) { item ->
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(5.dp)
                    .background(Color(100,100,100,255))
                    .clickable(onClick = {
                        onItemClick(item+1)
                    },
                    )
            ) {
                Text("${item+1}", color=Color.White, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
fun ShowBook(navHostController: NavHostController,bookname: String ="Genesis")
{
    val coroutineScope = rememberCoroutineScope()
    val book: List<BibleVerse>? = getWholeBook()[bookname]
    var currentChapter= 1
    val listState = rememberLazyListState()
    Column (
        modifier = Modifier.fillMaxWidth().padding(top=10.dp)){
        Text(bookname,
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            fontSize = 33.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center)

    ShowGridOfChapter(bookname){ chapter ->
        val index = book
            ?.indexOfFirst { it.chapter == chapter }
            ?: return@ShowGridOfChapter
        if (index >= 0) {
            coroutineScope.launch{
                listState.scrollToItem(index)
            }
        }
    }
    LazyColumn(state = listState) {
        items(count = book!!.size){
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
}

