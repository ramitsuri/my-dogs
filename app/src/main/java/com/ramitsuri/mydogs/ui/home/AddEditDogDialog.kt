package com.ramitsuri.mydogs.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ramitsuri.mydogs.R
import com.ramitsuri.mydogs.data.model.SizeClass
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditDogDialog(
    viewModel: AddEditDogViewModel,
    onDismiss: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    var breedExpanded by remember { mutableStateOf(false) }

    val dateFormatter = remember {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_dog_barking),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (state.dogId == null) "Add New Dog" else "Edit Dog Profile",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Name Field
                OutlinedTextField(
                    value = state.name,
                    onValueChange = viewModel::onNameChanged,
                    label = { Text("Dog Name *") },
                    isError = state.isNameError,
                    supportingText = {
                        if (state.isNameError) {
                            Text("Name cannot be empty", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Birthday Field with DatePicker trigger
                OutlinedTextField(
                    value = dateFormatter.format(Date(state.birthdayTimestamp)),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Birthday") },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Rounded.CalendarMonth, contentDescription = "Select Birthday")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }
                )

                // Breed Autocomplete / Dropdown
                ExposedDropdownMenuBox(
                    expanded = breedExpanded,
                    onExpandedChange = { breedExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = state.breed,
                        onValueChange = {
                            viewModel.onBreedChanged(it)
                            breedExpanded = true
                        },
                        label = { Text("Breed (Optional / Mixed)") },
                        placeholder = { Text("e.g. Labrador Retriever, Mixed") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = breedExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        singleLine = true
                    )

                    if (state.availableBreeds.isNotEmpty()) {
                        ExposedDropdownMenu(
                            expanded = breedExpanded,
                            onDismissRequest = { breedExpanded = false }
                        ) {
                            state.availableBreeds.forEach { breedName ->
                                DropdownMenuItem(
                                    text = { Text(breedName) },
                                    onClick = {
                                        viewModel.onBreedChanged(breedName)
                                        breedExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Fallback Size Class selector using FlowRow
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Size Class (For Mixed / Custom Breeds)",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SizeClass.entries.forEach { sizeClass ->
                            val selected = state.fallbackSizeClass == sizeClass
                            FilterChip(
                                selected = selected,
                                onClick = { viewModel.onSizeClassChanged(sizeClass) },
                                label = {
                                    Text(
                                        text = sizeClass.name.lowercase()
                                            .replaceFirstChar { it.uppercase() }
                                    )
                                }
                            )
                        }
                    }
                    Text(
                        text = when (state.fallbackSizeClass) {
                            SizeClass.SMALL -> "Small: 0 - 20 lbs (Lifespan ~14 yrs)"
                            SizeClass.MEDIUM -> "Medium: 21 - 50 lbs (Lifespan ~13 yrs)"
                            SizeClass.LARGE -> "Large: 51 - 90 lbs (Lifespan ~11 yrs)"
                            SizeClass.GIANT -> "Giant: 91+ lbs (Lifespan ~9.5 yrs)"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { viewModel.save(onSaved = onDismiss) },
                enabled = !state.isSaving
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Save Dog")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.birthdayTimestamp
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            viewModel.onBirthdayChanged(millis)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
