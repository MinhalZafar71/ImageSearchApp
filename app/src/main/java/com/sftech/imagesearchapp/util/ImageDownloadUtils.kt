package com.sftech.imagesearchapp.util

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

fun downloadImage(
    context: Context,
    imageUrl: String,
    fileName: String,
) {
    try {
        val request =
            DownloadManager.Request(imageUrl.toUri()).apply {
                setTitle("Downloading Image")
                setDescription("Saving image to gallery...")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fileName)
                setAllowedOverMetered(true)
                setAllowedOverRoaming(true)
            }

        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)

        showToast(context, "Download started")
    } catch (e: Exception) {
        e.printStackTrace()
        showToast(context, "Download failed")
    }
}

suspend fun saveImageFromUrl(
    context: Context,
    imageUrl: String,
    fileName: String,
) {
    withContext(Dispatchers.IO) {
        try {
            val resolver = context.contentResolver
            val imageUri: Uri?

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues =
                    ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }
                imageUri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            } else {
                val imagesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val image = java.io.File(imagesDir, fileName)
                imageUri = Uri.fromFile(image)
            }

            if (imageUri == null) {
                throw NullPointerException("Failed to create new MediaStore record.")
            }

            resolver.openOutputStream(imageUri)?.use { outputStream ->
                val url = URL(imageUrl)
                val inputStream = url.openStream()
                inputStream.copyTo(outputStream)
                inputStream.close()
            }

            withContext(Dispatchers.Main) {
                showToast(context, "Image saved successfully!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                showToast(context, "Failed to save image.")
            }
        }
    }
}
