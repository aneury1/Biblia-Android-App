package com.aneury1.biblia.Screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aneury1.biblia.Data.BibleFunction.BibleData

@Composable
fun ListOfBooks(navHostController: NavHostController){
    print("Bible")
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp)
    ){
        Spacer(modifier=Modifier.height(10.dp))
        Text("Biblia Reina Valera 1960", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier=Modifier.height(10.dp))
        ///Text("Antiguo Testamento", fontSize = 32.sp, fontWeight = FontWeight.Normal)
        ///Spacer(modifier=Modifier.height(10.dp))
        LazyColumn(){
            val books = BibleData.getBooksNames()
            items(count = books.size){
                Log.d("Bible","Book ${books[it]}");
                if(books[it]=="Mateo"){
                    Spacer(modifier=Modifier.height(10.dp))
                    Text("Nuevo Testamento", fontSize = 32.sp, fontWeight = FontWeight.Normal)
                    Spacer(modifier=Modifier.height(10.dp))
                }
                else if(books[it]=="Genesis")
                {
                    Spacer(modifier=Modifier.height(10.dp))
                    Text("Antiguo Testamento", fontSize = 32.sp, fontWeight = FontWeight.Normal)
                    Spacer(modifier=Modifier.height(10.dp))
                }
                BookTitle(navHostController, books[it])
            }
        }
        Spacer(modifier=Modifier.height(10.dp))
    }
}