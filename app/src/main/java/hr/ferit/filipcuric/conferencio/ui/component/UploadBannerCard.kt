package hr.ferit.filipcuric.conferencio.ui.component

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import hr.ferit.filipcuric.conferencio.R
import hr.ferit.filipcuric.conferencio.ui.theme.Blue

@Composable
fun UploadBannerCard(
    onClick: () -> Unit,
    imageUri: Uri?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(2.dp, Blue, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        if(imageUri != Uri.EMPTY) {
            AsyncImage(
                model = imageUri
                ,
                contentDescription = "image",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Surface (
                modifier = Modifier
                    .fillMaxSize(),
                color = Color(0f, 0f, 0f, 0.7f, ColorSpaces.Srgb)
            ) {}
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.upload),
                contentDescription = "Circled plus icon",
                colorFilter = ColorFilter.tint(Blue),
            )
            Text(
                text = "Upload your banner",
                color = Blue,
                fontSize = 18.sp,
            )
        }
    }
}

@Preview
@Composable
fun UploadBannerCardPreview() {
    UploadBannerCard(onClick = { /*TODO*/ }, imageUri = Uri.EMPTY)
}
