package hr.ferit.filipcuric.conferencio.ui.createconference

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepositoryImpl
import hr.ferit.filipcuric.conferencio.data.repository.UserRepositoryImpl
import hr.ferit.filipcuric.conferencio.ui.component.BackButton
import hr.ferit.filipcuric.conferencio.ui.component.TextBox
import hr.ferit.filipcuric.conferencio.ui.component.UploadBannerCard
import hr.ferit.filipcuric.conferencio.ui.theme.Blue
import java.time.Instant

@Composable
fun CreateConferenceScreen(
    viewModel: CreateConferenceViewModel,
    onBackClick: () -> Unit,
) {
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
                onValueChange = {
                    viewModel.onTitleChange(it)
                }
            )
        }
        item {
            TextBox(
                label = "Starting date",
                value = viewModel.startDateTextValue,
                onValueChange = { },
                readOnly = true,
                modifier = Modifier
                    .clickable {
                        viewModel.showDatePicker = true
                    }
            )
            if (viewModel.showDatePicker) {
                MyDatePickerDialog(
                    onDateSelected = {
                        viewModel.startDate = it
                        viewModel.onStartDateTextValueChange(it)
                                     },
                    onDismiss = {
                        viewModel.showDatePicker = false
                    }
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    onDateSelected: (Instant) -> Unit,
    onDismiss: () -> Unit,
) {
    val dateState = rememberDatePickerState()

    val dateSelected = dateState.selectedDateMillis?.let { Instant.ofEpochMilli(it) } ?: Instant.now()

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(dateSelected)
                onDismiss()
            }) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        },
        colors = DatePickerDefaults.colors(
            //TODO: Set proper colors
        ),
        content = {
            DatePicker(state = dateState)
        }
    )
}

@Preview
@Composable
fun CreateConferenceScreenPreview() {
    CreateConferenceScreen(
        viewModel = CreateConferenceViewModel(
            userRepository = UserRepositoryImpl(),
            conferenceRepository = ConferenceRepositoryImpl(),
        ),
        onBackClick = { }
    )
}
