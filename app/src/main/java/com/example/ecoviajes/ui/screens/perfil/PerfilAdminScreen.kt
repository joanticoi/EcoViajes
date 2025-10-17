package com.example.ecoviajes.ui.screens.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable
fun PerfilAdminScreen(
    nombre: String = "Administrador",
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Panel de Administrador",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFFD32F2F)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Bienvenido $nombre",  // Muestra el nombre del admin
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Rol: Administrador",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {

                onLogout()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD32F2F)
            )
        ) {
            Text("Cerrar Sesión")
        }
    }
}