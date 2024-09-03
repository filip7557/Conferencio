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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.filipcuric.conferencio.model.ChatMessage
import hr.ferit.filipcuric.conferencio.model.User
import hr.ferit.filipcuric.conferencio.ui.theme.Blue
import java.time.Instant
import java.time.ZoneId

@Composable
fun Message(
    message: ChatMessage,
    user: User,
    conferenceOwnerId: String,
    isUserAuthor: Boolean = false,
    isEvent: Boolean = false,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth()
            .background(
                if (isSystemInDarkTheme()) Color(40, 40, 41) else Color(166, 166, 166),
                if (isUserAuthor) RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp, topEnd = 0.dp, bottomEnd = 20.dp) else RoundedCornerShape(topStart = 0.dp, bottomStart = 20.dp, topEnd = 20.dp, bottomEnd = 20.dp)
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .fillMaxWidth()
        ) {
            Column {

                Text(
                    text = user.fullname.ifEmpty { "Deleted User" },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                if (user.id == conferenceOwnerId) {
                    Text(
                        text = if (isEvent) "Host" else "Organizer",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Thin,
                        color = Blue,
                    )
                }
            }
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
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .padding(start = 10.dp, bottom = 10.dp, end = 30.dp)
        )
    }
}
