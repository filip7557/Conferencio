package hr.ferit.filipcuric.conferencio.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import hr.ferit.filipcuric.conferencio.R
import hr.ferit.filipcuric.conferencio.model.User
import hr.ferit.filipcuric.conferencio.ui.theme.Blue

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Title()
        Picture(imageUrl = viewModel.user.imageUrl)
        Fullname(fullname = viewModel.user.fullname)
        CompanyAndPosition(company = viewModel.user.company, position = viewModel.user.position)
        ConferencesInfo(organized = viewModel.organized, attended = viewModel.attended)
    }
}

@Composable
fun Title() {
    Text(
        text = "Profile",
        fontSize = 48.sp,
        fontWeight = FontWeight.Medium,
        color = Blue,
        modifier = Modifier
            .padding(bottom = 30.dp)
    )
}

@Composable
fun Picture(
    imageUrl: String,
) {
    Box(
        modifier = Modifier
            .size(300.dp)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                shape = CircleShape,
                brush = Brush.horizontalGradient(colors = listOf(Blue, Blue))
            )
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "profile picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
fun Fullname(
    fullname: String,
) {
    Text(
        text = fullname,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .padding(top = 30.dp)
    )
}

@Composable
fun CompanyAndPosition(
    company: String,
    position: String,
) {
    Text(
        text = "$position @ $company",
        fontSize = 18.sp,
        fontWeight = FontWeight.ExtraLight,
        modifier = Modifier
            .padding(bottom = 30.dp)
    )
}

@Composable
fun ConferencesInfo(
    organized: Int,
    attended: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = organized.toString(),
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Organized",
                fontSize = 16.sp,
                fontWeight = FontWeight.Thin
            )
        }
        Image(
            painter = painterResource(id = R.drawable.divider),
            contentDescription = "separator",
            modifier = Modifier
                .size(width = 50.dp, height = 100.dp) //TODO: Fix size of separator
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = attended.toString(),
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Attended",
                fontSize = 16.sp,
                fontWeight = FontWeight.Thin
            )
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    val user = User(
        fullname = "Filip Ćurić",
        company = "FERIT",
        position = "Student",
        email = "filip.curic6@yahoo.com",
        imageUrl = "https://images.ctfassets.net/h6goo9gw1hh6/2sNZtFAWOdP1lmQ33VwRN3/24e953b920a9cd0ff2e1d587742a2472/1-intro-photo-final.jpg?w=1200&h=992&fl=progressive&q=70&fm=jpg"
    )

    val viewModel = ProfileViewModel()
    viewModel.user = user
    
    ProfileScreen(viewModel = viewModel)
}
