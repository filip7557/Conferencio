package hr.ferit.filipcuric.conferencio.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.filipcuric.conferencio.R
import hr.ferit.filipcuric.conferencio.model.Event
import java.time.Instant
import java.time.ZoneId

@Composable
fun EventCard(
    event: Event,
    onClick: (String) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (event.type) {
                "Lecture" -> Color(151, 252, 134).copy(alpha = 0.58f)
                "Workshop" -> Color(252, 226, 134).copy(alpha = 0.58f)
                "Fair" -> Color(52, 93, 168).copy(alpha = 0.58f)
                else -> Color(150, 2, 2).copy(alpha = 0.58f)
            },
            contentColor = Color.Black,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(event.id!!) }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(
                        id = when (event.type) {
                            "Lecture" -> R.drawable.ic_lecture
                            "Workshop" -> R.drawable.ic_workshop
                            "Fair" -> R.drawable.ic_fair
                            else -> R.drawable.ic_other
                        }
                    ),
                    contentDescription = "event icon"
                )
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .padding(start = 5.dp)
                ) {
                    Text(
                        text = event.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(
                        text = event.type,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
            ) {
                val datetime = Instant.ofEpochMilli(event.dateTime).atZone(ZoneId.systemDefault())
                Text(text = "${if (datetime.dayOfMonth < 10) '0' else ""}${datetime.dayOfMonth}/${if (datetime.monthValue < 10) '0' else ""}${datetime.monthValue}/${datetime.year}")
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "${if (datetime.hour < 10) '0' else ""}${datetime.hour}:${if (datetime.minute < 10) '0' else ""}${datetime.minute}")
                    Text(text = event.location)
                }
            }
        }
    }
}
