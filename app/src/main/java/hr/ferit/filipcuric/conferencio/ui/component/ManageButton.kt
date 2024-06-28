package hr.ferit.filipcuric.conferencio.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hr.ferit.filipcuric.conferencio.R
import hr.ferit.filipcuric.conferencio.ui.theme.Blue
import hr.ferit.filipcuric.conferencio.ui.theme.DarkTertiaryColor
import hr.ferit.filipcuric.conferencio.ui.theme.TertiaryColor

@Composable
fun ManageButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(top = 30.dp)
            .clip(CircleShape)
            .size(32.dp)
            .background(if(isSystemInDarkTheme()) DarkTertiaryColor.copy(alpha = 0.8f) else TertiaryColor.copy(alpha = 0.8f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_gear),
            contentDescription = "back icon",
            tint = Blue,
            modifier = Modifier
                .padding(6.dp)
        )
    }
}
