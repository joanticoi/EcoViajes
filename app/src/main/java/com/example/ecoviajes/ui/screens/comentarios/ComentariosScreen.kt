package com.example.ecoviajes.ui.screens.comentarios

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComentariosScreen(onVolver: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance()
    var comentario by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Deja tu comentario 🌱") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Queremos conocer tu opinión sobre EcoViajes.",
                style = MaterialTheme.typography.titleMedium
            )

            // 🧩 Campo de texto para el comentario
            OutlinedTextField(
                value = comentario,
                onValueChange = { comentario = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                placeholder = { Text("Escribe tu comentario aquí...") },
                label = { Text("Comentario") },
                maxLines = 5
            )

            // 🧭 Botón para enviar comentario
            Button(
                onClick = {
                    if (comentario.isNotBlank()) {
                        db.collection("comentarios").add(
                            mapOf(
                                "texto" to comentario,
                                "fecha" to System.currentTimeMillis()
                            )
                        ).addOnSuccessListener {
                            comentario = ""
                            scope.launch {
                                snackbarHostState.showSnackbar("¡Comentario enviado con éxito! ✅")
                            }
                        }.addOnFailureListener {
                            scope.launch {
                                snackbarHostState.showSnackbar("Error al enviar el comentario ❌")
                            }
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Por favor, escribe algo antes de enviar 📝")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Enviar comentario")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Tus comentarios nos ayudan a mejorar nuestras experiencias eco 🌍",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
