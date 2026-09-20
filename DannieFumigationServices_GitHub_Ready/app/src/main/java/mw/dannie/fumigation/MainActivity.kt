package mw.dannie.fumigation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF2457A6),
            secondary = Color(0xFF6A4BC3),
            background = Color(0xFFF5F7FB),
            surface = Color.White
        )
    ) {

        Scaffold(
            containerColor = Color(0xFFF5F7FB),

            bottomBar = {

                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {

                    NavigationBarItem(
                        selected = navController
                            .currentBackStackEntry
                            ?.destination
                            ?.route == "home",

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
                        selected = navController
                            .currentBackStackEntry
                            ?.destination
                            ?.route == "favorites",

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
                        selected = navController
                            .currentBackStackEntry
                            ?.destination
                            ?.route == "about",

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
                            navController.navigate(
                                "song/$songNumber"
                            )
                        }
                    )
                }

                composable("favorites") {

                    FavoritesScreen(
                        favoriteSongs = favoriteSongs,
                        onSongClick = { songNumber ->
                            navController.navigate(
                                "song/$songNumber"
                            )
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

                            isFavorite =
                                favoriteSongs.contains(song.number),

                            onFavoriteClick = {

                                val newFavorites =
                                    if (
                                        favoriteSongs.contains(
                                            song.number
                                        )
                                    ) {
                                        favoriteSongs - song.number
                                    } else {
                                        favoriteSongs + song.number
                                    }

                                saveFavorites(newFavorites)
                            },

                            onPreviousClick = {

                                val currentIndex =
                                    SongData.songs.indexOfFirst {
                                        it.number == song.number
                                    }

                                if (currentIndex > 0) {

                                    val previousSong =
                                        SongData.songs[
                                            currentIndex - 1
                                        ]

                                    navController.navigate(
                                        "song/${previousSong.number}"
                                    ) {
                                        popUpTo(
                                            "song/${song.number}"
                                        ) {
                                            inclusive = true
                                        }
                                    }
                                }
                            },

                            onNextClick = {

                                val currentIndex =
                                    SongData.songs.indexOfFirst {
                                        it.number == song.number
                                    }

                                if (
                                    currentIndex >= 0 &&
                                    currentIndex <
                                    SongData.songs.lastIndex
                                ) {

                                    val nextSong =
                                        SongData.songs[
                                            currentIndex + 1
                                        ]

                                    navController.navigate(
                                        "song/${nextSong.number}"
                                    ) {
                                        popUpTo(
                                            "song/${song.number}"
                                        ) {
                                            inclusive = true
                                        }
                                    }
                                }
                            },

                            hasPrevious =
                                SongData.songs.indexOfFirst {
                                    it.number == song.number
                                } > 0,

                            hasNext =
                                SongData.songs.indexOfFirst {
                                    it.number == song.number
                                } < SongData.songs.lastIndex,

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
    favoriteSongs: Set<Int>,
    onSongClick: (Int) -> Unit
) {

    var searchText by remember {
        mutableStateOf("")
    }

    val filteredSongs = SongData.songs.filter { song ->

        val search = searchText
            .trim()
            .lowercase()

        search.isEmpty() ||
                song.number
                    .toString()
                    .contains(search) ||
                song.title
                    .lowercase()
                    .contains(search) ||
                song.englishTitle
                    .lowercase()
                    .contains(search)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF173F7A),
                            Color(0xFF3F72B5),
                            Color(0xFF7DA7D8)
                        )
                    )
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 18.dp,
                        bottom = 26.dp
                    )
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "SHILOH SDB CHURCH",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Row(
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Color.White.copy(
                                            alpha = 0.95f
                                        )
                                    ),
                                contentAlignment =
                                    Alignment.Center
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Default.Info,
                                    contentDescription =
                                        "Open Bible",
                                    tint =
                                        Color(0xFF2457A6),
                                    modifier =
                                        Modifier.size(30.dp)
                                )
                            }

                            Spacer(
                                modifier = Modifier.width(12.dp)
                            )

                            Text(
                                text = "SHILOH",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.width(170.dp),
                        horizontalAlignment =
                            Alignment.End
                    ) {

                        Text(
                            text = "Chibvumbulutso 14:12",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        )

                        Spacer(
                            modifier = Modifier.height(5.dp)
                        )

                        Text(
                            text =
                                "Pano pali cipiriro ca oyera mtima,\n" +
                                "ca iwo akusunga malamulo a Mulungu,\n" +
                                "ndi cikhulupiriro ca Yesu.",
                            color = Color.White,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            textAlign = TextAlign.End
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(22.dp)
                )

                Text(
                    text =
                        "LEMEKEZANI\nMULUNGU M'NYIMBO",
                    color = Color.White,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 31.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 18.dp,
                    end = 18.dp
                )
        ) {

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            OutlinedTextField(
                value = searchText,

                onValueChange = {
                    searchText = it
                },

                modifier = Modifier.fillMaxWidth(),

                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },

                placeholder = {
                    Text("Sankhani nyimbo...")
                },

                singleLine = true,

                shape = RoundedCornerShape(18.dp),

                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedContainerColor =
                            Color.White,
                        unfocusedContainerColor =
                            Color.White,
                        focusedBorderColor =
                            Color(0xFF2457A6),
                        unfocusedBorderColor =
                            Color(0xFFD7DCE5)
                    )
            )

            Spacer(
                modifier = Modifier.height(25.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = "NYIMBO ZONSE",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF202A38),
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color(0xFFE4ECFA)
                ) {

                    Text(
                        text = "ZONSE 413",
                        modifier = Modifier.padding(
                            horizontal = 13.dp,
                            vertical = 7.dp
                        ),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2457A6)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            if (filteredSongs.isEmpty()) {

                Spacer(
                    modifier = Modifier.height(30.dp)
                )

                Text(
                    text = "Palibe nyimbo yopezeka.",
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

            } else {

                filteredSongs.forEachIndexed {
                        index,
                        song ->

                    SongCard(
                        song = song,
                        isFavorite =
                            favoriteSongs.contains(
                                song.number
                            ),
                        index = index,
                        onClick = {
                            onSongClick(song.number)
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(25.dp)
            )
        }
    }
}

@Composable
fun SongCard(
    song: Song,
    isFavorite: Boolean,
    index: Int,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 15.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        Color(0xFFE8EFFB)
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = song.number.toString(),
                    color = Color(0xFF2457A6),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = song.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF202A38),
                    maxLines = 2
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = song.englishTitle,
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280),
                    maxLines = 1
                )
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            if (isFavorite) {

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Favorite",
                    tint = Color(0xFFFFB300),
                    modifier = Modifier.size(22.dp)
                )

            } else {

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Open song",
                    tint = Color(0xFF2457A6),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun FavoritesScreen(
    favoriteSongs: Set<Int>,
    onSongClick: (Int) -> Unit
) {

    val favoriteSongList =
        SongData.songs.filter {
            favoriteSongs.contains(it.number)
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(18.dp)
    ) {

        Text(
            text = "ZOKONDA",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF202A38)
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = "Nyimbo zomwe mwasunga",
            fontSize = 14.sp,
            color = Color(0xFF6B7280)
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        if (favoriteSongList.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.Favorite,
                        contentDescription =
                            "No favorites",
                        tint =
                            Color(0xFFB0B7C3),
                        modifier =
                            Modifier.size(48.dp)
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text =
                            "Palibe nyimbo zomwe mwasunga.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF555D6B)
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text =
                            "Dinani nyenyezi pa nyimbo kuti muiike pano.",
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF7A828F)
                    )
                }
            }

        } else {

            favoriteSongList.forEachIndexed {
                    index,
                    song ->

                SongCard(
                    song = song,
                    isFavorite = true,
                    index = index,
                    onClick = {
                        onSongClick(song.number)
                    }
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )
    }
}

