package com.example.ecoviajes.ui.screens.Catalogo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(navController: NavHostController) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Catálogo EcoViajes") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Contenido del catálogo (puedes reemplazar este texto por tus tarjetas, etc.)
            Text(
                text = "Explora nuestros destinos sostenibles 🌿",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Aquí puedes conocer todos los viajes eco disponibles.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ✅ Botón para ir a la pantalla de comentarios
            Button(
                onClick = { navController.navigate("comentarios") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Dejar un comentario ✍️")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mensaje de ayuda o información
            Text(
                text = "Tu opinión nos ayuda a mejorar nuestros destinos 🌎",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}



