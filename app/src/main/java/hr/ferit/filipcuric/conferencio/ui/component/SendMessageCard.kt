package hr.ferit.filipcuric.conferencio.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hr.ferit.filipcuric.conferencio.R
import hr.ferit.filipcuric.conferencio.ui.theme.Blue

@Composable
fun SendMessageCard(
    textValue: String,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            .fillMaxWidth()
            .background(
                if (isSystemInDarkTheme()) Color(40, 40, 41) else Color(166, 166, 166),
                RoundedCornerShape(8.dp)
            )
    ) {
        TextField(
            value = textValue,
            onValueChange = { onTextChange(it) },
            label = { Text(text = "Chat...") },
            singleLine = false,
            maxLines = 5,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences,
            ),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = if (isSystemInDarkTheme()) Color(40, 40, 41) else Color(166, 166, 166),
                unfocusedContainerColor = if (isSystemInDarkTheme()) Color(40, 40, 41) else Color(166, 166, 166),
                cursorColor = Blue,
                focusedLabelColor = Blue,
            ),
            modifier = Modifier
                .fillMaxWidth(0.85f)
        )
        IconButton(
            onClick = onSendClick,
            modifier = Modifier
                .clickable(onClick = onSendClick)
                .fillMaxWidth(),
            enabled = textValue.isNotEmpty(),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = Blue,
                disabledContentColor = Color.Gray
            )
        ) {
            Icon(
            painter = painterResource(id = R.drawable.ic_send),
            contentDescription = "send icon"
            )
        }
    }
}
