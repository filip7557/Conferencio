package hr.ferit.filipcuric.conferencio.ui.browse

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.ferit.filipcuric.conferencio.data.repository.ConferenceRepositoryImpl
import hr.ferit.filipcuric.conferencio.data.repository.UserRepositoryImpl
import hr.ferit.filipcuric.conferencio.ui.component.ConferenceCard

@Composable
fun BrowseScreen(
    viewModel: BrowseViewModel,
    onConferenceClick: (String) -> Unit,
) {
    val conferences by viewModel.conferences.collectAsState()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Subtitle()
        }
        items(
            items = conferences,
            key = { conference -> conference.id!!}
        ) {
            ConferenceCard(
                conference = it,
                user = viewModel.getConferenceOwnerByUserId(it.ownerId),
                onClick = { onConferenceClick(it.id!!) /*TODO: Destination.createNavigation*/ }
            )
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
