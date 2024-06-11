package hr.ferit.filipcuric.conferencio.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepositoryImpl
import hr.ferit.filipcuric.conferencio.data.repository.UserRepositoryImpl
import hr.ferit.filipcuric.conferencio.ui.theme.Blue

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
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
                .background(if (isActiveSelected) Blue else Color(0f, 0f, 0f, 0f), shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                .border(2.dp, Blue, shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
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
                .background(if (!isActiveSelected) Blue else Color(0f, 0f, 0f, 0f), shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
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

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(viewModel = HomeViewModel(userRepository = UserRepositoryImpl(), conferenceRepository = ConferenceRepositoryImpl()))
}
