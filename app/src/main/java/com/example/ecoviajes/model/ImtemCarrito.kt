package com.example.ecoviajes.model

data class ItemCarrito(
    val producto: Producto,
    var cantidad: Int = 1
)