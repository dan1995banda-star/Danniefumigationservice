package mw.dannie.fumigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
                        onSong1 = {
                            navController.navigate("song/1")
                        }
                    )
                }

                composable("song/1") {
                    Song1Screen(
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    onSong1: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "LEMEKEZANI\nMULUNGU M'NYIMBO",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Nyimbo za Chikhristu",
            fontSize = 17.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onSong1,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "SONG 1",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun Song1Screen(
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
            text = "1",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "AITANA, AITANA DZIKO LONSE",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "He Calls the Whole World",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        HymnVerse(
            number = 1,
            text = """
Aitana, aitana dziko lonse,
Imvani, mukhaliranji chete? Aitana,
Aitana, aitana dziko lonse.
""".trimIndent()
        )

        HymnVerse(
            number = 2,
            text = """
Mfumu iyi, Mfumu iyi yakumwamba,
Ndi Yesu, ndi Mpulumutsiyo wa anthu onse,
Anthu onse, anthu onse, tinke kwawo.
""".trimIndent()
        )

        HymnVerse(
            number = 3,
            text = """
Aitana, aitana inu mai,
Ndi Yesu, amene anakhetsa mwazi wake;
Mwazi wake unagwera inu mai.
""".trimIndent()
        )

        HymnVerse(
            number = 4,
            text = """
Aitana, aitana inu 'tate,
Ndi Yesu amene anakhetsa mwazi wake:
Mwazi wake unagwera inu 'tate.
""".trimIndent()
        )

        HymnVerse(
            number = 5,
            text = """
Yesu ati, Yesu ati: "Muzisiye
Zoipa zimene muzichita, muzisiye,
Muzisiye, muzisiye inu nonse."
""".trimIndent()
        )

        HymnVerse(
            number = 6,
            text = """
Titi bwanji? Titi bwanji ife anthu?
Ndi Yesu anatifera ife akuchimwa;
Akuchimwa, akuchimwa tinke kwawo.
""".trimIndent()
        )

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
