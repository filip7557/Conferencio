package hr.ferit.filipcuric.conferencio.ui.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepositoryImpl
import hr.ferit.filipcuric.conferencio.data.repository.UserRepositoryImpl
import hr.ferit.filipcuric.conferencio.navigation.ConferenceDestination
import hr.ferit.filipcuric.conferencio.ui.component.ConferenceCard
import hr.ferit.filipcuric.conferencio.ui.component.TextBox

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onConferenceClick: (String) -> Unit,
) {
    val foundConferences = viewModel.foundConferences.collectAsState()
    val searchValue = viewModel.searchValue.collectAsState()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Subtitle()
            TextBox(
                label = "Search by title",
                value = searchValue.value,
                onValueChange =  { viewModel.onSearchValueChange(it) }
            )
        }
        if (foundConferences.value.isEmpty() && searchValue.value.length >= 3)
            item {
                Text(
                    text = "There are no conferences meeting your search criteria.",
                    fontWeight = FontWeight.Light,
                    modifier = Modifier
                        .padding(top = 10.dp)
                )
            }
        else {
            items(
                items = foundConferences.value,
                key = { conference -> conference.id!! }
            ) {
                ConferenceCard(
                    conference = it,
                    user = viewModel.getConferenceOwnerByUserId(it.ownerId),
                    onClick = { onConferenceClick(ConferenceDestination.createNavigation(it.id!!)) }
                )
            }
        }
    }
}

@Composable
fun Subtitle() {
    Text(
        text = "Search for conferences.",
        fontSize = 20.sp,
        fontWeight = FontWeight.Thin,
        modifier = Modifier
            .padding(bottom = 20.dp, top = 20.dp)
    )
}

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen(viewModel = SearchViewModel(userRepository = UserRepositoryImpl(), conferenceRepository = ConferenceRepositoryImpl())) {}
}
