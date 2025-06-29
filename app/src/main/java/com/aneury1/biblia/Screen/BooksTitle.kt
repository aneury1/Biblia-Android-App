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

fun replaceUnderscore(currentText: String) :String{

   val ret= currentText.replace('_',' ')
    return ret;
}

@Composable
fun BookTitle(navHostController: NavHostController,title: String){
    Row(){
        val textTitle = replaceUnderscore(title)
        Text(textTitle,
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
