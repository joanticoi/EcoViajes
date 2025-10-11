package com.example.ecoviajes.model

data class User (
    val correo: String = "",
    val clave: String = "",
    val nombre: String = "",
    val rol: String = "" //variable local para controlar los roles de los correos

)