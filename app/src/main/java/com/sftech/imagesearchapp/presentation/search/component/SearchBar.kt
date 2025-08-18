package com.sftech.imagesearchapp.presentation.search.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sftech.imagesearchapp.R

@Composable
fun ImageSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    onClear: () -> Unit = {},
    onVoiceClick: () -> Unit = {},
    onSearchIme: () -> Unit = {}
) {
    val shape = RoundedCornerShape(28.dp)

    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchIme() }),
        cursorBrush = SolidColor(Color.Black),
        textStyle = LocalTextStyle.current.copy(
            color = Color.Black,
            fontSize = 18.sp
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = modifier
                    .height(52.dp)
                    .fillMaxWidth()
                    .border(1.5.dp, Color.Black, shape)
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Leading search icon
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Black,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(Modifier.width(12.dp))

                // Text / Placeholder
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color(0xFF6F6F6F),
                            fontSize = 18.sp
                        )
                    }
                    innerTextField()
                }

                // Clear button (shows only when text exists)
                AnimatedVisibility(visible = query.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onClear() }
                    )
                }

                // Vertical divider
                Box(
                    Modifier
                        .padding(horizontal = 12.dp)
                        .width(1.dp)
                        .height(24.dp)
                        .background(Color.Black.copy(alpha = 0.6f))
                )

                // Mic icon
                Icon(
                    painter = painterResource(id = R.drawable.ic_google_voice),
                    contentDescription = "Voice search",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { onVoiceClick() }
                )
            }
        }
    )
}
