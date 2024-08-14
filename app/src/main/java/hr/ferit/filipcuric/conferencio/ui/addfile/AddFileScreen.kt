package hr.ferit.filipcuric.conferencio.ui.addfile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.filipcuric.conferencio.R
import hr.ferit.filipcuric.conferencio.ui.component.BlueButton
import hr.ferit.filipcuric.conferencio.ui.component.LoadingAnimation
import hr.ferit.filipcuric.conferencio.ui.component.TextBox
import hr.ferit.filipcuric.conferencio.ui.theme.Blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFileScreen(
    viewModel: AddFileViewModel,
    onBackClick: () -> Unit,
    onUploadClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Blue,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White,
                titleContentColor = Color.White
            ),
            title = {
                Text(
                    text = "Add file",
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = onBackClick,
                    enabled = !viewModel.loading
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
                        contentDescription = "back icon",
                        modifier = Modifier
                            .padding(top = 5.dp)
                    )
                }
            },
            windowInsets = WindowInsets.statusBars
        )
        if (viewModel.loading) {
            LoadingAnimation()
        } else {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextBox(
                    modifier = Modifier
                        .padding(10.dp),
                    label = "File display name",
                    isError = viewModel.fileName.isEmpty(),
                    supportingText = {
                        if (viewModel.fileName.isEmpty()) {
                            Text(text = "File display name filed cannot be empty.")
                        }
                    },
                    value = viewModel.fileName,
                    onValueChange = {
                        viewModel.onFileNameChange(it)
                    }
                )
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent(),
                    onResult = { uri: Uri? -> uri?.let { viewModel.onFileUriChange(it) } }
                )
                if (viewModel.fileUri == Uri.EMPTY) {
                    Button(
                        onClick = {
                            launcher.launch("application/pdf")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                        ),
                        border = BorderStroke(2.dp, Blue),
                    ) {
                        Text(text = "Choose file", color = Blue)
                    }
                } else {
                    Text(
                        text = viewModel.fileUri.lastPathSegment.toString(),
                        fontSize = 18.sp,
                        modifier = Modifier
                            .clickable {
                                launcher.launch("application/pdf")
                            }
                    )
                }
                BlueButton(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                    text = "Add file",
                    enabled = viewModel.fileName.isNotEmpty() && viewModel.fileUri != Uri.EMPTY,
                    onClick = {
                        viewModel.uploadFile(onUploadClick)
                    }
                )
            }
        }
    }
}
