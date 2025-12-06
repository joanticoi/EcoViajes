package com.example.ecoviajes.api

import com.example.ecoviajes.model.Producto
import retrofit2.Response
import retrofit2.http.*

// API REST para gestionar los paquetes de viaje ecológicos (EcoViajes)
interface ProductoApiService {

    // Obtener todos los viajes ecológicos
    @GET("productos")
    suspend fun obtenerProductos(): Response<List<Producto>>

    // Obtener un viaje ecológico por ID
    @GET("productos/{id}")
    suspend fun obtenerProductoPorId(@Path("id") id: String): Response<Producto>

    // Crear un nuevo viaje ecológico
    @POST("productos")
    suspend fun crearProducto(@Body producto: Producto): Response<Producto>

    // Actualizar un viaje ecológico existente
    @PUT("productos/{id}")
    suspend fun actualizarProducto(
        @Path("id") id: String,
        @Body producto: Producto
    ): Response<Producto>

    // Eliminar un viaje ecológico
    @DELETE("productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: String): Response<Void>

    // Actualizar solo el stock (cupos disponibles)
    @PATCH("productos/{id}/stock")
    suspend fun actualizarStock(
        @Path("id") id: String,
        @Body stockUpdate: StockUpdate
    ): Response<Producto>
}

// Objeto para actualizar solo el stock
data class StockUpdate(val stock: Int)
