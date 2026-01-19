package com.sftech.imagesearchapp.presentation.search.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sftech.imagesearchapp.R
import com.sftech.imagesearchapp.presentation.ui.theme.TextColor

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




@Composable
fun CustomSearchBar(
    query: String,
    isActive: Boolean,
    onQueryChange: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onVoiceClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    // Auto-focus when active becomes true
    LaunchedEffect(isActive) {
        if (isActive) {
            focusRequester.requestFocus()
        }
    }

    val outerPaddingHorizontal = if (isActive) 0.dp else 16.dp
    val outerPaddingTop = if (isActive) 0.dp else 16.dp
    val outerPaddingBottom = if (isActive) 0.dp else 8.dp
    val barHeight = if (isActive) 56.dp else 50.dp
    val cornerRadius = if (isActive) 0.dp else 25.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = outerPaddingTop,
                start = outerPaddingHorizontal,
                end = outerPaddingHorizontal,
                bottom = outerPaddingBottom
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
                .clip(RoundedCornerShape(cornerRadius))
                .background(Color(0xFFF1F4F9))
                .clickable { if (!isActive) onActiveChange(true) }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isActive) {
                IconButton(onClick = onBackClick, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextColor)
                }
            } else {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = TextColor, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty() && !isActive) {
                    Text("Search Wallpaper", color = TextColor, fontSize = 16.sp)
                }

                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    enabled = isActive,
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                    cursorBrush = SolidColor(Color(0xFFE6E9E8)),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    decorationBox = { innerTextField ->
                        if (isActive && query.isEmpty()) {
                            Text("Search Wallpaper", color = TextColor, fontSize = 16.sp)
                        }
                        innerTextField()
                    }
                )
            }

            // Icons on the right
            if (isActive) {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }, modifier = Modifier.size(20.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextColor)
                    }
                } else {
                    // Show Mic if active but empty
                    IconButton(onClick = onVoiceClick, modifier = Modifier.size(24.dp)) {
                        Icon(painter = painterResource(id = R.drawable.ic_google_voice), contentDescription = "Voice Search", tint = TextColor)
                    }
                }
            }
        }
    }
}