package mw.dannie.fumigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HymnBookApp()
        }
    }
}

@Composable
fun HymnBookApp() {

    val navController = rememberNavController()

    MaterialTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {

                composable("home") {

                    HomeScreen(
                        onSongClick = { songNumber ->
                            navController.navigate("song/$songNumber")
                        }
                    )
                }

                composable("song/{songNumber}") { backStackEntry ->

                    val songNumber =
                        backStackEntry.arguments
                            ?.getString("songNumber")
                            ?.toIntOrNull()

                    val song = SongData.songs.find {
                        it.number == songNumber
                    }

                    if (song != null) {

                        SongScreen(
                            song = song,
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    onSongClick: (Int) -> Unit
) {

    var searchText by remember {
        mutableStateOf("")
    }

    val filteredSongs = SongData.songs.filter { song ->

        val search = searchText.trim().lowercase()

        search.isEmpty() ||
                song.number.toString().contains(search) ||
                song.title.lowercase().contains(search) ||
                song.englishTitle.lowercase().contains(search)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(35.dp))

        Text(
            text = "LEMEKEZANI\nMULUNGU M'NYIMBO",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Nyimbo za Chikhristu",
            fontSize = 17.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(25.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Search songs")
            },
            placeholder = {
                Text("Number or song title")
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(25.dp))

        if (filteredSongs.isEmpty()) {

            Text(
                text = "No songs found",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

        } else {

            filteredSongs.forEach { song ->

                Button(
                    onClick = {
                        onSongClick(song.number)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .height(70.dp)
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "SONG ${song.number}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = song.title,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = song.englishTitle,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun SongScreen(
    song: Song,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        TextButton(
            onClick = onBack
        ) {
            Text("← Back")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "${song.number}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = song.title,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = song.englishTitle,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(25.dp))

        song.verses.forEachIndexed { index, verse ->

            HymnVerse(
                number = index + 1,
                text = verse
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun HymnVerse(
    number: Int,
    text: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 22.dp)
    ) {

        Text(
            text = "$number.",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = text,
            fontSize = 18.sp,
            lineHeight = 27.sp
        )
    }
}
