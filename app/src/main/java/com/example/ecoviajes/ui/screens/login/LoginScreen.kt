package com.example.ecoviajes.ui.screens.login

import androidx.compose.runtime.Composable

import android.widget.Toast //Mensaje emergentes
import androidx.compose.foundation.layout.* //Organizar los elementos en una vista
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions //Mostrar la entre de datos al usuario
import androidx.compose.material3.* //Elementos para diseñar UI
import androidx.compose.runtime.* // Manejar los estados de la app
import androidx.compose.ui.Alignment // Alinear los elementos
import androidx.compose.ui.Modifier //Modificar el diseño visual de los elemento
import androidx.compose.ui.platform.LocalContext //obtener el contexto o estado en ejecución del ciclo de vida de la app y poder mostrar mensaje
import androidx.compose.ui.text.input.KeyboardType //Controlar el tipo de entrada para el usuario
import androidx.compose.ui.text.input.PasswordVisualTransformation //Ocultar la contraseña al escribirla
import androidx.compose.ui.unit.dp //Controlar el tamaño de los elementos
import androidx.compose.ui.graphics.Color //Controlar el color de los elementos
import androidx.lifecycle.viewmodel
import com.example.ecoviajes.viewmodel.LoginViewModel
@Composable
fun LoginScreen() {
    //Variable para obtener en tiempo de ejecución el estado del ciclo de vida de app
    val context = LocalContext.current

    //Variable para almacenar en nombre del usuario
    var correo by remember { mutableStateOf("") }

    val viewModel:
    //Variable para almacenar la clave del usuario
    var pass by remember { mutableStateOf("") }

    //Configuración para organizar los elementos de la pantalla usando el componente Column()
    Column (
        modifier = Modifier
            .fillMaxSize() //Rellenar todo el espacio diponible de la pantalla
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        //Componente tipo Text() para agregar un título
        Text("Inciar Sesión",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFF4CAF50))

        //Componente Spacer() para agregar un separador entre los elementos
        Spacer(Modifier.height(46.dp))

        //Componente tipo OutlinedTextField() para ingresar datos por usuario
        OutlinedTextField(
            //Variable para el nombre del usuario
            value = user,
            onValueChange = { user = it },
            label = { Text("Usuario", color = Color(0xFFFF5722))},
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        //Componente Spacer() para agregar un separador entre los elementos
        Spacer(Modifier.height(10.dp))

        //Componente tipo OutlinedTextField() para ingresar datos por usuario
        OutlinedTextField(
            //Variable para la clave del usuario
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Clave", color = Color(0xFFFF5722))},
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        //Componente Spacer() para agregar un separador entre los elementos
        Spacer(Modifier.height(30.dp))

        //Componente Button() para agrega un boton
        Button(
            onClick = {
                Toast.makeText(context, "Bienvenido $user", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81154C), contentColor = Color(0xFFC7F9CC))
        ) {
            Text("Entrar")
        }
    }
}