package mw.dannie.fumigation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


// ============================================================
// MAIN ACTIVITY
// ============================================================

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HymnBookApp()
        }
    }
}


// ============================================================
// COLORS
// ============================================================

private val PrimaryBlue = Color(0xFF173F7A)
private val Blue = Color(0xFF2457A6)
private val LightBlue = Color(0xFFEAF1FC)
private val Background = Color(0xFFF5F7FB)
private val DarkText = Color(0xFF182230)
private val GrayText = Color(0xFF697586)
private val Gold = Color(0xFFFFB300)


// ============================================================
// MAIN APP
// ============================================================

@Composable
fun HymnBookApp() {

    val context = androidx.compose.ui.platform.LocalContext.current

    val navController = rememberNavController()

    // --------------------------------------------------------
    // LOCAL FAVORITES
    // --------------------------------------------------------

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
                ?.mapNotNull { it.toIntOrNull() }
                ?.toSet()
                ?: emptySet()
        )
    }

    fun saveFavorites(favorites: Set<Int>) {

        preferences
            .edit()
            .putStringSet(
                "favorite_songs",
                favorites.map { it.toString() }.toSet()
            )
            .apply()

        favoriteSongs = favorites
    }

    // --------------------------------------------------------
    // THEME
    // --------------------------------------------------------

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Blue,
            secondary = Color(0xFF6A4BC3),
            background = Background,
            surface = Color.White
        )
    ) {

        val currentBackStackEntry by
            navController.currentBackStackEntryAsState()

        val currentRoute =
            currentBackStackEntry?.destination?.route

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

                            navController.navigate("home") {

                                popUpTo("home") {
                                    inclusive = false
                                }

                                launchSingleTop = true
                            }
                        },

                        onFavoritesClick = {

                            navController.navigate("favorites") {
                                launchSingleTop = true
                            }
                        },

                        onAboutClick = {

                            navController.navigate("about") {
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
                modifier = Modifier.padding(paddingValues)
            ) {

                // =================================================
                // HOME
                // =================================================

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

                // =================================================
                // FAVORITES
                // =================================================

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

                // =================================================
                // ABOUT
                // =================================================

                composable("about") {

                    AboutScreen()
                }

                // =================================================
                // SONG
                // =================================================

                composable(
                    route = "song/{songNumber}"
                ) { backStackEntry ->

                    val songNumber =
                        backStackEntry
                            .arguments
                            ?.getString("songNumber")
                            ?.toIntOrNull()

                    val song =
                        SongData.songs.find {
                            it.number == songNumber
                        }

                    if (song != null) {

                        val currentIndex =
                            SongData.songs.indexOfFirst {
                                it.number == song.number
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

                                        favoriteSongs - song.number

                                    } else {

                                        favoriteSongs + song.number
                                    }

                                saveFavorites(newFavorites)
                            },

                            onPreviousClick = {

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


// ============================================================
// SHILOH HOME HEADER
// ============================================================

@Composable
fun HomeImageHeader() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF102F5C),
                        Color(0xFF2457A6),
                        Color(0xFF4C78B8)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ------------------------------------------------
            // SHILOH LOGO
            // ------------------------------------------------

            Image(
                painter = painterResource(
                    id = R.drawable.shiloh_launcher
                ),
                contentDescription = "Shiloh Mission Logo",
                modifier = Modifier
                    .size(92.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // ------------------------------------------------
            // CHURCH NAME
            // ------------------------------------------------

            Text(
                text = "SHILOH SDB CHURCH",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            // ------------------------------------------------
            // APP TITLE
            // ------------------------------------------------

            Text(
                text = "LEMEKEZANI MULUNGU M'NYIMBO",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
        }
    }
}


// ============================================================
// BOTTOM NAVIGATION
// ============================================================

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onAboutClick: () -> Unit
) {

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {

        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = onHomeClick,

            icon = {

                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(27.dp)
                )
            },

            label = {

                Text(
                    text = "Home",
                    fontWeight = FontWeight.Bold
                )
            }
        )

        NavigationBarItem(
            selected = currentRoute == "favorites",
            onClick = onFavoritesClick,

            icon = {

                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorites",
                    modifier = Modifier.size(27.dp)
                )
            },

            label = {

                Text(
                    text = "Favorites",
                    fontWeight = FontWeight.Bold
                )
            }
        )

        NavigationBarItem(
            selected = currentRoute == "about",
            onClick = onAboutClick,

            icon = {

                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "About",
                    modifier = Modifier.size(27.dp)
                )
            },

            label = {

                Text(
                    text = "About",
                    fontWeight = FontWeight.Bold
                )
            }
        )
    }
}


// ============================================================
// HOME SCREEN
// ============================================================

