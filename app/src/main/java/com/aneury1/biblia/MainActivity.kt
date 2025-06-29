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
import com.aneury1.biblia.Screen.BookTitleV2
import com.aneury1.biblia.Screen.ChapterParagraph
import com.aneury1.biblia.Screen.ListOfBooks
import com.aneury1.biblia.Screen.NotasGuardada
import com.aneury1.biblia.Screen.ShowBibleBooks
import com.aneury1.biblia.Screen.ShowBook
import com.aneury1.biblia.ui.theme.BibliaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BibliaTheme {
              Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "Home") {

                        composable("Home"){
                            ShowBibleBooks(navController)
                        }

                        composable("/notes"){
                            NotasGuardada(navController)
                        }

                        ////composable("home") { ListOfBooks(navController)}
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
                                ShowBibleBooks(navController)
                            else
                                ShowBook(navController, id)
                             //// BibleBook(navController, id)
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
/*


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhatsAppTheme {
                WhatsAppApp()
            }
        }
    }
}

// Color Scheme WhatsApp
object WhatsAppColors {
    val Primary = Color(0xFF075E54)
    val Secondary = Color(0xFF25D366)
    val Tertiary = Color(0xFF128C7E)
    val Background = Color(0xFFECE5DD)
    val Surface = Color(0xFFFFFFFF)
    val ChatBubbleMe = Color(0xFFDCF8C6)
    val ChatBubbleOther = Color(0xFFFFFFFF)
    val StatusBarColor = Color(0xFF075E54)
    val UnreadBadge = Color(0xFF25D366)
}

@Composable
fun WhatsAppTheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary = WhatsAppColors.Primary,
        secondary = WhatsAppColors.Secondary,
        background = WhatsAppColors.Background,
        surface = WhatsAppColors.Surface
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

// Data Classes
data class Chat(
    val id: Int,
    val name: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0,
    val profileInitial: String,
    val isOnline: Boolean = false
)

data class Status(
    val id: Int,
    val name: String,
    val time: String,
    val profileInitial: String,
    val hasViewed: Boolean = false
)

data class Call(
    val id: Int,
    val name: String,
    val time: String,
    val type: String, // "incoming", "outgoing", "missed"
    val isVideo: Boolean = false,
    val profileInitial: String
)

data class Message(
    val id: Int,
    val text: String,
    val time: String,
    val isMe: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsAppApp() {
    var selectedTab by remember { mutableStateOf(0) }
    var showChatDetail by remember { mutableStateOf(false) }
    var selectedChat by remember { mutableStateOf<Chat?>(null) }

    val tabs = listOf("Chats", "Estados", "Llamadas")

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "WhatsApp",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = WhatsAppColors.Primary,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White
                    ),
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.Search, "Buscar")
                        }
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.MoreVert, "Más opciones")
                        }
                    }
                )

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = WhatsAppColors.Primary,
                    contentColor = Color.White
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    title,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = WhatsAppColors.Secondary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = when (selectedTab) {
                        0 -> Icons.Default.Add
                        1 -> Icons.Default.Favorite
                        else -> Icons.Default.Call
                    },
                    contentDescription = "Acción principal"
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (showChatDetail && selectedChat != null) {
                ChatDetailScreen(
                    chat = selectedChat!!,
                    onBack = {
                        showChatDetail = false
                        selectedChat = null
                    }
                )
            } else {
                when (selectedTab) {
                    0 -> ChatsScreen(
                        onChatClick = { chat ->
                            selectedChat = chat
                            showChatDetail = true
                        }
                    )
                    1 -> StatusScreen()
                    2 -> CallsScreen()
                }
            }
        }
    }
}

@Composable
fun ChatsScreen(onChatClick: (Chat) -> Unit) {
    val chats = remember {
        listOf(
            Chat(1, "María García", "Hola! ¿Cómo estás?", "10:30 AM", 3, "MG", true),
            Chat(2, "Juan Pérez", "Nos vemos mañana", "9:15 AM", 0, "JP", false),
            Chat(3, "Grupo Familia", "Papá: Ok, perfecto", "Ayer", 12, "GF", false),
            Chat(4, "Ana Rodríguez", "👍", "Ayer", 0, "AR", true),
            Chat(5, "Carlos Méndez", "¿Ya terminaste el proyecto?", "Lun", 1, "CM", false),
            Chat(6, "Trabajo - Equipo", "Laura: Enviado el reporte", "Dom", 5, "TE", false),
            Chat(7, "Sofía López", "Gracias por tu ayuda!", "Sáb", 0, "SL", true),
            Chat(8, "Pedro Martínez", "¿Vamos al cine?", "Vie", 0, "PM", false),
            Chat(9, "Grupo Amigos", "Luis: JAJAJA", "Jue", 25, "GA", false),
            Chat(10, "Laura Sánchez", "¡Feliz cumpleaños! 🎉", "Mié", 0, "LS", false)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        items(chats) { chat ->
            ChatItem(chat = chat, onClick = { onChatClick(chat) })
            Divider(color = Color.LightGray.copy(alpha = 0.3f))
        }
    }
}

@Composable
fun ChatItem(chat: Chat, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Picture
        Box {
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(WhatsAppColors.Tertiary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chat.profileInitial,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            if (chat.isOnline) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(WhatsAppColors.Secondary)
                        .align(Alignment.BottomEnd)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Chat Info
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = chat.time,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.lastMessage,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (chat.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(WhatsAppColors.UnreadBadge),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = chat.unreadCount.toString(),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusScreen() {
    val statuses = remember {
        listOf(
            Status(1, "Mi estado", "Toca para añadir", "YO", false),
            Status(2, "María García", "Hace 5 min", "MG", false),
            Status(3, "Juan Pérez", "Hace 30 min", "JP", true),
            Status(4, "Ana Rodríguez", "Hace 1 hora", "AR", false),
            Status(5, "Carlos Méndez", "Hace 2 horas", "CM", true),
            Status(6, "Sofía López", "Ayer", "SL", true),
            Status(7, "Pedro Martínez", "Ayer", "PM", true)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item {
            Text(
                text = "RECIENTES",
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }

        items(statuses) { status ->
            StatusItem(status = status)
            if (status.id != statuses.last().id) {
                Divider(color = Color.LightGray.copy(alpha = 0.3f))
            }
        }
    }
}

@Composable
fun StatusItem(status: Status) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Status Circle
        Box(
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .background(
                    if (status.hasViewed) Color.LightGray
                    else WhatsAppColors.Secondary
                )
                .padding(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(WhatsAppColors.Tertiary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = status.profileInitial,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = status.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = status.time,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun CallsScreen() {
    val calls = remember {
        listOf(
            Call(1, "María García", "Hoy, 10:30 AM", "outgoing", false, "MG"),
            Call(2, "Juan Pérez", "Hoy, 9:15 AM", "incoming", true, "JP"),
            Call(3, "Ana Rodríguez", "Ayer, 8:45 PM", "missed", false, "AR"),
            Call(4, "Carlos Méndez", "Ayer, 2:30 PM", "outgoing", true, "CM"),
            Call(5, "Sofía López", "19/01/2025, 6:15 PM", "incoming", false, "SL"),
            Call(6, "Pedro Martínez", "18/01/2025, 11:20 AM", "outgoing", false, "PM"),
            Call(7, "Laura Sánchez", "17/01/2025, 4:50 PM", "missed", true, "LS")
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        items(calls) { call ->
            CallItem(call = call)
            Divider(color = Color.LightGray.copy(alpha = 0.3f))
        }
    }
}

@Composable
fun CallItem(call: Call) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Picture
        Box(
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .background(WhatsAppColors.Tertiary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = call.profileInitial,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Call Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = call.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = if (call.type == "missed") Color.Red else Color.Black
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = when (call.type) {
                        "outgoing" -> Icons.Default.Call
                        "incoming" -> Icons.Default.Call
                        else -> Icons.Default.Call
                    },
                    contentDescription = call.type,
                    modifier = Modifier.size(16.dp),
                    tint = when (call.type) {
                        "missed" -> Color.Red
                        else -> Color.Gray
                    }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = call.time,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        IconButton(onClick = { }) {
            Icon(
                imageVector = if (call.isVideo) Icons.Default.Favorite else Icons.Default.Call,
                contentDescription = if (call.isVideo) "Videollamada" else "Llamada",
                tint = WhatsAppColors.Tertiary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(chat: Chat, onBack: () -> Unit) {
    val messages = remember {
        listOf(
            Message(1, "Hola! ¿Cómo estás?", "10:25 AM", false),
            Message(2, "Muy bien, gracias! ¿Y tú?", "10:26 AM", true),
            Message(3, "Todo bien por aquí también", "10:27 AM", false),
            Message(4, "¿Tienes planes para el fin de semana?", "10:28 AM", false),
            Message(5, "Aún no, pero pensaba ir al cine", "10:29 AM", true),
            Message(6, "¡Qué buena idea! ¿Qué película?", "10:30 AM", false),
            Message(7, "Todavía no lo decido, ¿tienes alguna recomendación?", "10:30 AM", true)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(WhatsAppColors.Tertiary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = chat.profileInitial,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = chat.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (chat.isOnline) "En línea" else "Últ. vez hoy ${chat.time}",
                                fontSize = 12.sp
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Email, "Videollamada", tint = Color.White)
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Call, "Llamar", tint = Color.White)
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, "Más", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WhatsAppColors.Primary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            ChatInputBar()
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(WhatsAppColors.Background)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                reverseLayout = false
            ) {
                items(messages) { message ->
                    MessageBubble(message = message)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isMe) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = if (message.isMe) 12.dp else 0.dp,
                        bottomEnd = if (message.isMe) 0.dp else 12.dp
                    )
                )
                .background(
                    if (message.isMe) WhatsAppColors.ChatBubbleMe
                    else WhatsAppColors.ChatBubbleOther
                )
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = message.text,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.time,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    if (message.isMe) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Leído",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF34B7F1)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatInputBar() {
    var message by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(25.dp))
                .background(Color(0xFFF0F0F0))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Emoji",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )

                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    if (message.isEmpty()) {
                        Text(
                            text = "Mensaje",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Adjuntar",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = "Cámara",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        FloatingActionButton(
            onClick = { },
            modifier = Modifier.size(48.dp),
            containerColor = WhatsAppColors.Secondary,
            contentColor = Color.White
        ) {
            Icon(
                imageVector = if (message.isEmpty()) Icons.Default.AccountBox else Icons.Default.Send,
                contentDescription = if (message.isEmpty()) "Voz" else "Enviar"
            )
        }
    }
} */