package hr.ferit.filipcuric.conferencio.ui.picture

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import hr.ferit.filipcuric.conferencio.R
import hr.ferit.filipcuric.conferencio.ui.component.BlueButton
import hr.ferit.filipcuric.conferencio.ui.theme.Blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PictureScreen(
    onBackClick: () -> Unit,
    viewModel: PictureViewModel,
) {
    val picture = viewModel.picture.collectAsState()

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
                    text = "Picture",
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
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
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = picture.value.imageUrl,
                contentDescription = "picture"
            )
            val context = LocalContext.current
            BlueButton(
                modifier = Modifier
                    .padding(top = 10.dp),
                text = "Download",
                onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(picture.value.imageUrl)))
                }
            )
        }
    }
}
