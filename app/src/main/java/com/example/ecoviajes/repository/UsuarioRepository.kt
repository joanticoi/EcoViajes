package com.example.ecoviajes.repository

import com.example.ecoviajes.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class UsuarioRepository {
    private val db = FirebaseFirestore.getInstance()

    // 🔹 Actualizar SOLO el nombre
    suspend fun actualizarNombreUsuario(correo: String, nuevoNombre: String): Boolean {
        return try {
            println("DEBUG: buscando usuario con correo = $correo")
            val snapshot = db.collection("usuario")
                .whereEqualTo("correo", correo)
                .get()
                .await()

            println("DEBUG: encontrados ${snapshot.size()} documentos")

            if (snapshot.isEmpty) return false

            snapshot.documents.forEach { doc ->
                println("DEBUG: actualizando doc ${doc.id} a nombre = $nuevoNombre")
                doc.reference.update("nombre", nuevoNombre).await()
            }
            true
        } catch (e: Exception) {
            println("DEBUG ERROR actualizarNombreUsuario: ${e.message}")
            false
        }
    }


    suspend fun registroUsuario(correo: String, clave: String, nombre: String): Boolean {
        return try {
            // Verificar si el correo ya existe
            val querySnapshot = db.collection("usuario")
                .whereEqualTo("correo", correo)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                // Si ya existe el correo, no registrar
                return false
            }

            val userData = mapOf(
                "correo" to correo,
                "clave" to clave,
                "nombre" to nombre,
                "rol" to "cliente",
                "fechaCreacion" to getCurrentDate()
            )

            db.collection("usuario").add(userData).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // 🔹 Obtener datos del usuario por correo
    suspend fun obtenerUsuarioPorCorreo(correo: String): User? {
        return try {
            val snapshot = db.collection("usuario")
                .whereEqualTo("correo", correo)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                val doc = snapshot.documents[0]
                User(
                    correo = doc.getString("correo") ?: "",
                    clave = doc.getString("clave") ?: "",
                    nombre = doc.getString("nombre") ?: "",
                    rol = doc.getString("rol") ?: "cliente"
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
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

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}
