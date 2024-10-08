package hr.ferit.filipcuric.conferencio.ui.createconference

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.filipcuric.conferencio.ui.component.BackButton
import hr.ferit.filipcuric.conferencio.ui.component.BlueButton
import hr.ferit.filipcuric.conferencio.ui.component.ConferenceDatePickerDialog
import hr.ferit.filipcuric.conferencio.ui.component.LoadingAnimation
import hr.ferit.filipcuric.conferencio.ui.component.TextBox
import hr.ferit.filipcuric.conferencio.ui.component.UploadBannerCard
import hr.ferit.filipcuric.conferencio.ui.theme.Blue
import java.time.Instant

@Composable
fun CreateConferenceScreen(
    viewModel: CreateConferenceViewModel,
    onBackClick: () -> Unit,
    onCreateClick: (String) -> Unit,
) {
    if (viewModel.loading) {
        LoadingAnimation()
    } else {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            BackButton(
                onClick = {
                    onBackClick()
                }
            )
            LazyColumn(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(10.dp)
            ) {
                item {
                    Title()
                }
                item {
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent(),
                        onResult = { uri: Uri? -> uri?.let { viewModel.onImageSelected(it) } }
                    )
                    UploadBannerCard(
                        onClick = {
                            launcher.launch("image/*")
                        },
                        imageUri = viewModel.imageUri,
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                    )
                }
                item {
                    TextBox(
                        label = "Title",
                        value = viewModel.title,
                        isError = viewModel.title.isEmpty(),
                        supportingText = {
                            if (viewModel.title.isEmpty()) {
                                Text(text = "Title field cannot be empty.")
                            }
                        },
                        onValueChange = {
                            viewModel.onTitleChange(it)
                        }
                    )
                }
                item {
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
                                onDismiss = { viewModel.showStartDatePicker = false },
                                dateValidator = {
                                    it >= Instant.now().toEpochMilli()
                                }
                            )
                        }
                    }
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp)
                    ) {
                        Text(
                            text = "Ending date: ",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                        )
                        if (viewModel.endDateTextValue == "Choose date") {
                            Button(
                                onClick = {
                                    viewModel.showEndDatePicker = true
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
                                text = viewModel.endDateTextValue,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .clickable {
                                        viewModel.showEndDatePicker = true
                                    }
                            )
                        }
                        if (viewModel.showEndDatePicker) {
                            ConferenceDatePickerDialog(
                                onDateSelected = { viewModel.onEndDateTextValueChange(it) },
                                onDismiss = { viewModel.showEndDatePicker = false },
                                dateValidator = {
                                    it >= Instant.now()
                                        .toEpochMilli() && it >= viewModel.startDate.toEpochMilli()
                                }
                            )
                        }
                    }
                }
                item {
                    BlueButton(
                        text = "Create",
                        enabled = viewModel.title.isNotEmpty() && viewModel.startDateTextValue != "Choose date" && viewModel.endDateTextValue != "Choose date",
                        onClick = {
                            viewModel.onCreateClick(onCreateClick)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Title() {
    Column(
        modifier = Modifier
            .padding(bottom = 20.dp, top = 50.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Create a",
            fontSize = 48.sp,
            fontWeight = FontWeight.Medium,
            color = Blue,
        )
        Text(
            text = "Conference",
            fontSize = 48.sp,
            fontWeight = FontWeight.Medium,
            color = Blue,
        )
    }
}
