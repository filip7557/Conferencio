package hr.ferit.filipcuric.conferencio.ui.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import hr.ferit.filipcuric.conferencio.model.Picture

@Composable
fun Picture(
    picture: Picture
) {
    val context = LocalContext.current
    AsyncImage(
        model = picture.imageUrl,
        contentDescription = "picture",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .size(200.dp)
            .clickable {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(picture.imageUrl)))
            }
    )
}
