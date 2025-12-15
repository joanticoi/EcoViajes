package com.example.ecoviajes.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class StorageRepository {
    private val storage = FirebaseStorage.getInstance().reference

    suspend fun subirFotoPerfil(uri: Uri): String {
        val ref = storage.child("perfil/${UUID.randomUUID()}.jpg")
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }
}
