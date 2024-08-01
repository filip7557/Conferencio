package hr.ferit.filipcuric.conferencio.ui.conference

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import hr.ferit.filipcuric.conferencio.R
import hr.ferit.filipcuric.conferencio.navigation.EventDestination
import hr.ferit.filipcuric.conferencio.navigation.ModifyConferenceDestination
import hr.ferit.filipcuric.conferencio.ui.component.BlueButton
import hr.ferit.filipcuric.conferencio.ui.component.EventCard
import hr.ferit.filipcuric.conferencio.ui.component.Message
import hr.ferit.filipcuric.conferencio.ui.component.SendMessageCard
import hr.ferit.filipcuric.conferencio.ui.theme.Blue
import hr.ferit.filipcuric.conferencio.ui.theme.DarkTertiaryColor
import hr.ferit.filipcuric.conferencio.ui.theme.TertiaryColor
import java.time.Duration
import java.time.Instant

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ConferenceScreen(
    viewModel: ConferenceViewModel,
    onBackClick: () -> Unit,
    onManageClick: (String) -> Unit,
    onEventClick: (String) -> Unit,
    onAddEventClick: (String) -> Unit,
) {
    val conference = viewModel.conference.collectAsState()
    val duration = viewModel.duration.collectAsState()
    val events = viewModel.events.collectAsState()
    Log.d("EVENTS SCREEN", "Got events: ${events.value}")
    val messages = viewModel.messages.collectAsState()
    val authors = viewModel.messageAuthors.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        stickyHeader {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                            text = conference.value.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .basicMarquee(iterations = Int.MAX_VALUE)
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
                    actions = {
                        if (viewModel.isUserManager()) {
                            IconButton(
                                onClick = {
                                    onManageClick(
                                        ModifyConferenceDestination.createNavigation(
                                            conference.value.id!!
                                        )
                                    )
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_gear),
                                    contentDescription = "gear icon"
                                )
                            }
                        }
                    }
                )
                AsyncImage(
                    model = conference.value.imageUrl,
                    contentDescription = "conference banner",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)),
                    contentScale = ContentScale.Crop,
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 5.dp, end = 5.dp)
                ) {
                    ConferenceScreenState.entries.forEach {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(90.dp)
                        ) {
                            Text(
                                text = it.name.lowercase().replaceFirstChar { char ->
                                    if (char.isLowerCase()) char.titlecase() else char.toString()
                                },
                                fontSize = 18.sp,
                                fontWeight = if (it == viewModel.screenState) FontWeight.SemiBold else FontWeight.Light,
                                color = if (it == viewModel.screenState) Blue else if (isSystemInDarkTheme()) Color.White else Color.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .clickable {
                                        viewModel.onScreenStateClick(it)
                                    }
                            )
                            if (it == viewModel.screenState) {
                                Spacer(
                                    modifier = Modifier
                                        .size(6.dp)
                                )
                                Divider(
                                    color = Blue,
                                    thickness = 4.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
        when (viewModel.screenState) {
            ConferenceScreenState.OVERVIEW -> {
                item {
                    Title(title = conference.value.title)
                }
                if (conference.value.endDateTime >= Instant.now()
                        .toEpochMilli()
                ) { // If conference hasn't ended yet, show timer.
                    item {
                        Timer(conference.value.startDateTime, duration.value)
                    }
                }
                item {
                    AttendingCounter(peopleAttending = viewModel.attendingCount)
                }
                item {
                    Attendance(
                        onClick = { viewModel.toggleAttendance() },
                        isAttending = viewModel.isAttending
                    )
                }
            }

            ConferenceScreenState.EVENTS -> {
                if (viewModel.isUserManager()) {
                    item {
                        BlueButton(
                            modifier = Modifier
                                .padding(vertical = 20.dp),
                            text = "Add an event",
                            onClick = { onAddEventClick(viewModel.conference.value.id!!) }
                        )
                    }
                }
                if (events.value.isEmpty()) {
                    item {
                        Text(
                            text = "There are no events planned in this conference.",
                            fontWeight = FontWeight.Light,
                            modifier = Modifier
                                .padding(top = 30.dp, bottom = 10.dp)
                        )
                    }
                } else {
                    items(
                        items = events.value,
                        key = { event -> event.id!! }
                    ) {
                        EventCard(
                            event = it,
                            onClick = { eventId ->
                                onEventClick(
                                    EventDestination.createNavigation(
                                        eventId
                                    )
                                )
                            },
                            isOnEventScreen = false
                        )
                    }
                }
            }

            ConferenceScreenState.GALLERY -> {
                //TODO: Implement gallery
            }

            ConferenceScreenState.CHAT -> {
                item {
                    Box(
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
                            LazyColumn(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                reverseLayout = true,
                                modifier = Modifier
                                    .height(if (messages.value.isEmpty()) 0.dp else if (messages.value.size < 2) 110.dp else 220.dp)
                                    .padding(bottom = 10.dp)
                                    .fillMaxWidth()
                            ) {
                                items(
                                    items = messages.value,
                                    key = { message -> messages.value.indexOf(message) }
                                ) {
                                    Message(
                                        message = it,
                                        user = authors.value[messages.value.indexOf(it)],
                                        conferenceOwnerId = conference.value.ownerId
                                    )
                                }
                            }
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
    }
}

@Composable
fun Title(title: String) {
    Text(
        text = title,
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(top = 20.dp, start = 10.dp, end = 10.dp)
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
