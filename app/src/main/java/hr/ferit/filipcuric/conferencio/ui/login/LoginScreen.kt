package hr.ferit.filipcuric.conferencio.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.filipcuric.conferencio.ui.component.BlueButton
import hr.ferit.filipcuric.conferencio.ui.component.TextBox
import hr.ferit.filipcuric.conferencio.ui.theme.Blue

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        Title()
        Subtitle()
        TextBox(
            label = "Email",
            value = viewModel.email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                capitalization = KeyboardCapitalization.None
            ),
            onValueChange = { viewModel.onEmailChange(it) }
        )
        TextBox(
            label = "Password",
            value = viewModel.password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                capitalization = KeyboardCapitalization.None
            ),
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { viewModel.onPasswordChange(it) }
        )
        Text(
            text = viewModel.error,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Red
        )
        BlueButton(text = "Login", onClick = { viewModel.login(onLoginClick) })
        RegisterText(onRegisterClick)
    }
}

@Composable
fun Title() {
    Column(
        modifier = Modifier
            .padding(bottom = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Welcome to",
            fontSize = 48.sp,
            fontWeight = FontWeight.Medium,
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
        fontWeight = FontWeight.Light,
        modifier = Modifier
            .padding(bottom = 20.dp)
    )
}

@Composable
fun RegisterText(onRegisterClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Don't have an account?",
                fontSize = 16.sp,
                fontWeight = FontWeight.Light
            )
            Text(
                text = "Sign up now!",
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                color = Blue,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .clickable { onRegisterClick() }
            )
        }
    }
}
