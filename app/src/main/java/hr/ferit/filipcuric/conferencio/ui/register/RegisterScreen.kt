package hr.ferit.filipcuric.conferencio.ui.register

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.filipcuric.conferencio.ui.component.BackButton
import hr.ferit.filipcuric.conferencio.ui.component.BlueButton
import hr.ferit.filipcuric.conferencio.ui.component.TextBox
import hr.ferit.filipcuric.conferencio.ui.component.UploadProfilePictureCard
import hr.ferit.filipcuric.conferencio.ui.theme.Blue

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onBackClick: () -> Unit,
    onRegisterClick: () -> Unit,
) {
    val emailHasError = viewModel.emailHasError.collectAsState()
    val isEmailValid = viewModel.isEmailValid.collectAsState()
    val passwordHasError = viewModel.passwordHasError.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        BackButton(
            onClick = {
                onBackClick()
            }
        )
        Title()
        Subtitle()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent(),
                    onResult = { uri: Uri? -> uri?.let { viewModel.onImageSelected(it) } }
                )
                UploadProfilePictureCard(
                    onClick = {
                        launcher.launch("image/*")
                    },
                    imageUri = viewModel.imageUri,
                    modifier = Modifier
                        .height(250.dp)
                        .width(250.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                TextBox(
                    label = "Full name",
                    value = viewModel.fullname,
                    onValueChange = {
                        viewModel.onFullnameChange(it)
                    },
                    isError = viewModel.fullname.isEmpty(),
                    supportingText = {
                        if (viewModel.fullname.isEmpty()) {
                            Text(text = "Full name field cannot be empty.")
                        }
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
                        keyboardType = KeyboardType.Email,
                        capitalization = KeyboardCapitalization.None
                    ),
                    onValueChange = {
                        viewModel.onEmailChange(it)
                    },
                    isError = emailHasError.value || !isEmailValid.value,
                    supportingText = {
                        if (!isEmailValid.value) {
                            Text(text = "This is not a valid email address.")
                        } else if (emailHasError.value) {
                            Text(text = "This email address is not available.")
                        }
                    }
                )
                TextBox(
                    label = "Password",
                    value = viewModel.password,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        capitalization = KeyboardCapitalization.None
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        viewModel.onPasswordChange(it)
                    },
                    isError = passwordHasError.value,
                    supportingText = {
                        if (passwordHasError.value) {
                            Text(text = "Password must be at least 6 characters long.")
                        }
                    }
                )
            }
            item {
                BlueButton(
                    text = "Register",
                    enabled = !viewModel.registrationHasErrors(),
                    onClick = { viewModel.onRegisterClick(onRegisterClick) })
            }
        }
    }
}

@Composable
fun Title() {
    Text(
        text = "Register",
        fontSize = 48.sp,
        color = Blue,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun Subtitle() {
    Text(
        text = "Create your new account.",
        fontSize = 20.sp,
        fontWeight = FontWeight.Thin,
        modifier = Modifier
            .padding(bottom = 20.dp)
    )
}
