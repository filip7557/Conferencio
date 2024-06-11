package hr.ferit.filipcuric.conferencio.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            .padding(horizontal = 10.dp)
    ) {
        item {
            Title()
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

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(viewModel = HomeViewModel(userRepository = UserRepositoryImpl(), conferenceRepository = ConferenceRepositoryImpl()))
}
