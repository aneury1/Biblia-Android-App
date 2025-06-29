package com.aneury1.biblia.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aneury1.biblia.Data.BibleFunction.BibleData
import com.aneury1.biblia.Data.BibleSqlite3
import com.aneury1.biblia.Data.getAllNotes

@Composable
fun NotasGuardada(navHostController: NavHostController)
{
    val context = LocalContext.current
    val db = BibleSqlite3(context)
    val rd = db.readableDatabase

    val allNotes = getAllNotes(rd);
    if(allNotes.size>0) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text("Biblia Reina Valera 1960", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn() {

                items(count = allNotes.size) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(4.dp).background(Color(0xFFAABBFF))
                    ) {
                        Text("Libro ${allNotes[it].book}")
                        Text("${allNotes[it].chapter}:${allNotes[it].verse}")
                        Text(allNotes[it].note)
                    }
                }
            }
        }
    }else
    {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text("Biblia Reina Valera 1960", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxSize(0.90f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text("No hay notas guardadas", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }

        }
    }
}