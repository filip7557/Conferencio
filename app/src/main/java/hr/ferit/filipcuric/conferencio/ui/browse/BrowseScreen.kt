package hr.ferit.filipcuric.conferencio.ui.browse

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
import hr.ferit.filipcuric.conferencio.model.User
import hr.ferit.filipcuric.conferencio.navigation.ConferenceDestination
import hr.ferit.filipcuric.conferencio.ui.component.ConferenceCard

@Composable
fun BrowseScreen(
    viewModel: BrowseViewModel,
    onConferenceClick: (String) -> Unit,
) {
    val conferences = viewModel.conferences.collectAsState()
    val owners = viewModel.owners.collectAsState()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Subtitle()
        }
        if (conferences.value.isEmpty()) {
            item {
                Text(
                    text = "There are no upcoming conferences yet.",
                    fontWeight = FontWeight.Light,
                    modifier = Modifier
                        .padding(top = 10.dp)
                )
            }
        } else {
            items(
                items = conferences.value,
                key = { conference -> conference.id!! }
            ) {

                ConferenceCard(
                    conference = it,
                    user = if (owners.value.size < conferences.value.size) User() else owners.value[conferences.value.indexOf(it)],
                    onClick = { onConferenceClick(ConferenceDestination.createNavigation(it.id!!)) }
                )
            }
        }
    }
}

@Composable
fun Subtitle() {
    Text(
        text = "Browse upcoming conferences.",
        fontSize = 20.sp,
        fontWeight = FontWeight.Thin,
        modifier = Modifier
            .padding(bottom = 20.dp, top = 20.dp)
    )
}

@Preview
@Composable
fun BrowseScreenPreview() {
    BrowseScreen(viewModel = BrowseViewModel(ConferenceRepositoryImpl(), UserRepositoryImpl()), onConferenceClick = {})
}
