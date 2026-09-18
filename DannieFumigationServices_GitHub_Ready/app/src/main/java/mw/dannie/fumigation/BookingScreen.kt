import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
package mw.dannie.fumigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var propertyType by remember { mutableStateOf("") }
    var service by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var propertyExpanded by remember { mutableStateOf(false) }
    var serviceExpanded by remember { mutableStateOf(false) }
    var timeExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val propertyTypes = listOf(
        "House",
        "Office",
        "Shop",
        "Warehouse",
        "Restaurant",
        "Other"
    )

    val services = listOf(
        "General Fumigation",
        "Cockroach Control",
        "Bed Bug Control",
        "Termite Control",
        "Rodent Control",
        "Commercial Pest Control"
    )

    val times = listOf(
        "08:00 AM",
        "09:00 AM",
        "10:00 AM",
        "11:00 AM",
        "12:00 PM",
        "01:00 PM",
        "02:00 PM",
        "03:00 PM",
        "04:00 PM"
    )

    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formatter = SimpleDateFormat(
                                "dd/MM/yyyy",
                                Locale.getDefault()
                            )
                            date = formatter.format(Date(millis))
                        }

                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("CANCEL")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }

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
            text = "Book a Service",
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tell us about the fumigation service you need."
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = propertyExpanded,
            onExpandedChange = {
                propertyExpanded = !propertyExpanded
            }
        ) {
            OutlinedTextField(
                value = propertyType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Property Type") },
                placeholder = { Text("Select property type") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = propertyExpanded
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = propertyExpanded,
                onDismissRequest = {
                    propertyExpanded = false
                }
            ) {
                propertyTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            propertyType = type
                            propertyExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = serviceExpanded,
            onExpandedChange = {
                serviceExpanded = !serviceExpanded
            }
        ) {
            OutlinedTextField(
                value = service,
                onValueChange = {},
                readOnly = true,
                label = { Text("Service Required") },
                placeholder = { Text("Select a service") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = serviceExpanded
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = serviceExpanded,
                onDismissRequest = {
                    serviceExpanded = false
                }
            ) {
                services.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            service = item
                            serviceExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = date,
            onValueChange = {},
            readOnly = true,
            label = { Text("Preferred Date") },
            placeholder = { Text("Select a date") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                TextButton(
                    onClick = {
                        showDatePicker = true
                    }
                ) {
                    Text("SELECT")
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = timeExpanded,
            onExpandedChange = {
                timeExpanded = !timeExpanded
            }
        ) {
            OutlinedTextField(
                value = time,
                onValueChange = {},
                readOnly = true,
                label = { Text("Preferred Time") },
                placeholder = { Text("Select a time") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = timeExpanded
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = timeExpanded,
                onDismissRequest = {
                    timeExpanded = false
                }
            ) {
                times.forEach { selectedTime ->
                    DropdownMenuItem(
                        text = { Text(selectedTime) },
                        onClick = {
                            time = selectedTime
                            timeExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Property Address") },
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Additional Notes") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Booking submission will be connected next
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "SUBMIT BOOKING",
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}