package hr.ferit.filipcuric.conferencio.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.filipcuric.conferencio.ui.component.BackButton
import hr.ferit.filipcuric.conferencio.ui.component.BlueButton
import hr.ferit.filipcuric.conferencio.ui.component.TextBox
import hr.ferit.filipcuric.conferencio.ui.theme.Blue

@Preview
@Composable
fun RegisterScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        BackButton(
            onClick = {}
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Title()
            Subtitle()
            TextBox(label = "Full name", value = "", onValueChange = {})
            TextBox(label = "Company/Organization", value = "", onValueChange = {})
            TextBox(label = "Position", value = "", onValueChange = {})
            TextBox(label = "Email", value = "", onValueChange = {})
            TextBox(label = "Password", value = "", onValueChange = {})
            BlueButton(text = "Register", onClick = {})
        }
    }
}

@Composable
fun Title() {
    Text(
        text = "Register",
        fontSize = 48.sp,
        color = Blue,
        )
}

@Composable
fun Subtitle() {
    Text(
        text = "Create your new account",
        fontSize = 20.sp,
        modifier = Modifier
            .padding(bottom = 50.dp)
    )
}
