package com.example.ecoviajes.api

import com.example.ecoviajes.model.User
import retrofit2.Response
import retrofit2.http.*

// API REST para gestionar los usuarios de EcoViajes
interface UsuarioApiService {

    // Lista todos los usuarios
    @GET("usuarios")
    suspend fun obtenerUsuarios(): Response<List<User>>

    // Obtener un usuario por correo
    @GET("usuarios/{correo}")
    suspend fun obtenerUsuarioPorCorreo(@Path("correo") correo: String): Response<User>

    // Crear un nuevo usuario
    @POST("usuarios")
    suspend fun crearUsuario(@Body usuario: User): Response<User>

    // Actualizar un usuario existente
    @PUT("usuarios/{correo}")
    suspend fun actualizarUsuario(
        @Path("correo") correo: String,
        @Body usuario: User
    ): Response<User>

    // Eliminar un usuario
    @DELETE("usuarios/{correo}")
    suspend fun eliminarUsuario(@Path("correo") correo: String): Response<Void>
}
