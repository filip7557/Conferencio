package hr.ferit.filipcuric.conferencio.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hr.ferit.filipcuric.conferencio.ui.theme.Blue
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConferenceDatePickerDialog(
    onDateSelected: (Instant) -> Unit,
    onDismiss: () -> Unit,
    dateValidator: (Long) -> Boolean,
) {
    val dateState = rememberDatePickerState()

    val dateSelected = dateState.selectedDateMillis?.let { Instant.ofEpochMilli(it) } ?: Instant.now()

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = {
                    onDateSelected(dateSelected)
                    onDismiss()
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
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                ),
                border = BorderStroke(2.dp, Blue)
            ) {
                Text(text = "Cancel", color = Blue)
            }
        },
        content = {
            DatePicker(
                state = dateState,
                colors = DatePickerDefaults.colors(
                    selectedYearContainerColor = Blue,
                    selectedDayContainerColor = Blue,
                    currentYearContentColor = Blue,
                    todayDateBorderColor = Blue,
                    todayContentColor = Blue,
                ),
                dateValidator = {
                    dateValidator(it)
                }
            )
        }
    )
}
