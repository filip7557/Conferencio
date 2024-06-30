package hr.ferit.filipcuric.conferencio.ui.createvent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepositoryImpl
import hr.ferit.filipcuric.conferencio.data.repository.UserRepositoryImpl
import hr.ferit.filipcuric.conferencio.ui.component.BackButton
import hr.ferit.filipcuric.conferencio.ui.component.BlueButton
import hr.ferit.filipcuric.conferencio.ui.component.ConferenceDatePickerDialog
import hr.ferit.filipcuric.conferencio.ui.component.TextBox
import hr.ferit.filipcuric.conferencio.ui.component.TimePickerDialog
import hr.ferit.filipcuric.conferencio.ui.component.UserCard
import hr.ferit.filipcuric.conferencio.ui.theme.Blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    viewModel: CreateEventViewModel,
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit,
) {
    val timePickerState = rememberTimePickerState()
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(10.dp)
    ) {
        item {
            BackButton(
                onClick = {
                    onBackClick()
                }
            )
        }
        item {
            Title()
        }
        item {
            TextBox(
                label = "Title",
                value = viewModel.title,
                onValueChange = { viewModel.onTitleChange(it) })
            TextBox(
                label = "Location",
                value = viewModel.location,
                onValueChange = { viewModel.onLocationChange(it) })
            TextBox(
                label = "Duration (minutes)",
                value = viewModel.duration,
                onValueChange = { viewModel.onDurationChange(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            TextBox(
                label = "Host",
                value = viewModel.host,
                onValueChange = {
                    viewModel.hostId = ""
                    viewModel.onHostChange(it)
                }
            )
            if (viewModel.hostId == "") {
                val users = viewModel.foundHosts.collectAsState()
                for (user in users.value) {
                    UserCard(user = user, onClick = { viewModel.hostId = it })
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Starting date: ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                )
                if (viewModel.startDateTextValue == "Choose date") {
                    Button(
                        onClick = {
                            viewModel.showStartDatePicker = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                        ),
                        border = BorderStroke(2.dp, Blue),
                    ) {
                        Text(text = "Choose date", color = Blue)
                    }
                } else {
                    Text(
                        text = viewModel.startDateTextValue,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .clickable {
                                viewModel.showStartDatePicker = true
                            }
                    )
                }
                if (viewModel.showStartDatePicker) {
                    ConferenceDatePickerDialog(
                        onDateSelected = { viewModel.onStartDateTextValueChange(it) },
                        onDismiss = { viewModel.showStartDatePicker = false }
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Starting time: ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                )
                if (viewModel.timeTextValue == "Choose time") {
                    Button(
                        onClick = {
                            viewModel.showTimePicker = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                        ),
                        border = BorderStroke(2.dp, Blue),
                    ) {
                        Text(text = "Choose time", color = Blue)
                    }
                } else {
                    Text(
                        text = viewModel.timeTextValue,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .clickable {
                                viewModel.showTimePicker = true
                            }
                    )
                }
                if (viewModel.showTimePicker) {
                    TimePickerDialog(
                        onDismissRequest = { viewModel.showTimePicker = false },
                        confirmButton = {
                            Button(
                                onClick = {
                                    viewModel.onTimeTextValueChange(timePickerState.hour, timePickerState.minute)
                                    viewModel.showTimePicker = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Blue,
                                )
                            ) {
                                Text(text = "Confirm", color = Color.White)
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    viewModel.showTimePicker = false
                                    viewModel.timeTextValue = "Choose time"
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                ),
                                border = BorderStroke(2.dp, Blue)
                            ) {
                                Text(text = "Cancel", color = Blue)
                            }
                        }
                    ) {
                        TimePicker(
                            state = timePickerState,
                            layoutType = TimePickerLayoutType.Vertical,
                            colors = TimePickerDefaults.colors(
                                selectorColor = Blue,
                                timeSelectorSelectedContainerColor = Blue,
                                timeSelectorSelectedContentColor = Color.White
                            )
                        )
                    }
                }
            }
        }
        item {
            BlueButton(
                text = "Add",
                onClick = {
                    viewModel.onCreateClick(onCreateClick)
                }
            )
        }
    }
}

@Composable
fun Title() {
    Text(
        text = "Add an Event",
        fontSize = 48.sp,
        fontWeight = FontWeight.Medium,
        color = Blue,
        modifier = Modifier
            .padding(bottom = 20.dp, top = 20.dp)
    )
}

@Preview
@Composable
fun CreateEventScreenPreview() {
    CreateEventScreen(
        CreateEventViewModel(UserRepositoryImpl(), ConferenceRepositoryImpl()),
        {},
        {}
    )
}
