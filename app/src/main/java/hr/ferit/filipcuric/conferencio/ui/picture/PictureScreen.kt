package hr.ferit.filipcuric.conferencio.ui.picture

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import hr.ferit.filipcuric.conferencio.R
import hr.ferit.filipcuric.conferencio.ui.component.BlueButton
import hr.ferit.filipcuric.conferencio.ui.theme.Blue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun PictureScreen(
    onBackClick: () -> Unit,
    viewModel: PictureViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    text = "Picture",
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
            windowInsets = WindowInsets.statusBars
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val pagerState = rememberPagerState(initialPage = viewModel.pictures.indexOf(viewModel.pictures.first {p-> p.id == viewModel.pictureId}))
            HorizontalPager(
                count = viewModel.pictures.size,
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier
                    .height(400.dp)
            ) { page ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .graphicsLayer {
                            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                            lerp(
                                start = ScaleFactor(0.85f, 0.85f),
                                stop = ScaleFactor(1f, 1f),
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).also { scale ->
                                scaleX = scale.scaleX
                                scaleY = scale.scaleY
                            }

                            alpha = lerp(
                                start = ScaleFactor(0.5f, 0.5f),
                                stop = ScaleFactor(1f, 1f),
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).scaleX
                        }
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize(),
                        model = viewModel.pictures[page].imageUrl,
                        contentDescription = "picture",
                        contentScale = ContentScale.Fit
                    )
                }
            }
            val context = LocalContext.current
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
                putExtra(Intent.EXTRA_TITLE, "${System.currentTimeMillis()}.jpg")
            }
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            val scope = rememberCoroutineScope()

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                val data = result.data
                val uri = data?.data
                uri?.let {
                    context.contentResolver.openOutputStream(it)?.let { outputStream ->
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                Log.d("SAVE PICTURE","outputStream: $outputStream")

                                outputStream.write(viewModel.picturesByteArray[pagerState.currentPage])
                                outputStream.flush()
                                outputStream.close()
                            }
                        }
                        Toast.makeText(context, "Picture downloaded", Toast.LENGTH_LONG).show()
                    }
                }
            }
            BlueButton(
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 10.dp),
                text = "Download",
                onClick = {
                    launcher.launch(intent)
                }
            )
        }
    }
}
