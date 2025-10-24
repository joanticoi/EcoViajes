package com.example.ecoviajes.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecoviajes.repository.AuthRepository
import com.example.ecoviajes.viewmodel.LoginViewModel
import com.example.ecoviajes.ui.components.LogoEcoviajes
import com.example.ecoviajes.ui.components.ecoviajesBackground

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit = {},
    onLoginSuccess: (user: com.example.ecoviajes.model.User) -> Unit = {}
) {
    val context = LocalContext.current
    var correo by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var correoError by remember { mutableStateOf("") }
    var passError by remember { mutableStateOf("") }
    var correoFocused by remember { mutableStateOf(false) }
    var passFocused by remember { mutableStateOf(false) }

    val viewModel: LoginViewModel = viewModel()
    val user by viewModel.user.collectAsState()
    val carga by viewModel.carga.collectAsState()
    val repositorio = AuthRepository()

    LaunchedEffect(user) {
        user?.let {
            val mensaje = when (it.rol) {
                "admin" -> "Bienvenido Admin: ${it.nombre}"
                else -> "Bienvenido: ${it.nombre}"
            }
            Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
            onLoginSuccess(it)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .ecoviajesBackground(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoEcoviajes(modifier = Modifier.padding(bottom = 8.dp))

            Text(
                "Iniciar Sesión",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Color(0xFF00796B),
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo Correo
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
                            "Ejemplo: nombre@ecoviajes.cl o usuario@gmail.com",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { correoFocused = it.isFocused },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Contraseña
            OutlinedTextField(
                value = pass,
                onValueChange = {
                    pass = it
                    passError = if (it.length < 6)
                        "La contraseña debe tener al menos 6 caracteres"
                    else ""
                },
                label = { Text("Contraseña *") },
                isError = passError.isNotEmpty(),
                visualTransformation = PasswordVisualTransformation(),
                supportingText = {
                    when {
                        passError.isNotEmpty() -> Text(passError, color = Color.Red, fontSize = 12.sp)
                        passFocused -> Text(
                            "Debe contener mínimo 6 caracteres y puede incluir letras o números.",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { passFocused = it.isFocused },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Botón Entrar
            Button(
                onClick = {
                    if (correo.isEmpty() || pass.isEmpty()) {
                        Toast.makeText(context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                    } else if (correoError.isEmpty() && passError.isEmpty()) {
                        viewModel.login(correo, pass)
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
                enabled = !carga
            ) {
                if (carga) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Text("Entrar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onRegisterClick) {
                Text(
                    "¿No tienes cuenta? Regístrate aquí",
                    color = Color(0xFF00796B),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
