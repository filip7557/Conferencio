package hr.ferit.filipcuric.conferencio.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.filipcuric.conferencio.R
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepositoryImpl
import hr.ferit.filipcuric.conferencio.data.repository.UserRepositoryImpl
import hr.ferit.filipcuric.conferencio.ui.component.ConferenceCard
import hr.ferit.filipcuric.conferencio.ui.theme.Blue

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onConferenceClick: (String) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        item {
            Title()
            ActivePastSwitcher(
                isActiveSelected = viewModel.isActiveSelected,
                onActiveClick = { viewModel.onActiveClick() },
                onPastClick = { viewModel.onPastClick() }
            )
            OrganizedHeader(
                isToggled = viewModel.isOrganizedToggled,
                onClick = {
                    viewModel.toggleOrganized()
                }
            )
        }
        if (viewModel.isOrganizedToggled) {
            items(
                items = viewModel.organizedConferences,
                key = { conference -> conference.id!! }
            ) {
                ConferenceCard(
                    conference = it,
                    user = viewModel.currentUser,
                    onClick = { onConferenceClick(it.id!!) /*TODO: Destination.createNavigation*/}
                )
            }
        }
        item {
            AttendingHeader(
                isToggled = viewModel.isAttendingToggled,
                onClick = { viewModel.toggleAttending() }
            )
        }
        if (viewModel.isAttendingToggled) {
            items(
                items = viewModel.attendingConferences,
                key = { conference -> conference.id!! }
            ) {
                ConferenceCard(
                    conference = it,
                    user = viewModel.currentUser,
                    onClick = { onConferenceClick(it.id!!) /*TODO: Destination.createNavigation*/}
                )
            }
        }
    }

}

@Composable
fun Title() {
    Text(
        text = "My Conferences",
        fontSize = 48.sp,
        fontWeight = FontWeight.Medium,
        color = Blue,
        modifier = Modifier
            .padding(bottom = 15.dp)
    )
}

@Composable
fun ActivePastSwitcher(
    isActiveSelected: Boolean,
    onActiveClick: () -> Unit,
    onPastClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(25.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight()
                .background(
                    if (isActiveSelected) Blue else Color(0f, 0f, 0f, 0f),
                    shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
                )
                .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                .border(
                    2.dp,
                    Blue,
                    shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
                )
                .clickable(onClick = onActiveClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Active",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = if (isActiveSelected) MaterialTheme.colorScheme.onPrimary else Blue
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    if (!isActiveSelected) Blue else Color(0f, 0f, 0f, 0f),
                    shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
                )
                .clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                .border(2.dp, Blue, shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                .clickable(onClick = onPastClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Past",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = if (!isActiveSelected) MaterialTheme.colorScheme.onPrimary else Blue
            )
        }
    }
}

@Composable
fun OrganizedHeader(
    isToggled: Boolean,
    onClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Text(
            text = "Organized",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_expand),
            contentDescription = "expand",
            tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
            modifier = Modifier
                .rotate(if (isToggled) 0f else 180f)
                .clickable(onClick = onClick)
                .padding(top = 2.dp)
        )
    }
}

@Composable
fun AttendingHeader(
    isToggled: Boolean,
    onClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Text(
            text = "Attending",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_expand),
            contentDescription = "expand",
            tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
            modifier = Modifier
                .rotate(if (isToggled) 0f else 180f)
                .clickable(onClick = onClick)
                .padding(top = 2.dp)
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(viewModel = HomeViewModel(userRepository = UserRepositoryImpl(), conferenceRepository = ConferenceRepositoryImpl()), onConferenceClick = { Log.d("CONF CLICK", "Clicked on conference with id $it") })
}
