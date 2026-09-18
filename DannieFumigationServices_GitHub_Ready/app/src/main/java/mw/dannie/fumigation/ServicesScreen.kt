package mw.dannie.fumigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ServicesScreen(
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        TextButton(onClick = onBack) {
            Text("← Back")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Our Services",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Professional pest control and fumigation solutions."
        )

        Spacer(modifier = Modifier.height(24.dp))

        ServiceCard(
            title = "General Fumigation",
            description = "Effective treatment for homes, offices and other properties."
        )

        ServiceCard(
            title = "Cockroach Control",
            description = "Targeted pest control to help eliminate cockroach infestations."
        )

        ServiceCard(
            title = "Bed Bug Control",
            description = "Professional treatment for bed bugs in residential and commercial properties."
        )

        ServiceCard(
            title = "Termite Control",
            description = "Treatment and control solutions for termite problems."
        )

        ServiceCard(
            title = "Rodent Control",
            description = "Solutions for rats, mice and other unwanted rodents."
        )

        ServiceCard(
            title = "Commercial Pest Control",
            description = "Pest management services for businesses, offices, shops and warehouses."
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ServiceCard(
    title: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                text = title,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                fontSize = 14.sp
            )
        }
    }
}