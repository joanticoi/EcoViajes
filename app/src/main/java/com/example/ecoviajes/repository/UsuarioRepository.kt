package com.example.ecoviajes.repository

import com.example.ecoviajes.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class UsuarioRepository {
    private val db = FirebaseFirestore.getInstance()

    // 🔹 Registrar usuario en colección "usuario"
    suspend fun registroUsuario(
        correo: String,
        clave: String,
        nombre: String,
        telefono: String,
        foto: String = ""
    ): Boolean{
        return try {
            println("DEBUG registroUsuario: intentando registrar $correo")

            // Verificar si el correo ya existe
            val querySnapshot = db.collection("usuario")
                .whereEqualTo("correo", correo)
                .get()
                .await()

            println("DEBUG registroUsuario: encontrados ${querySnapshot.size()} usuarios con ese correo")

            if (!querySnapshot.isEmpty) {
                println("DEBUG registroUsuario: correo ya existe, no se registra")
                return false
            }

            val userData = mapOf(
                "correo" to correo,
                "clave" to clave,
                "nombre" to nombre,
                "rol" to "cliente",
                "telefono" to telefono,
                "foto" to foto,
                "fechaCreacion" to getCurrentDate()
            )

            db.collection("usuario").add(userData).await()
            println("DEBUG registroUsuario: usuario registrado correctamente en Firestore")
            true
        } catch (e: Exception) {
            println("DEBUG ERROR registroUsuario: ${e.message}")
            false
        }
    }

    // 🔹 Obtener datos del usuario por correo
    suspend fun obtenerUsuarioPorCorreo(correo: String): User? {
        return try {
            println("DEBUG obtenerUsuarioPorCorreo: buscando $correo")
            val snapshot = db.collection("usuario")
                .whereEqualTo("correo", correo)
                .get()
                .await()

            println("DEBUG obtenerUsuarioPorCorreo: encontrados ${snapshot.size()} docs")

            if (!snapshot.isEmpty) {
                val doc = snapshot.documents[0]
                User(
                    correo = doc.getString("correo") ?: "",
                    clave = doc.getString("clave") ?: "",
                    nombre = doc.getString("nombre") ?: "",
                    rol = doc.getString("rol") ?: "cliente",
                    telefono = doc.getString("telefono") ?: "",
                    foto = doc.getString("foto") ?: ""
                )
            } else {
                null
            }
        } catch (e: Exception) {
            println("DEBUG ERROR obtenerUsuarioPorCorreo: ${e.message}")
            null
        }
    }

    // 🔹 Actualizar SOLO el nombre
    suspend fun actualizarNombreUsuario(correo: String, nuevoNombre: String): Boolean {
        return try {
            val snapshot = db.collection("usuario")
                .whereEqualTo("correo", correo)
                .get()
                .await()

            if (snapshot.isEmpty) return false

            snapshot.documents.forEach { doc ->
                doc.reference.update("nombre", nuevoNombre).await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    // 🔹 Eliminar usuario de la colección "usuario"
    suspend fun eliminarUsuario(correo: String): Boolean {
        return try {
            val snapshot = db.collection("usuario")
                .whereEqualTo("correo", correo)
                .get()
                .await()

            snapshot.documents.forEach { doc ->
                doc.reference.delete().await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }
    suspend fun actualizarPerfil(
        correo: String,
        nuevoNombre: String,
        nuevoTelefono: String,
        nuevaFotoUrl: String
    ): Boolean {
        return try {
            val snapshot = db.collection("usuario")
                .whereEqualTo("correo", correo)
                .get()
                .await()

            if (snapshot.isEmpty) return false

            snapshot.documents.forEach { doc ->
                doc.reference.update(
                    mapOf(
                        "nombre" to nuevoNombre,
                        "telefono" to nuevoTelefono,
                        "foto" to nuevaFotoUrl
                    )
                ).await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }
    suspend fun actualizarClaveUsuario(correo: String, nuevaClave: String): Boolean {
        return try {
            val snapshot = db.collection("usuario")
                .whereEqualTo("correo", correo)
                .get()
                .await()

            if (snapshot.isEmpty) return false

            snapshot.documents.forEach { doc ->
                doc.reference.update("clave", nuevaClave).await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }




    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}
