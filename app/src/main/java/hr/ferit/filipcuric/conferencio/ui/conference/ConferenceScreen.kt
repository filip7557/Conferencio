package hr.ferit.filipcuric.conferencio.ui.conference

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import hr.ferit.filipcuric.conferencio.R
import hr.ferit.filipcuric.conferencio.model.ChatMessage
import hr.ferit.filipcuric.conferencio.model.User
import hr.ferit.filipcuric.conferencio.ui.component.BackButton
import hr.ferit.filipcuric.conferencio.ui.component.ManageButton
import hr.ferit.filipcuric.conferencio.ui.component.Message
import hr.ferit.filipcuric.conferencio.ui.component.SendMessageCard
import hr.ferit.filipcuric.conferencio.ui.theme.Blue
import hr.ferit.filipcuric.conferencio.ui.theme.DarkTertiaryColor
import hr.ferit.filipcuric.conferencio.ui.theme.TertiaryColor
import java.time.Duration
import java.time.Instant

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConferenceScreen(
    viewModel: ConferenceViewModel,
    onBackClick: () -> Unit,
    onManageClick: (String) -> Unit,
) {
    val conference = viewModel.conference.collectAsState()
    val duration = viewModel.duration.collectAsState()
    val messages = viewModel.messages.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        stickyHeader {
            Box(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
            ) {
                AsyncImage(
                    model = conference.value.imageUrl,
                    contentDescription = "conference banner",
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop,
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    BackButton(onClick = onBackClick)
                    if (viewModel.isUserManager()) {
                        ManageButton(onClick = { onManageClick(conference.value.id!!) })
                    }
                }
            }
        }
        item {
            Title(title = conference.value.title)
        }
        if (conference.value.endDateTime >= Instant.now().toEpochMilli()) { // If conference hasn't ended yet, show timer.
            item {
                Timer(conference.value.startDateTime, duration.value)
            }
        }
        item {
            AttendingCounter(peopleAttending = viewModel.attendingCount)
        }
        item {
            Attendance(onClick = { viewModel.toggleAttendance() }, isAttending = viewModel.isAttending)
        }
        item {
            Box( //TODO: Change colors of messages and add send message also make toggleable
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .background(
                        if (isSystemInDarkTheme()) DarkTertiaryColor else TertiaryColor,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row {
                        Text(
                            text = "Chat ",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 5.dp, bottom = 2.5.dp)
                        )
                        Text(
                            text = messages.value.size.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraLight,
                            modifier = Modifier.padding(top = 5.dp, bottom = 2.5.dp)
                        )
                    }
                    Messages(messages = messages.value, currentUser = viewModel.currentUser, conferenceOwnerId = conference.value.ownerId)
                    SendMessageCard(
                        textValue = viewModel.newMessage,
                        onTextChange = { viewModel.onNewMessageChange(it) },
                        onSendClick = {
                            viewModel.sendMessage()
                            viewModel.newMessage = ""
                                      },
                    )
                }
            }
        }
    }
}

@Composable
fun Title(title: String) {
    Text(
        text = title,
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .padding(top = 20.dp)
    )
}

@Composable
fun Timer(
    startDate: Long,
    duration: Duration,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .background(
                color = if (isSystemInDarkTheme()) DarkTertiaryColor else TertiaryColor,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        if (startDate > Instant.now().toEpochMilli()) {
            Text(
                text = "Starts in",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(top = 10.dp)
            )
        } else {
            Text(
                text = "Ends in",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(top = 10.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(bottom = 10.dp, start = 5.dp, end = 5.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${if (duration.toDaysPart() < 10) "0" else ""}${duration.toDaysPart()}",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Medium,
                    color = Blue,
                )
                Text(
                    text = "Days",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
            Text(
                text = " : ",
                fontSize = 50.sp,
                fontWeight = FontWeight.Medium,
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${if (duration.toHoursPart() < 10) "0" else ""}${duration.toHoursPart()}",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Medium,
                    color = Blue,
                )
                Text(
                    text = "Hours",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
            Text(
                text = " : ",
                fontSize = 50.sp,
                fontWeight = FontWeight.Medium,
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${if (duration.toMinutesPart() < 10) "0" else ""}${duration.toMinutesPart()}",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Medium,
                    color = Blue,
                )
                Text(
                    text = "Minutes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
            Text(
                text = " : ",
                fontSize = 50.sp,
                fontWeight = FontWeight.Medium,
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${if (duration.toSecondsPart() < 10) "0" else ""}${duration.toSecondsPart()}",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Medium,
                    color = Blue,
                )
                Text(
                    text = "Seconds",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
fun AttendingCounter(
    peopleAttending: Int,
){
    Text(
        text = "$peopleAttending people attending",
        fontSize = 16.sp,
        color = Blue,
        modifier = Modifier
            .padding(top = 20.dp)
    )
}

@Composable
fun Attendance(
    onClick: () -> Unit,
    isAttending: Boolean,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 20.dp)
    ) {
        Text(text = "Attending this conference?", fontSize = 20.sp)
            Text(text = "Let others know.", fontSize = 16.sp, fontWeight = FontWeight.Light)
            Icon(
                painter = painterResource(id = if (isAttending) R.drawable.ic_attendance_clicked else R.drawable.ic_attendance),
                contentDescription = "icon",
                tint = Blue,
                modifier = Modifier
                    .clickable(onClick = onClick)
            )
    }
}

@Composable
fun Messages(
    messages: List<ChatMessage>,
    currentUser: User,
    conferenceOwnerId: String,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        reverseLayout = true,
        modifier = Modifier
            .height(if (messages.size < 3) 0.dp else 220.dp)
            .padding(bottom = 10.dp)
            .fillMaxWidth()
    ) {
        items(
            items = messages,
            key = { message -> messages.indexOf(message) }
        ) {
            Message(
                message = it,
                user = currentUser,
                conferenceOwnerId = conferenceOwnerId
            )
        }
    }
}
