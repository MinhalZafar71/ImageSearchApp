package com.sftech.imagesearchapp.util

import android.app.WallpaperManager
import android.content.Context
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Modern way to set wallpaper using Coil for efficient image loading.
 */
suspend fun setWallpaper(
    context: Context,
    imageUrl: String,
    applyToLock: Boolean = true,
    applyToHome: Boolean = true
) {
    // Run the entire operation on a background thread.
    withContext(Dispatchers.IO) {
        try {
            // 1. Use Coil to load the image efficiently.
            // Coil handles caching, down sampling, and memory management.
            val imageLoader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false) // Required for WallpaperManager
                .build()
            val drawable = imageLoader.execute(request).drawable
            val bitmap = drawable?.toBitmap()

            if (bitmap == null) {
                throw IllegalStateException("Failed to decode bitmap from image.")
            }

            // 2. Use WallpaperManager more efficiently.
            val wallpaperManager = WallpaperManager.getInstance(context)

            // Combine flags to set both wallpapers in one call.
            var flags = 0
            if (applyToHome) flags = flags or WallpaperManager.FLAG_SYSTEM
            if (applyToLock) flags = flags or WallpaperManager.FLAG_LOCK

            // Make a single call with the combined flags.
            if (flags > 0) {
                wallpaperManager.setBitmap(bitmap, null, true, flags)
            }

            // Switch back to the Main thread to show the Toast.
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Wallpaper set successfully!", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Failed to set wallpaper.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}