@Composable
fun AboutScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(20.dp)
    ) {

        Text(
            text = "ZA APP",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF202A38)
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier.padding(22.dp)
            ) {

                Text(
                    text =
                        "LEMEKEZANI MULUNGU M'NYIMBO",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2457A6)
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Text(
                    text = "SHILOH SDB CHURCH",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text =
                        "Buku la nyimbo la Chichewa lokonzedwa " +
                        "kuti likhale losavuta kuwerenga ndi " +
                        "kusaka nyimbo.",
                    fontSize = 15.sp,
                    lineHeight = 23.sp,
                    color = Color(0xFF555D6B)
                )

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                Text(
                    text =
                        "Mavesi a m'Baibulo ndi nyimbo zili " +
                        "mwa dongosolo la buku la nyimbo.",
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier.padding(22.dp)
            ) {

                Text(
                    text = "ZOKONDA",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2457A6)
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text =
                        "Nyimbo zomwe mwasankha kukhala zokonda " +
                        "zimasungidwa pa foni yanu ndipo zimapitiriza " +
                        "kupezeka mukatsegulanso app.",
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )
    }
}

@Composable
fun SongScreen(
    song: Song,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    hasPrevious: Boolean,
    hasNext: Boolean,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFF2457A6)
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 14.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = "‹",
                color = Color.White,
                fontSize = 34.sp,
                modifier = Modifier
                    .clickable {
                        onBack()
                    }
                    .padding(
                        horizontal = 8.dp
                    )
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "NYIMBO ${song.number}",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = song.title,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
            }

            IconButton(
                onClick = {
                    onFavoriteClick()
                }
            ) {

                Icon(
                    imageVector =
                        if (isFavorite) {
                            Icons.Default.Star
                        } else {
                            Icons.Default.Favorite
                        },
                    contentDescription = "Favorite",
                    tint =
                        if (isFavorite) {
                            Color(0xFFFFD54F)
                        } else {
                            Color.White
                        }
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 22.dp,
                    bottom = 25.dp
                )
        ) {

            Text(
                text = song.title,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF202A38),
                lineHeight = 32.sp
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Text(
                text = song.englishTitle,
                fontSize = 15.sp,
                color = Color(0xFF6B7280)
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            HorizontalDivider()

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = song.verses.joinToString(
                    separator = "\n\n"
                ),
                fontSize = 18.sp,
                lineHeight = 29.sp,
                color = Color(0xFF202A38)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                    onPreviousClick()
                },
                enabled = hasPrevious,
                shape = RoundedCornerShape(14.dp)
            ) {

                Text(
                    text = "‹  YAM'MBUYOMO"
                )
            }

            Surface(
                shape = RoundedCornerShape(50.dp),
                color = Color(0xFFE8EFFB)
            ) {

                Text(
                    text = "${song.number}",
                    modifier = Modifier.padding(
                        horizontal = 15.dp,
                        vertical = 8.dp
                    ),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2457A6)
                )
            }

            Button(
                onClick = {
                    onNextClick()
                },
                enabled = hasNext,
                shape = RoundedCornerShape(14.dp)
            ) {

                Text(
                    text = "YOTSATIRA  ›"
                )
            }
        }
    }
}
