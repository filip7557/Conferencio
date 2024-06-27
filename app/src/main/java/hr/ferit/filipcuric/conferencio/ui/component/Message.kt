package hr.ferit.filipcuric.conferencio.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.filipcuric.conferencio.model.ChatMessage
import hr.ferit.filipcuric.conferencio.model.User
import hr.ferit.filipcuric.conferencio.ui.theme.DarkOnTertiaryColor
import hr.ferit.filipcuric.conferencio.ui.theme.OnTertiaryColor
import java.time.Instant
import java.time.ZoneId

@Composable
fun Message(
    message: ChatMessage,
    user: User,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth()
            .background(if (isSystemInDarkTheme()) DarkOnTertiaryColor else OnTertiaryColor, RoundedCornerShape(8.dp))
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = user.fullname,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            val dateTime = Instant.ofEpochMilli(message.timeStamp).atZone(ZoneId.systemDefault())
            Text(
                text = "${if (dateTime.dayOfMonth < 10) '0' else ""}${dateTime.dayOfMonth}/${if (dateTime.monthValue < 10) '0' else ""}${dateTime.monthValue}/${dateTime.year} ${if (dateTime.hour < 10) '0' else ""}${dateTime.hour}:${if (dateTime.minute < 10) '0' else ""}${dateTime.minute}",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraLight
            )
        }
        Text(
            text = message.message,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(start = 5.dp, bottom = 5.dp, end = 5.dp)
        )
    }
}
