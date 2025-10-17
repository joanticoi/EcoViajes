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
            //intentar autenticar con auth
            when {
                correo == "admin@ecoviajes.cl" -> {
                    //Autenticacion con Firebase auth
                    val resultado = auth.signInWithEmailAndPassword(correo, clave).await()
                    User(
                        correo = correo,
                        nombre = "Administrador",
                        rol = "admin"
                    )
                }
            else -> {
                //Autenticacion con la coleccion usuario de Firestore
                loginWithFirestore(correo,clave)
            }
            }

        } catch (e: Exception){
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
                    clave = doc.getString("clave") ?: "",
                    rol = doc.getString("rol") ?: "cliente"
                )
            }else null
        }catch(e: Exception){
    null
        }
    }

}