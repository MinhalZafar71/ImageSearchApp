package com.sftech.imagesearchapp.presentation.image_preview.component

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.sftech.imagesearchapp.util.saveImageFromUrl
import com.sftech.imagesearchapp.util.showToast
import kotlinx.coroutines.launch

@Composable
fun DownloadHandler(
    context: Context,
    downloadRequest: DownloadRequest?,
    onDownloadHandled: () -> Unit,
) {
    if (downloadRequest == null) {
        return
    }

    val scope = rememberCoroutineScope()

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->

            if (isGranted) {
                scope.launch {
                    saveImageFromUrl(context, downloadRequest.imageUrl, downloadRequest.fileName)
                }
            } else {
                showToast(context, "Storage permission is required to download images.")
            }
            onDownloadHandled()
        }

    LaunchedEffect(downloadRequest) {
        val fileName = "downloaded_${System.currentTimeMillis()}.jpg"
        val request = DownloadRequest(downloadRequest.imageUrl, fileName)

        // For Android 10 (Q) and above, no permission is needed to save to public collections.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveImageFromUrl(context, request.imageUrl, request.fileName)
            onDownloadHandled()
        } else {
            // For older versions, we must request WRITE_EXTERNAL_STORAGE
            permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
}

data class DownloadRequest(
    val imageUrl: String,
    val fileName: String,
)
