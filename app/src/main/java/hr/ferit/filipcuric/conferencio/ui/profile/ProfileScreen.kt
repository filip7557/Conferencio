package hr.ferit.filipcuric.conferencio.ui.profile

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
        Picture(imageUrl = viewModel.user.imageUrl)
        Fullname(fullname = viewModel.user.fullname)
        CompanyAndPosition(company = viewModel.user.company, position = viewModel.user.position)
        ConferencesInfo(organized = viewModel.organized, attended = viewModel.attended)
    }
}

@Composable
fun Picture(
    imageUrl: String,
) {
    Box(
        modifier = Modifier
            .padding(top = 20.dp)
            .size(300.dp)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                shape = CircleShape,
                color = Blue,
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
    )
}

@Composable
fun ConferencesInfo(
    organized: Int,
    attended: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
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
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .size(height = 65.dp, width = 1.dp)
                .background(Color(94, 93, 93))
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