@Composable
fun HomeScreen(
    favoriteSongs: Set<Int>,
    onSongClick: (Int) -> Unit
) {

    var searchText by remember {
        mutableStateOf("")
    }

    val search =
        searchText
            .trim()
            .lowercase()

    val filteredSongs =
        SongData.songs.filter { song ->

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

        // -----------------------------------------------------
        // HEADER
        // -----------------------------------------------------

        HomeImageHeader()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp
                )
        ) {

            Spacer(
                modifier = Modifier.height(15.dp)
            )

            // -------------------------------------------------
            // SEARCH
            // -------------------------------------------------

            HomeSearchBox(
                searchText = searchText,

                onSearchChange = {
                    searchText = it
                }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // -------------------------------------------------
            // SONG HEADER
            // -------------------------------------------------

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = "♫",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        color = Blue
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = "NYIMBO ZONSE",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Blue
                    )
                }

                Surface(
                    shape = RoundedCornerShape(30.dp),
                    color = Color(0xFFE3ECFC)
                ) {

                    Text(
                        text = SongData.songs.size.toString(),

                        modifier = Modifier.padding(
                            horizontal = 18.dp,
                            vertical = 9.dp
                        ),

                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // -------------------------------------------------
            // SONG LIST
            // -------------------------------------------------

            if (filteredSongs.isEmpty()) {

                EmptySearchState()

            } else {

                filteredSongs.forEachIndexed { index, song ->

                    NewSongCard(
                        song = song,

                        isFavorite =
                            favoriteSongs.contains(
                                song.number
                            ),

                        colorIndex = index,

                        onClick = {
                            onSongClick(song.number)
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(9.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(25.dp)
            )
        }
    }
}


// ============================================================
// SEARCH BOX
// ============================================================

@Composable
fun HomeSearchBox(
    searchText: String,
    onSearchChange: (String) -> Unit
) {

    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchChange,

        modifier = Modifier.fillMaxWidth(),

        leadingIcon = {

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Blue,
                modifier = Modifier.size(30.dp)
            )
        },

        placeholder = {

            Text(
                text = "Sankhani nyimbo...",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        },

        singleLine = true,

        shape = RoundedCornerShape(30.dp),

        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color(0xFFB8CBEA),
            unfocusedBorderColor = Color(0xFFD4DFF0)
        )
    )
}


// ============================================================
// SONG CARD
// ============================================================

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
            colorIndex % cardColors.size
        ]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(17.dp),

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
                .height(86.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .width(7.dp)
                    .fillMaxHeight()
                    .background(numberColor)
            )

            Spacer(
                modifier = Modifier.width(10.dp)
            )

            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(
                        RoundedCornerShape(13.dp)
                    )
                    .background(numberColor),

                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = song.number.toString(),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            Spacer(
                modifier = Modifier.width(15.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = song.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryBlue,
                    maxLines = 2
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = song.englishTitle,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF657795),
                    maxLines = 1
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
                        Color(0xFF526B93)
                    },

                modifier = Modifier
                    .padding(end = 15.dp)
                    .size(25.dp)
            )
        }
    }
}


// ============================================================
// EMPTY SEARCH STATE
// ============================================================

@Composable
fun EmptySearchState() {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(20.dp),

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
                imageVector = Icons.Default.Search,
                contentDescription = "No results",
                tint = Color(0xFF9AA4B2),
                modifier = Modifier.size(45.dp)
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Palibe nyimbo yopezeka.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "Yesani nambala kapena dzina lina.",
                fontSize = 13.sp,
                color = GrayText,
                textAlign = TextAlign.Center
            )
        }
    }
}


