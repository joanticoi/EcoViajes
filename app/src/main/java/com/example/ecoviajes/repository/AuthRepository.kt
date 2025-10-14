package com.example.ecoviajes.repository

import com.example.ecoviajes.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import  kotlinx.coroutines.tasks.await

class AuthRepository{
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()


    suspend fun login(correo: String, clave: String): User?{
        return try{
            //int
            val resultado = auth.signInWithEmailAndPassword( correo,clave).await()
            val user = resultado.user
            if (user != null){
                getUserFromFirestore(user.uid, correo) ?: User (
                    correo = correo,
                    nombre = if(correo == "admin@ecoviajes.cl") "administrador" else "usuario",
                    rol = if (correo == "admin@ecoviajes.cl" ) "admin" else "cliente"
                )
            }else null

        } catch (e: Exception){
            loginWithFirestore( correo, clave)
        }
    }
    private suspend fun getUserFromFirestore(uid: String, correo: String): User? {
        return try {
            val doc = db.collection("usuario").document().get().await()
            if (doc.exists()) {
                User(
                    correo = doc.getString("correo") ?: correo,
                    nombre = doc.getString("nombre") ?: "Usuario",
                    rol = doc.getString("rol") ?: "cliente"
                )
            }else null


        } catch (e: Exception) {
            null
        }
    }
    private suspend fun loginWithFirestore(correo: String, clave: String): User? {
        return try{
            val query =db.collection("usuario")
                .whereEqualTo("correo", correo)
                .whereEqualTo("clave", clave)
                .get()
                .await()

            if (!query.isEmpty){
                val doc = query.documents[0]
                User(
                    correo = doc.getString("correo") ?: " ",
                    nombre = doc.getString("nombre") ?: "cliente",
                    rol = doc.getString("rol") ?: "cliente"
                )
            }else null
        }catch(e: Exception){
    null
        }
    }

}