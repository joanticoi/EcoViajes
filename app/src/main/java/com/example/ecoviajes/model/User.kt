package com.example.ecoviajes.model



data class User(
    val correo: String = "",
    val clave: String = "",
    val nombre: String = "",
    val rol: String = "cliente",
    val telefono: String = "",
    val foto: String = ""
)

