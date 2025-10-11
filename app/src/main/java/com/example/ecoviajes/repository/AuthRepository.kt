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
            val resultado = auth.signInWithEmailAndPassword(p0 = correo, p1= clave)
            val user = resultado.user
            if (user != null){
                getUserFromFirestore(user.uid, correo) ?: User (
                    correo = correo
                    nombre = if(correo == "admin@ecoviaje.cl") "administrador" else "usuario"
                )
            }else null
        }catch(e: Exeption){
            loginWithFirestore(correo, clave)
        }
    }
    private suspend fun getUserFromFirestore(uid: String, correo: String): User? {
        return try {
            val doc = db.collection("usuario").document()
            val .
        } catch () {

        }
    }
    private suspend fun loginWithFirestore(correo: String, user: String){
        return try{
            val query =db.collection("usuario")
                .whereEqualTo("correo", value = correo)
                .whereEqualTo("clave", clave)
                .get()
                .await()

            if (!query.isEmpty){
                val doc = query.documents[0]
                user(
                    correo = doc.getString("correo") ?: " ",
                    nombre = doc.getString("nombre") ?: " "
                )
            }
        }catch(){

        }
    }

}