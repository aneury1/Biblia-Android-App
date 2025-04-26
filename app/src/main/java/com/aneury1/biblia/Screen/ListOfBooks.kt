package com.aneury1.biblia.Screen

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.aneury1.biblia.Data.BibleFunction.BibleData

@Composable
fun ListOfBooks(navHostController: NavHostController){
    print("Bible")
    LazyColumn(){
       val books = BibleData.getBooksNames()
       items(count = books.size){
           Log.d("Bible","Book ${books[it]}");
           BookTitle(navHostController, books[it])
       }
   }
}