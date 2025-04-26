package com.aneury1.biblia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aneury1.biblia.Screen.BibleBook
import com.aneury1.biblia.Screen.BookTitle
import com.aneury1.biblia.Screen.ChapterParagraph
import com.aneury1.biblia.Screen.ListOfBooks
import com.aneury1.biblia.ui.theme.BibliaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BibliaTheme {
              Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") { ListOfBooks(navController)}
                        composable(
                            "detail/{bookname}",
                            arguments = listOf(navArgument("bookname")
                            {
                                type = NavType.StringType
                            }
                            )
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("bookname") ?: "Home"
                            if(id=="Home")
                                ListOfBooks(navController)
                            else
                                BibleBook(navController, id)
                        }





                    }
                }
            }
        }
    }
}
@Composable
fun HomeScreen(navController: NavHostController) {
    Column {
        Text("Home Screen")
        Button(onClick = { navController.navigate("detail/5") }) {
            Text("Go to Detail for ID 5")
        }
    }
}

@Composable
fun DetailScreen(navController: NavHostController, id: Int) {
    Column {
        Text("Detail Screen for ID: $id")
        Button(onClick = { navController.popBackStack() }) {
            Text("Go Back")
        }
    }
}