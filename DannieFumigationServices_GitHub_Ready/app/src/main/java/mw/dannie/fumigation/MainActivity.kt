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
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HymnBookApp()
        }
    }
}

/* ------------------------------------------------ */
/* COLORS */
/* ------------------------------------------------ */

private val PrimaryBlue = Color(0xFF173F7A)
private val Blue = Color(0xFF2457A6)
private val LightBlue = Color(0xFFEAF1FC)
private val Background = Color(0xFFF5F7FB)
private val DarkText = Color(0xFF182230)
private val GrayText = Color(0xFF697586)
private val Gold = Color(0xFFFFB300)

/* ------------------------------------------------ */
/* MAIN APP */
/* ------------------------------------------------ */

@Composable
fun HymnBookApp() {

    val context = androidx.compose.ui.platform.LocalContext.current

    val navController = rememberNavController()

    /*
     * FAVORITES ARE SAVED LOCALLY ON THE PHONE
     */

    val preferences = remember {

        context.getSharedPreferences(
            "hymn_book_preferences",
            Context.MODE_PRIVATE
        )
    }

    var favoriteSongs by remember {

        mutableStateOf(

            preferences
                .getStringSet(
                    "favorite_songs",
                    emptySet()
                )
                ?.mapNotNull {
                    it.toIntOrNull()
                }
                ?.toSet()
                ?: emptySet()
        )
    }

    fun saveFavorites(
        favorites: Set<Int>
    ) {

        preferences
            .edit()
            .putStringSet(
                "favorite_songs",
                favorites
                    .map {
                        it.toString()
                    }
                    .toSet()
            )
            .apply()

        favoriteSongs = favorites
    }

    MaterialTheme(

        colorScheme = lightColorScheme(

            primary = Blue,

            secondary = Color(0xFF6A4BC3),

            background = Background,

            surface = Color.White
        )

    ) {

        val currentBackStackEntry by
        navController
            .currentBackStackEntryAsState()

        val currentRoute =
            currentBackStackEntry
                ?.destination
                ?.route

        Scaffold(

            containerColor = Background,

            bottomBar = {

                if (
                    currentRoute == "home" ||
                    currentRoute == "favorites" ||
                    currentRoute == "about"
                ) {

                    BottomNavigationBar(

                        currentRoute = currentRoute,

                        onHomeClick = {

                            navController.navigate(
                                "home"
                            ) {

                                popUpTo(
                                    "home"
                                ) {

                                    inclusive = false
                                }

                                launchSingleTop = true
                            }
                        },

                        onFavoritesClick = {

                            navController.navigate(
                                "favorites"
                            ) {

                                launchSingleTop = true
                            }
                        },

                        onAboutClick = {

                            navController.navigate(
                                "about"
                            ) {

                                launchSingleTop = true
                            }
                        }
                    )
                }
            }

        ) { paddingValues ->

            NavHost(

                navController = navController,

                startDestination = "home",

                modifier =
                    Modifier.padding(
                        paddingValues
                    )

            ) {

                /* ------------------------------------------------ */
                /* HOME */
                /* ------------------------------------------------ */

                composable("home") {

                    HomeScreen(

                        favoriteSongs =
                            favoriteSongs,

                        onSongClick = { songNumber ->

                            navController.navigate(
                                "song/$songNumber"
                            )
                        }
                    )
                }

                /* ------------------------------------------------ */
                /* FAVORITES */
                /* ------------------------------------------------ */

                composable("favorites") {

                    FavoritesScreen(

                        favoriteSongs =
                            favoriteSongs,

                        onSongClick = { songNumber ->

                            navController.navigate(
                                "song/$songNumber"
                            )
                        }
                    )
                }

                /* ------------------------------------------------ */
                /* ABOUT */
                /* ------------------------------------------------ */

                composable("about") {

                    AboutScreen()
                }

                /* ------------------------------------------------ */
                /* SONG */
                /* ------------------------------------------------ */

                composable(
                    "song/{songNumber}"
                ) { backStackEntry ->

                    val songNumber =
                        backStackEntry
                            .arguments
                            ?.getString(
                                "songNumber"
                            )
                            ?.toIntOrNull()

                    val song =
                        SongData.songs.find {
                            it.number == songNumber
                        }

                    if (song != null) {

                        val currentIndex =
                            SongData.songs
                                .indexOfFirst {
                                    it.number ==
                                        song.number
                                }

                        SongScreen(

                            song = song,

                            isFavorite =
                                favoriteSongs.contains(
                                    song.number
                                ),

                            onFavoriteClick = {

                                val newFavorites =

                                    if (
                                        favoriteSongs.contains(
                                            song.number
                                        )
                                    ) {

                                        favoriteSongs -
                                            song.number

                                    } else {

                                        favoriteSongs +
                                            song.number
                                    }

                                saveFavorites(
                                    newFavorites
                                )
                            },

                            onPreviousClick = {

                                if (
                                    currentIndex > 0
                                ) {

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
                                currentIndex > 0,

                            hasNext =
                                currentIndex >= 0 &&
                                currentIndex <
                                SongData.songs.lastIndex,

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

/* ------------------------------------------------ */
/* HOME HEADER */
/* ------------------------------------------------ */

@Composable
fun HomeImageHeader() {

    Box(

        modifier =
            Modifier
                .fillMaxWidth()
                .height(230.dp)
                .background(
                    PrimaryBlue
                )

    ) {

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 22.dp,
                        vertical = 22.dp
                    ),

            verticalArrangement =
                Arrangement.Bottom
        ) {

            Text(

                text =
                    "SHILOH SDB CHURCH",

                fontSize =
                    15.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color.White
            )

            Spacer(
                modifier =
                    Modifier.height(5.dp)
            )

            Text(

                text =
                    "LEMEKEZANI MULUNGU",

                fontSize =
                    26.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                color =
                    Color.White
            )

            Text(

                text =
                    "M'NYIMBO",

                fontSize =
                    31.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                color =
                    Color.White
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(

                text =
                    "Nyimbo za kupembedza ndi kutamanda Mulungu",

                fontSize =
                    13.sp,

                color =
                    Color.White.copy(
                        alpha = 0.88f
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            Surface(

                shape =
                    RoundedCornerShape(
                        30.dp
                    ),

                color =
                    Color.White.copy(
                        alpha = 0.16f
                    )

            ) {

                Text(

                    text =
                        "Chibvumbulutso 14:12",

                    modifier =
                        Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 8.dp
                        ),

                    fontSize =
                        12.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Color.White
                )
            }
        }
    }
}

/* ------------------------------------------------ */
/* BOTTOM NAVIGATION */
/* ------------------------------------------------ */

@Composable
fun BottomNavigationBar(

    currentRoute: String?,

    onHomeClick: () -> Unit,

    onFavoritesClick: () -> Unit,

    onAboutClick: () -> Unit

) {

    NavigationBar(

        containerColor =
            Color.White,

        tonalElevation =
            8.dp

    ) {

        NavigationBarItem(

            selected =
                currentRoute == "home",

            onClick =
                onHomeClick,

            icon = {

                Icon(

                    imageVector =
                        Icons.Default.Home,

                    contentDescription =
                        "Home",

                    modifier =
                        Modifier.size(
                            27.dp
                        )
                )
            },

            label = {

                Text(

                    text =
                        "Home",

                    fontWeight =
                        FontWeight.Bold
                )
            }
        )

        NavigationBarItem(

            selected =
                currentRoute == "favorites",

            onClick =
                onFavoritesClick,

            icon = {

                Icon(

                    imageVector =
                        Icons.Default.Favorite,

                    contentDescription =
                        "Favorites",

                    modifier =
                        Modifier.size(
                            27.dp
                        )
                )
            },

            label = {

                Text(

                    text =
                        "Favorites",

                    fontWeight =
                        FontWeight.Bold
                )
            }
        )

        NavigationBarItem(

            selected =
                currentRoute == "about",

            onClick =
                onAboutClick,

            icon = {

                Icon(

                    imageVector =
                        Icons.Default.Info,

                    contentDescription =
                        "About",

                    modifier =
                        Modifier.size(
                            27.dp
                        )
                )
            },

            label = {

                Text(

                    text =
                        "About",

                    fontWeight =
                        FontWeight.Bold
                )
            }
        )
    }
}

/* ------------------------------------------------ */
/* HOME SCREEN */
/* ------------------------------------------------ */

@Composable
fun HomeScreen(

    favoriteSongs: Set<Int>,

    onSongClick: (Int) -> Unit

) {

    var searchText by remember {

        mutableStateOf("")
    }

    val filteredSongs =
        SongData.songs.filter { song ->

            val search =
                searchText
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

        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )

    ) {

        HomeImageHeader()

        Column(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 18.dp
                    )

        ) {

            Spacer(
                modifier =
                    Modifier.height(15.dp)
            )

            HomeSearchBox(

                searchText =
                    searchText,

                onSearchChange = {
                    searchText = it
                }
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically

            ) {

                Row(

                    modifier =
                        Modifier.weight(1f),

                    verticalAlignment =
                        Alignment.CenterVertically

                ) {

                    Text(

                        text =
                            "♫",

                        fontSize =
                            38.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Blue
                    )

                    Spacer(
                        modifier =
                            Modifier.width(8.dp)
                    )

                    Text(

                        text =
                            "NYIMBO ZONSE",

                        fontSize =
                            25.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            Blue
                    )
                }

                Surface(

                    shape =
                        RoundedCornerShape(
                            30.dp
                        ),

                    color =
                        Color(0xFFE3ECFC)

                ) {

                    Text(

                        text =
                            "413",

                        modifier =
                            Modifier.padding(
                                horizontal = 18.dp,
                                vertical = 9.dp
                            ),

                        fontSize =
                            15.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            PrimaryBlue
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            if (filteredSongs.isEmpty()) {

                EmptySearchState()

            } else {

                filteredSongs.forEachIndexed {

                    index,
                    song ->

                    NewSongCard(

                        song =
                            song,

                        isFavorite =
                            favoriteSongs.contains(
                                song.number
                            ),

                        colorIndex =
                            index,

                        onClick = {

                            onSongClick(
                                song.number
                            )
                        }
                    )

                    Spacer(
                        modifier =
                            Modifier.height(9.dp)
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(25.dp)
            )
        }
    }
}

/* ------------------------------------------------ */
/* SEARCH */
/* ------------------------------------------------ */

@Composable
fun HomeSearchBox(

    searchText: String,

    onSearchChange: (String) -> Unit

) {

    OutlinedTextField(

        value =
            searchText,

        onValueChange =
            onSearchChange,

        modifier =
            Modifier.fillMaxWidth(),

        leadingIcon = {

            Icon(

                imageVector =
                    Icons.Default.Search,

                contentDescription =
                    "Search",

                tint =
                    Blue,

                modifier =
                    Modifier.size(
                        30.dp
                    )
            )
        },

        placeholder = {

            Text(

                text =
                    "Sankhani nyimbo...",

                fontSize =
                    16.sp,

                fontWeight =
                    FontWeight.Medium
            )
        },

        singleLine = true,

        shape =
            RoundedCornerShape(
                30.dp
            ),

        colors =
            OutlinedTextFieldDefaults.colors(

                focusedContainerColor =
                    Color.White,

                unfocusedContainerColor =
                    Color.White,

                focusedBorderColor =
                    Color(0xFFB8CBEA),

                unfocusedBorderColor =
                    Color(0xFFD4DFF0)
            )
    )
}

/* ------------------------------------------------ */
/* SONG CARD */
/* ------------------------------------------------ */

@Composable
fun NewSongCard(

    song: Song,

    isFavorite: Boolean,

    colorIndex: Int,

    onClick: () -> Unit

) {

    val cardColors = listOf(

        Color(0xFF7135D8),

        Color(0xFF1789E8),

        Color(0xFF29A94B),

        Color(0xFFFFA000),

        Color(0xFFF52F48),

        Color(0xFF18AEBB),

        Color(0xFF7B4ACB),

        Color(0xFF2B82D9)
    )

    val numberColor =
        cardColors[
            colorIndex %
                cardColors.size
        ]

    Card(

        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                },

        shape =
            RoundedCornerShape(
                17.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    2.dp
            )

    ) {

        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(86.dp),

            verticalAlignment =
                Alignment.CenterVertically

        ) {

            Box(

                modifier =
                    Modifier
                        .width(7.dp)
                        .fillMaxHeight()
                        .background(
                            numberColor
                        )
            )

            Spacer(
                modifier =
                    Modifier.width(10.dp)
            )

            Box(

                modifier =
                    Modifier
                        .size(58.dp)
                        .clip(
                            RoundedCornerShape(
                                13.dp
                            )
                        )
                        .background(
                            numberColor
                        ),

                contentAlignment =
                    Alignment.Center

            ) {

                Text(

                    text =
                        song.number.toString(),

                    fontSize =
                        25.sp,

                    fontWeight =
                        FontWeight.ExtraBold,

                    color =
                        Color.White
                )
            }

            Spacer(
                modifier =
                    Modifier.width(15.dp)
            )

            Column(

                modifier =
                    Modifier.weight(1f)

            ) {

                Text(

                    text =
                        song.title,

                    fontSize =
                        16.sp,

                    fontWeight =
                        FontWeight.ExtraBold,

                    color =
                        PrimaryBlue,

                    maxLines =
                        2
                )

                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )

                Text(

                    text =
                        song.englishTitle,

                    fontSize =
                        13.sp,

                    fontWeight =
                        FontWeight.Medium,

                    color =
                        Color(0xFF657795),

                    maxLines =
                        1
                )
            }

            Icon(

                imageVector =
                    if (isFavorite) {

                        Icons.Default.Star

                    } else {

                        Icons.Default.ArrowForward
                    },

                contentDescription =
                    if (isFavorite) {
                        "Favorite"
                    } else {
                        "Open song"
                    },

                tint =
                    if (isFavorite) {
                        Gold
                    } else {
                        Color(0
