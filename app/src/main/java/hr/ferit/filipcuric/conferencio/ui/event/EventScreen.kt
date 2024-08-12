package hr.ferit.filipcuric.conferencio.ui.event

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.filipcuric.conferencio.R
import hr.ferit.filipcuric.conferencio.model.Event
import hr.ferit.filipcuric.conferencio.model.File
import hr.ferit.filipcuric.conferencio.model.User
import hr.ferit.filipcuric.conferencio.navigation.ModifyEventDestination
import hr.ferit.filipcuric.conferencio.ui.component.BackButton
import hr.ferit.filipcuric.conferencio.ui.component.BlueButton
import hr.ferit.filipcuric.conferencio.ui.component.EventCard
import hr.ferit.filipcuric.conferencio.ui.component.ManageButton
import hr.ferit.filipcuric.conferencio.ui.component.Message
import hr.ferit.filipcuric.conferencio.ui.component.SendMessageCard
import hr.ferit.filipcuric.conferencio.ui.theme.Blue
import hr.ferit.filipcuric.conferencio.ui.theme.DarkTertiaryColor
import hr.ferit.filipcuric.conferencio.ui.theme.TertiaryColor
import java.time.Instant
import java.time.ZoneId

@Composable
fun EventScreen(
    viewModel: EventViewModel,
    onBackClick: () -> Unit,
    onManageClick: (String) -> Unit,
) {
    val event = viewModel.event.collectAsState()
    val files = viewModel.files.collectAsState()
    val messages = viewModel.messages.collectAsState()
    val authors = viewModel.messageAuthors.collectAsState()

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, bottom = 20.dp, top = 10.dp)
        ) {
            BackButton(onClick = onBackClick)
            if (viewModel.isUserManager() || viewModel.isUserHost()) {
                ManageButton(
                    onClick = {
                        onManageClick(
                            ModifyEventDestination.createNavigation(event.value.id!!)
                        )
                    }
                )
            }
        }
        EventCard(event = event.value, onClick = { /*Do nothing*/ }, isOnEventScreen = true)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 5.dp, end = 5.dp)
        ) {
            EventScreenState.entries.forEach {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(130.dp)
                ) {
                    Text(
                        text = it.name.replace('_', ' '),
                        fontSize = 16.sp,
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
        when (viewModel.screenState) {

            EventScreenState.OVERVIEW -> {
                Info(event = event.value, host = viewModel.host)
                AttendingCounter(peopleAttending = viewModel.attendingCount)
                Attendance(
                    onClick = { viewModel.toggleAttendance() },
                    isAttending = viewModel.isAttending
                )
            }

            EventScreenState.SHARED_FILES -> {
                if (viewModel.isUserManager()) {
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent(),
                        onResult = { uri: Uri? -> uri?.let { viewModel.onFileSelected(it) } }
                    )
                    BlueButton(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        text = "Add files",
                        onClick = {
                            launcher.launch("application/pdf")
                        }
                    )
                }
                val context = LocalContext.current
                SharedFiles(
                    files = files.value,
                    onFileClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                    }
                )
            }

            EventScreenState.CHAT -> {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    reverseLayout = true,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f)
                ) {
                    if (messages.value.isEmpty()) {
                        item {
                            Text(
                                text = "There are no messages in this chat.\nStart the conversation now.",
                                fontWeight = FontWeight.Light,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(top = 10.dp, bottom = 10.dp)
                            )
                        }
                    } else {
                        items(
                            items = messages.value,
                            key = { message -> messages.value.indexOf(message) }
                        ) {
                            Message(
                                message = it,
                                user = authors.value[messages.value.indexOf(it)],
                                isUserAuthor = authors.value[messages.value.indexOf(it)] == viewModel.user,
                                conferenceOwnerId = event.value.hostId
                            )
                        }
                    }
                }
                SendMessageCard(
                    textValue = viewModel.newMessage,
                    onTextChange = { viewModel.onNewMessageChange(it) },
                    onSendClick = {
                        viewModel.sendMessage()
                        viewModel.newMessage = ""
                    }
                )
            }
        }
    }
}

@Composable
fun Info(
    event: Event,
    host: User,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(bottom = 20.dp, top = 10.dp)
            .fillMaxWidth()
            .background(
                if (isSystemInDarkTheme()) DarkTertiaryColor else TertiaryColor,
                RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            text = "Info",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(top = 10.dp)
        )
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Hosted by:",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraLight,
            )
            Text(
                text = "${host.fullname}, ${host.position}${if (host.position != "" && host.company != "")" @ " else ""}${host.company}",
                fontSize = 14.sp,
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Location:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraLight,
                )
                Text(
                    text = event.location,
                    fontSize = 14.sp,
                )
            }
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Date & Time:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraLight,
                )
                val datetime = Instant.ofEpochMilli(event.dateTime).atZone(ZoneId.systemDefault())
                Text(
                    text = "${if (datetime.dayOfMonth < 10) '0' else ""}${datetime.dayOfMonth}/${if (datetime.monthValue < 10) '0' else ""}${datetime.monthValue}/${datetime.year} | ${if (datetime.hour < 10) '0' else ""}${datetime.hour}:${if (datetime.minute < 10) '0' else ""}${datetime.minute}",
                    fontSize = 14.sp,
                )
            }
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Duration",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraLight,
                )
                Text(
                    text = "${event.duration} minutes",
                    fontSize = 14.sp,
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Description",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraLight,
            )
            Text(
                text = event.description,
                fontSize = 14.sp,
            )
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
        Text(text = "Attending this event?", fontSize = 20.sp)
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
fun SharedFiles(
    files: List<File>,
    onFileClick: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 10.dp)
            .fillMaxWidth()
            .background(
                if (isSystemInDarkTheme()) DarkTertiaryColor else TertiaryColor,
                RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            text = "Shared files",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(vertical = 10.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
        ) {
            if (files.isEmpty()) {
                Text(
                    text = "The host hasn't shared any files yet.",
                    fontWeight = FontWeight.Light,
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                )
            } else {
                for (file in files) {
                    Text(
                        text = file.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraLight,
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .clickable { onFileClick(file.link) }
                    )
                }
            }
        }
    }
}
