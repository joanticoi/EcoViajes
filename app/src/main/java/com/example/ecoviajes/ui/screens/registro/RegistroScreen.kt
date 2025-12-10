package com.example.ecoviajes.ui.screens.registro

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecoviajes.ui.components.LogoEcoviajes
import com.example.ecoviajes.ui.components.ecoviajesBackground
import com.example.ecoviajes.viewmodel.RegistroViewModel

@Composable
fun RegistroScreen(
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: RegistroViewModel = viewModel()

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var confirmarClave by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }     // 👈 NUEVO
    var foto by remember { mutableStateOf("") }         // 👈 NUEVO (URL opcional)

    var nombreError by remember { mutableStateOf("") }
    var correoError by remember { mutableStateOf("") }
    var claveError by remember { mutableStateOf("") }
    var confirmarClaveError by remember { mutableStateOf("") }

    var nombreFocused by remember { mutableStateOf(false) }
    var correoFocused by remember { mutableStateOf(false) }
    var claveFocused by remember { mutableStateOf(false) }
    var confirmarFocused by remember { mutableStateOf(false) }

    val cargando by viewModel.cargando.collectAsState()
    val registroExitoso by viewModel.registroExitoso.collectAsState()
    val errorMensaje by viewModel.errorMensaje.collectAsState()

    LaunchedEffect(registroExitoso) {
        if (registroExitoso) {
            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
            onRegisterSuccess()
            viewModel.limpiarRegistro()
        }
    }

    LaunchedEffect(errorMensaje) {
        if (errorMensaje.isNotEmpty()) {
            Toast.makeText(context, errorMensaje, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .ecoviajesBackground()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Botón volver en la esquina superior izquierda
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color(0xFF00796B)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Registro Ecoviajes",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = Color(0xFF00796B),
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            LogoEcoviajes(modifier = Modifier.padding(vertical = 8.dp))

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre completo
            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    nombreError = if (it.isBlank()) "Ingrese su nombre completo" else ""
                },
                label = { Text("Nombre completo *") },
                isError = nombreError.isNotEmpty(),
                supportingText = {
                    when {
                        nombreError.isNotEmpty() -> Text(nombreError, color = Color.Red, fontSize = 12.sp)
                        nombreFocused -> Text(
                            "Ingrese su nombre y apellido tal como figuran en su documento.",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { nombreFocused = it.isFocused },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Correo electrónico
            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    correoError = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches())
                        "Formato de correo no válido"
                    else ""
                },
                label = { Text("Correo electrónico *") },
                isError = correoError.isNotEmpty(),
                supportingText = {
                    when {
                        correoError.isNotEmpty() -> Text(correoError, color = Color.Red, fontSize = 12.sp)
                        correoFocused -> Text(
                            "Ejemplo: nombre@ecoviajes.cl o contacto@empresa.com",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { correoFocused = it.isFocused },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Teléfono (opcional)
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono (opcional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Foto (URL opcional)
            OutlinedTextField(
                value = foto,
                onValueChange = { foto = it },
                label = { Text("Foto (URL opcional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Contraseña
            OutlinedTextField(
                value = clave,
                onValueChange = {
                    clave = it
                    claveError = if (it.length < 6)
                        "Debe tener al menos 6 caracteres"
                    else ""
                },
                label = { Text("Contraseña *") },
                visualTransformation = PasswordVisualTransformation(),
                isError = claveError.isNotEmpty(),
                supportingText = {
                    when {
                        claveError.isNotEmpty() -> Text(claveError, color = Color.Red, fontSize = 12.sp)
                        claveFocused -> Text(
                            "Debe contener mínimo 6 caracteres y combinar letras o números.",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { claveFocused = it.isFocused },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Confirmar contraseña
            OutlinedTextField(
                value = confirmarClave,
                onValueChange = {
                    confirmarClave = it
                    confirmarClaveError = if (it != clave)
                        "Las contraseñas no coinciden"
                    else ""
                },
                label = { Text("Confirmar contraseña *") },
                visualTransformation = PasswordVisualTransformation(),
                isError = confirmarClaveError.isNotEmpty(),
                supportingText = {
                    when {
                        confirmarClaveError.isNotEmpty() -> Text(confirmarClaveError, color = Color.Red, fontSize = 12.sp)
                        confirmarFocused -> Text(
                            "Repita la misma contraseña para confirmar su registro.",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { confirmarFocused = it.isFocused },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { })
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    if (nombreError.isEmpty() && correoError.isEmpty() &&
                        claveError.isEmpty() && confirmarClaveError.isEmpty()
                    ) {
                        viewModel.registroUsuario(
                            correo = correo,
                            clave = clave,
                            confirmarClave = confirmarClave,
                            nombre = nombre,
                            telefono = telefono,
                            foto = foto
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Corrija los errores antes de continuar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0288D1),
                    contentColor = Color.White
                ),
                enabled = !cargando
            ) {
                if (cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Text("Registrarse", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                "* Campos obligatorios",
                color = Color.Gray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
