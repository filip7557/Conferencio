package hr.ferit.filipcuric.conferencio.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hr.ferit.filipcuric.conferencio.R
import hr.ferit.filipcuric.conferencio.ui.theme.DarkTertiaryColor
import hr.ferit.filipcuric.conferencio.ui.theme.TertiaryColor

@Composable
fun BackButton(
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(top = 50.dp, start = 10.dp)
            .clip(CircleShape)
            .size(32.dp)
            .background(if(isSystemInDarkTheme()) DarkTertiaryColor.copy(alpha = 0.8f) else TertiaryColor.copy(alpha = 0.8f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
            contentDescription = "back icon",
            modifier = Modifier
                .padding(start = 2.5.dp, top=4.5.dp))
    }
}
