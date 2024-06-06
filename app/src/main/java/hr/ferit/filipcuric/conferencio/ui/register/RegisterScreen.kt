package hr.ferit.filipcuric.conferencio.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.filipcuric.conferencio.data.repository.UserRepositoryImpl
import hr.ferit.filipcuric.conferencio.ui.component.BackButton
import hr.ferit.filipcuric.conferencio.ui.component.BlueButton
import hr.ferit.filipcuric.conferencio.ui.component.TextBox
import hr.ferit.filipcuric.conferencio.ui.theme.Blue

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
) {
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
            TextBox(
                label = "Full name",
                value = viewModel.fullname,
                onValueChange = {
                    viewModel.onFullnameChange(it)
                }
            )
            TextBox(
                label = "Company/Organization",
                value = viewModel.company,
                onValueChange = {
                    viewModel.onCompanyChange(it)
                }
            )
            TextBox(
                label = "Position",
                value = viewModel.position,
                onValueChange = {
                    viewModel.onPositionChange(it)
                }
            )
            TextBox(
                label = "Email",
                value = viewModel.email,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                onValueChange = {
                    viewModel.onEmailChange(it)
                }
            )
            TextBox(
                label = "Password",
                value = viewModel.password,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = {
                    viewModel.onPasswordChange(it)
                })
            BlueButton(text = "Register", onClick = { viewModel.onRegisterClick() })
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

@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(viewModel = RegisterViewModel(UserRepositoryImpl()))
}
