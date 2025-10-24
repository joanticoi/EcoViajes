package com.example.ecoviajes.ui.components


import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.ecoviajesBackground(): Modifier = this.then(
    Modifier.background(
        Brush.verticalGradient(
            listOf(
                Color(0xFFE8F5E9), // Verde suave
                Color(0xFFB3E5FC)  // Azul suave
            )
        )
    )
)