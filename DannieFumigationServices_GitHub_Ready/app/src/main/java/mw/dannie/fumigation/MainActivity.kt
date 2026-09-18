package mw.dannie.fumigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DannieFumigationApp()
        }
    }
}

@Composable
fun DannieFumigationApp() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen()
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Dannie Fumigation Services",
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Professional Pest Control & Fumigation"
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Booking screen will be added next
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Book a Service")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                // Services screen will be added next
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Our Services")
        }
    }
}
