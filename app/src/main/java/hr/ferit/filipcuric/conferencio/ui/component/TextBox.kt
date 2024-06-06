package hr.ferit.filipcuric.conferencio.ui.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import hr.ferit.filipcuric.conferencio.ui.theme.Blue
import hr.ferit.filipcuric.conferencio.ui.theme.DarkOnTertiaryColor
import hr.ferit.filipcuric.conferencio.ui.theme.DarkTertiaryColor

@Composable
fun TextBox(
    label: String,
    value: String,
    keyboardOptions: KeyboardOptions =  KeyboardOptions(
        keyboardType = KeyboardType.Text
    ),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit,
) {
    if(isSystemInDarkTheme()) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(text = label)
            },
            singleLine = true,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Blue,
                focusedLabelColor = Blue,
                cursorColor = Blue,
                unfocusedContainerColor = DarkTertiaryColor,
                focusedContainerColor = DarkTertiaryColor,
                focusedTextColor = DarkOnTertiaryColor,
                unfocusedTextColor = DarkOnTertiaryColor,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp)
        )
    } else {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(text = label)
            },
            singleLine = true,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Blue,
                focusedLabelColor = Blue,
                cursorColor = Blue,
                unfocusedContainerColor = Color(0xC9C9C9C9),
                focusedContainerColor = Color(0xC9C9C9C9),
                focusedTextColor = Color(28, 28, 28),
                unfocusedTextColor = Color(28, 28, 28),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp)
        )
    }
}
