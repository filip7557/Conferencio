package hr.ferit.filipcuric.conferencio.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.filipcuric.conferencio.ui.component.BlueButton
import hr.ferit.filipcuric.conferencio.ui.component.TextBox
import hr.ferit.filipcuric.conferencio.ui.theme.Blue

@Preview
@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        Title()
        Subtitle()
        TextBox(label = "Email", value = "", onValueChange = {})
        TextBox(label = "Password", value = "", onValueChange = {})
        BlueButton(text = "Login", onClick = {})
    }
}

@Composable
fun Title() {
    Column(
        modifier = Modifier
            .padding(bottom = 20.dp)
    ) {
        Text(
            text = "Welcome to",
            fontSize = 48.sp,
            fontWeight = FontWeight.Normal,
            color = Blue,
        )
        Text(
            text = "Conferencio!",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Blue,
        )
    }
}

@Composable
fun Subtitle() {
    Text(
        text = "Please login to continue.",
        fontSize = 20.sp,
        modifier = Modifier
            .padding(bottom = 20.dp)
    )
}
