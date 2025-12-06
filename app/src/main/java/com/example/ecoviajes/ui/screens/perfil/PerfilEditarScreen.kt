package com.example.ecoviajes.ui.screens.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ecoviajes.viewmodel.PerfilViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilEditarScreen(
    onBack: () -> Unit = {},
    onCuentaEliminada: () -> Unit = {},
    viewModel: PerfilViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Carga datos del usuario al entrar
    LaunchedEffect(Unit) {
        viewModel.cargarDatosUsuario()
    }

    var nombreEditado by remember(uiState.nombre) { mutableStateOf(uiState.nombre) }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.mensaje, uiState.error) {
        uiState.mensaje?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            viewModel.limpiarMensajes()
        }
        uiState.error?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            viewModel.limpiarMensajes()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
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

            if (uiState.cargando) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            OutlinedTextField(
                value = uiState.correo,
                onValueChange = {},
                enabled = false,
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nombreEditado,
                onValueChange = { nombreEditado = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.guardarCambios(nombreEditado) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.cargando
            ) {
                Text("Guardar cambios")
            }

            Spacer(Modifier.height(32.dp))

            Text(
                "Zona peligrosa",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Red
            )

            OutlinedButton(
                onClick = { mostrarDialogoEliminar = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Red
                )
            ) {
                Text("Eliminar cuenta")
            }
        }
    }

    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("Eliminar cuenta") },
            text = { Text("¿Seguro que quieres eliminar tu cuenta? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoEliminar = false
                        viewModel.eliminarCuenta { onCuentaEliminada() }
                    }
                ) {
                    Text("Sí, eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
