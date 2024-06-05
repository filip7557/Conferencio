package hr.ferit.filipcuric.conferencio.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import hr.ferit.filipcuric.conferencio.ui.theme.Blue

@Composable
fun BlueButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Blue,
            contentColor = Color.White,
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            fontSize = 14.sp
        )
    }
}
