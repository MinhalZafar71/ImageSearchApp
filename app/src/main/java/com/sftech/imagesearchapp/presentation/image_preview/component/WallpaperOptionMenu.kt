package com.sftech.imagesearchapp.presentation.image_preview.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.sftech.imagesearchapp.R

import android.app.WallpaperManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun WallpaperOptionMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onOptionSelected: (applyToHome: Boolean, applyToLock: Boolean) -> Unit,
    anchor: @Composable () -> Unit
) {

    Box(
        modifier = Modifier.wrapContentSize(Alignment.TopStart)
    ) {
        // The anchor composable (your button) goes here
        anchor()

        // The DropdownMenu is positioned relative to the Box
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest
        ) {
            DropdownMenuItem(
                text = { Text("Home Screen") },
                leadingIcon = {
                    Icon(painter = painterResource(R.drawable.outline_mobile), contentDescription = "Home Screen")
                },
                onClick = {
                    onOptionSelected(true, false)
                    onDismissRequest() // Close the menu
                }
            )
            DropdownMenuItem(
                text = { Text("Lock Screen") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Lock Screen")
                },
                onClick = {
                    onOptionSelected(false, true)
                    onDismissRequest()
                }
            )
            DropdownMenuItem(
                text = { Text("Both") },
                leadingIcon = {
                    Icon(painter = painterResource(R.drawable.outline_check_box), contentDescription = "Both Screens")
                },
                onClick = {
                    onOptionSelected(true, true)
                    onDismissRequest()
                }
            )
        }
    }


}