// ============================================================
// FAVORITES SCREEN
// ============================================================

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

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "ZOKONDA",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = DarkText
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "Nyimbo zomwe mwasunga",
            fontSize = 14.sp,
            color = GrayText
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),

            shape = RoundedCornerShape(18.dp),

            color = LightBlue
        ) {

            Row(
                modifier = Modifier.padding(18.dp),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color.White),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorites",
                        tint = Blue,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(14.dp)
                )

                Column {

                    Text(
                        text =
                            favoriteSongList.size.toString(),

                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Blue
                    )

                    Text(
                        text = "nyimbo zokondedwa",
                        fontSize = 12.sp,
                        color = GrayText
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        if (favoriteSongList.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(20.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "No favorites",
                        tint = Color(0xFFC3CAD4),
                        modifier = Modifier.size(55.dp)
                    )

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    Text(
                        text =
                            "Palibe nyimbo zokondedwa.",

                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier.height(7.dp)
                    )

                    Text(
                        text =
                            "Tsegulani nyimbo ndikudina nyenyezi kuti muyisunge pano.",

                        fontSize = 13.sp,
                        color = GrayText,
                        textAlign = TextAlign.Center
                    )
                }
            }

        } else {

            favoriteSongList.forEachIndexed { index, song ->

                NewSongCard(
                    song = song,
                    isFavorite = true,
                    colorIndex = index,

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


// ============================================================
// ABOUT SCREEN
// ============================================================

@Composable
fun AboutScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(18.dp)
    ) {

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "ZA APP",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = DarkText
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "Za buku la nyimbo",
            fontSize = 14.sp,
            color = GrayText
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // -----------------------------------------------------
        // SHILOH CARD
        // -----------------------------------------------------

        Card(
            modifier = Modifier.fillMaxWidth(),

            shape = RoundedCornerShape(24.dp),

            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 25.dp,
                        horizontal = 18.dp
                    ),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.shiloh_launcher
                    ),
                    contentDescription = "Shiloh Mission Logo",
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Text(
                    text = "SHILOH SDB CHURCH",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryBlue
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "LEMEKEZANI MULUNGU M'NYIMBO",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blue,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        // -----------------------------------------------------
        // ABOUT CARDS
        // -----------------------------------------------------

        AboutCard(
            title = "ZA BUKU",

            text =
                "Buku ili lili ndi nyimbo za Chichewa " +
                "zokonzedwa kuti zikhale zosavuta kusaka, " +
                "kuwerenga ndi kugwiritsa ntchito pa nthawi " +
                "ya kupembedza."
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        AboutCard(
            title = "ZOKONDA",

            text =
                "Nyimbo zomwe mumasunga ngati zokonda " +
                "zimasungidwa pa foni yanu ndipo zimapitiriza " +
                "kupezeka mukatsegulanso app."
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        AboutCard(
            title = "VESI",

            text =
                "Pano pali cipiriro ca oyera mtima, ca iwo " +
                "akusunga malamulo a Mulungu, ndi cikhulupiriro " +
                "ca Yesu. — Chibvumbulutso 14:12"
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )
    }
}


// ============================================================
// ABOUT CARD
// ============================================================

@Composable
fun AboutCard(
    title: String,
    text: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(21.dp)
        ) {

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Blue
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = text,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = GrayText
            )
        }
    }
}


// ============================================================
// SONG SCREEN
// ============================================================

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

        // -----------------------------------------------------
        // TOP BAR
        // -----------------------------------------------------

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryBlue)
                .padding(
                    horizontal = 8.dp,
                    vertical = 8.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "NYIMBO ${song.number}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Text(
                    text = song.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )
            }

            IconButton(
                onClick = onFavoriteClick
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
                            Gold
                        } else {
                            Color.White
                        }
                )
            }
        }

        // -----------------------------------------------------
        // SONG CONTENT
        // -----------------------------------------------------

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 24.dp
                )
        ) {

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = LightBlue
            ) {

                Text(
                    text = "NYIMBO ${song.number}",

                    modifier = Modifier.padding(
                        horizontal = 13.dp,
                        vertical = 7.dp
                    ),

                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blue
                )
            }

            Spacer(
                modifier = Modifier.height(15.dp)
            )

            Text(
                text = song.title,

                fontSize = 27.sp,

                lineHeight = 34.sp,

                fontWeight = FontWeight.ExtraBold,

                color = DarkText
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Text(
                text = song.englishTitle,
                fontSize = 14.sp,
                color = GrayText
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            HorizontalDivider(
                color = Color(0xFFE1E5EB)
            )

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(18.dp),

                color = Color.White,

                tonalElevation = 1.dp
            ) {

                Text(
                    text = song.verses.joinToString(
                        separator = "\n\n"
                    ),

                    modifier = Modifier.padding(20.dp),

                    fontSize = 18.sp,

                    lineHeight = 30.sp,

                    color = DarkText
                )
            }

            Spacer(
                modifier = Modifier.height(25.dp)
            )
        }

        // -----------------------------------------------------
        // PREVIOUS / NEXT
        // -----------------------------------------------------

        Surface(
            modifier = Modifier.fillMaxWidth(),

            color = Color.White,

            shadowElevation = 8.dp
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 12.dp,
                        vertical = 10.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically,

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                OutlinedButton(
                    onClick = onPreviousClick,
                    enabled = hasPrevious,

                    shape =
                        RoundedCornerShape(13.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous",
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(3.dp)
                    )

                    Text(
                        text = "YAM'MBUYOMO",
                        fontSize = 11.sp
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = LightBlue
                ) {

                    Text(
                        text = song.number.toString(),

                        modifier = Modifier.padding(
                            horizontal = 13.dp,
                            vertical = 9.dp
                        ),

                        fontWeight = FontWeight.Bold,

                        color = Blue
                    )
                }

                OutlinedButton(
                    onClick = onNextClick,
                    enabled = hasNext,

                    shape =
                        RoundedCornerShape(13.dp)
                ) {

                    Text(
                        text = "YOTSATIRA",
                        fontSize = 11.sp
                    )

                    Spacer(
                        modifier = Modifier.width(3.dp)
                    )

                    Icon(
                        imageVector =
                            Icons.Default.ArrowForward,

                        contentDescription =
                            "Next",

                        modifier =
                            Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
