package mw.dannie.fumigation

import android.os.Bundle
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
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

    val context = androidx.compose.ui.platform.LocalContext.current
    val navController = rememberNavController()

    val preferences = remember {
        context.getSharedPreferences(
            "hymn_book_preferences",
            Context.MODE_PRIVATE
        )
    }

    var favoriteSongs by remember {
        mutableStateOf(
            preferences
                .getStringSet("favorite_songs", emptySet())
                ?.mapNotNull { it.toIntOrNull() }
                ?.toSet()
                ?: emptySet()
        )
    }

    fun saveFavorites(favorites: Set<Int>) {

        preferences.edit()
            .putStringSet(
                "favorite_songs",
                favorites.map { it.toString() }.toSet()
            )
            .apply()

        favoriteSongs = favorites
    }

    MaterialTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            Scaffold(
                bottomBar = {

                    NavigationBar {

                        NavigationBarItem(
                            selected = navController.currentBackStackEntry?.destination?.route == "home",
                            onClick = {
                                navController.navigate("home") {
                                    popUpTo("home") {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Home"
                                )
                            },
                            label = {
                                Text("Home")
                            }
                        )

                        NavigationBarItem(
                            selected = navController.currentBackStackEntry?.destination?.route == "favorites",
                            onClick = {
                                navController.navigate("favorites") {
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Favorites"
                                )
                            },
                            label = {
                                Text("Favorites")
                            }
                        )

                        NavigationBarItem(
                            selected = navController.currentBackStackEntry?.destination?.route == "about",
                            onClick = {
                                navController.navigate("about") {
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "About"
                                )
                            },
                            label = {
                                Text("About")
                            }
                        )
                    }
                }
            ) { paddingValues ->

                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(paddingValues)
                ) {

                    composable("home") {

                        HomeScreen(
                            favoriteSongs = favoriteSongs,
                            onSongClick = { songNumber ->
                                navController.navigate("song/$songNumber")
                            }
                        )
                    }

                    composable("favorites") {

                        FavoritesScreen(
                            favoriteSongs = favoriteSongs,
                            onSongClick = { songNumber ->
                                navController.navigate("song/$songNumber")
                            }
                        )
                    }

                    composable("about") {

                        AboutScreen()
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
                                isFavorite = favoriteSongs.contains(song.number),
                                onFavoriteClick = {

                                    val newFavorites =
                                        if (favoriteSongs.contains(song.number)) {
                                            favoriteSongs - song.number
                                        } else {
                                            favoriteSongs + song.number
                                        }

                                    saveFavorites(newFavorites)
                                },
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
}

@Composable
fun HomeScreen(
    favoriteSongs: Set<Int>,
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

        Spacer(modifier = Modifier.height(25.dp))

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

        Text(
            text = "ALL SONGS",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

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
                            text = if (favoriteSongs.contains(song.number)) {
                                "⭐ SONG ${song.number}"
                            } else {
                                "SONG ${song.number}"
                            },
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
fun FavoritesScreen(
    favoriteSongs: Set<Int>,
    onSongClick: (Int) -> Unit
) {

    val favoriteSongList = SongData.songs.filter {
        favoriteSongs.contains(it.number)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Text(
            text = "❤️ FAVORITES",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(25.dp))

        if (favoriteSongList.isEmpty()) {

            Text(
                text = "No favorite songs yet.",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

        } else {

            favoriteSongList.forEach { song ->

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
                            text = "⭐ SONG ${song.number}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = song.title,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AboutScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "ABOUT",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "LEMEKEZANI MULUNGU M'NYIMBO",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Nyimbo za Chikhristu",
            fontSize = 17.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "This app is a digital hymn book containing Christian songs for worship and personal devotion.",
            fontSize = 17.sp,
            lineHeight = 26.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Version 1.0",
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SongScreen(
    song: Song,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
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

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = onFavoriteClick,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = if (isFavorite) {
                    "⭐ Remove from Favorites"
                } else {
                    "☆ Add to Favorites"
                },
                fontSize = 16.sp
            )
        }

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
