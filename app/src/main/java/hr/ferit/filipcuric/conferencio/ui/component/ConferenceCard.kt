package hr.ferit.filipcuric.conferencio.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import hr.ferit.filipcuric.conferencio.mock.getConferences
import hr.ferit.filipcuric.conferencio.mock.getUser
import hr.ferit.filipcuric.conferencio.model.Conference
import hr.ferit.filipcuric.conferencio.model.User
import hr.ferit.filipcuric.conferencio.ui.theme.DarkTertiaryColor
import hr.ferit.filipcuric.conferencio.ui.theme.TertiaryColor
import java.time.Instant
import java.time.ZoneId

@Composable
fun ConferenceCard(
    conference: Conference,
    user: User,
    onClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) DarkTertiaryColor else TertiaryColor,
            contentColor = if (isSystemInDarkTheme()) Color.White else Color.Black
        ),
        modifier = Modifier
            .size(height = 210.dp, width = 310.dp)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = conference.imageUrl,
            contentDescription = "conference banner",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = conference.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                val datetime = Instant.ofEpochMilli(conference.startDateTime).atZone(ZoneId.systemDefault())
                Row {
                    Text(
                        text = "Start: ",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${datetime.dayOfMonth}/${if (datetime.monthValue < 10) '0' else ""}${datetime.monthValue}/${datetime.year}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }
            Text(
                text = "Hosted by: ${user.fullname}, ${user.position} @ ${user.company}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Preview
@Composable
fun ConferenceCardPreview() {
    ConferenceCard(conference = getConferences().first(), user = getUser(), onClick = {})
}
