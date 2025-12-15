package com.example.ecoviajes.ui.screens.perfil

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ecoviajes.viewmodel.PerfilViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


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
    val scrollState = rememberScrollState()


    LaunchedEffect(Unit) { viewModel.cargarDatosUsuario() }

    var fotoNuevaUri by remember { mutableStateOf<Uri?>(null) }
    val pickerFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> fotoNuevaUri = uri }

    var nombreEditado by remember(uiState.nombre) { mutableStateOf(uiState.nombre) }
    var telefonoEditado by remember(uiState.telefono) { mutableStateOf(uiState.telefono) }
    var nuevaClave by remember { mutableStateOf("") }
    var claveActual by remember { mutableStateOf("") }


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
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
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
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (uiState.cargando) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            // 👤 Foto de perfil (tipo Facebook)
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val modelFoto: Any? = fotoNuevaUri ?: uiState.foto.takeIf { it.isNotBlank() }

                Surface(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .clickable(enabled = !uiState.cargando) {
                            pickerFoto.launch("image/*")
                        },
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    if (modelFoto != null) {
                        AsyncImage(
                            model = modelFoto,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "👤",
                                style = MaterialTheme.typography.headlineLarge
                            )
                        }
                    }
                }

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-8).dp, y = (-8).dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        "✎",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }



            // Correo
            OutlinedTextField(
                value = uiState.correo,
                onValueChange = {},
                enabled = false,
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth()
            )

            // Nombre
            OutlinedTextField(
                value = nombreEditado,
                onValueChange = { nombreEditado = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            // Teléfono
            OutlinedTextField(
                value = telefonoEditado,
                onValueChange = { telefonoEditado = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )


            // Guardar cambios (nombre + teléfono + fotoUri)
            Button(
                onClick = {
                    viewModel.guardarCambios(
                        nuevoNombre = nombreEditado,
                        nuevoTelefono = telefonoEditado,
                        nuevaFotoUri = fotoNuevaUri
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.cargando
            ) {
                Text("Guardar cambios")
            }

            Divider(Modifier.padding(vertical = 8.dp))

            // Cambiar contraseña
            // Cambiar contraseña
            Text("Cambiar contraseña", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = claveActual,
                onValueChange = { claveActual = it },
                label = { Text("Contraseña actual") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )


            OutlinedTextField(
                value = nuevaClave,
                onValueChange = { nuevaClave = it },
                label = { Text("Nueva contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.cambiarContrasenaFirestore(claveActual, nuevaClave)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.cargando
            ) {
                Text("Actualizar contraseña")
            }

            LaunchedEffect(uiState.mensaje) {
                if (uiState.mensaje == "Contraseña actualizada correctamente") {
                    claveActual = ""
                    nuevaClave = ""
                }
            }



            Spacer(Modifier.height(12.dp))

            // Zona peligrosa
            Text(
                "Zona peligrosa",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Red
            )

            OutlinedButton(
                onClick = { mostrarDialogoEliminar = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
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
