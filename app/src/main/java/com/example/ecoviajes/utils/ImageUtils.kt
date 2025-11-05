package com.example.ecoviajes.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun createImageUri(context: Context): Uri {
    val dir = File(context.cacheDir, "images")
    if (!dir.exists()) dir.mkdirs()
    val file = File.createTempFile("ecoviajes_", ".jpg", dir)
    val authority = "${context.packageName}.fileprovider"
    return FileProvider.getUriForFile(context, authority, file)
}
