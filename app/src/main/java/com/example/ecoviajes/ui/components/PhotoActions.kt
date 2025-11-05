package com.example.ecoviajes.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ecoviajes.utils.createImageUri

@Composable
fun PhotoActions(
    onImagePicked: (Uri?) -> Unit
) {
    val context = LocalContext.current
    var preview by remember { mutableStateOf<Uri?>(null) }
    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        preview = uri
        onImagePicked(uri)
    }

    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { ok ->
        if (ok) {
            preview = cameraUri
            onImagePicked(cameraUri)
        }
    }

    Column(Modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = {
                pickImage.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }) {
                Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                Spacer(Modifier.width(6.dp)); Text("Galería")
            }
            OutlinedButton(onClick = {
                val uri = createImageUri(context)
                cameraUri = uri
                takePicture.launch(uri)
            }) {
                Icon(Icons.Default.AddAPhoto, contentDescription = null)
                Spacer(Modifier.width(6.dp)); Text("Cámara")
            }
        }

        preview?.let { uri ->
            Spacer(Modifier.height(8.dp))
            AsyncImage(
                model = uri,
                contentDescription = "Vista previa",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )
        }
    }
}
