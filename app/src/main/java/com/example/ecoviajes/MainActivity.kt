package com.example.ecoviajes

import androidx.compose.ui.platform.LocalContext
import android.Manifest // <-- Asegúrate de tener este import
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ecoviajes.ui.theme.EcoViajesTheme

import android.os.Handler
import android.os.Looper
import android.window.SplashScreen
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import com.example.ecoviajes.ui.screens.login.LoginScreen
import com.example.ecoviajes.ui.screens.splash.SplashScreen
import com.example.ecoviajes.navigation.AppNavegacion
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
// 1. AÑADIR ESTOS IMPORTS
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

private const val CHANNEL_ID= "mi_canal_id"

class MainActivity : ComponentActivity() {

    // 2. DEFINIR EL LANZADOR PARA LA SOLICITUD DE PERMISO
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permiso concedido
            } else {
                // Permiso denegado
            }
        }

    // 3. CREAR FUNCIÓN PARA PEDIR PERMISO
    private fun askNotificationPermission() {
        // Solo aplica para Android 13 (TIRAMISU / API 33) y superiores
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Pedir el permiso
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 4. LLAMAR LAS FUNCIONES AL INICIAR
        askNotificationPermission()
        createNotificationChannel(this)

        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}



fun createNotificationChannel(context: Context) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Notificaciones Generales"
        val descriptionText = "Canal para notificaciones"
        val importance = NotificationManager.IMPORTANCE_DEFAULT


        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }


        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }
}

// (Tu función 'showBasicNotification' que acepta título y contenido va aquí)
fun showBasicNotification(context: Context, title: String, content: String) {

    val notificationId = 1

    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.logo2)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    val notificationManager = NotificationManagerCompat.from(context)

    // ESTA ES LA LÍNEA IMPORTANTE
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Si el permiso no está concedido (en API 33+), no hará nada.
        // Por eso es vital pedirlo en el 'onCreate' como hicimos arriba.
        return
    }

    notificationManager.notify(notificationId, builder.build())

}
@Composable
fun MyApp() {
    var showLogin by rememberSaveable{ mutableStateOf(false) }

    val handler = remember { Handler(Looper.getMainLooper()) }
    val context = LocalContext.current // 5. Obtener contexto aquí

    LaunchedEffect(Unit) {
        handler.postDelayed({showLogin = true}, 2000L)
    }

    MaterialTheme{
        Surface {
            if (!showLogin) {
                SplashScreen()

                // 6. MOVER LA NOTIFICACIÓN INICIAL AQUÍ
                // Se ejecutará después de que la app haya pedido permiso
                // y mostrará la notificación de bienvenida.
                LaunchedEffect(Unit) {
                    // Esperamos un poco más para que no salga sobre el splash
                    handler.postDelayed({
                        showBasicNotification(
                            context,
                            "Encuentra tu nuevo destino!",
                            "Visita nuestra app para conocer tu destino soñado"
                        )
                    }, 2100L) // 2.1 segundos
                }

            } else {
                AppNavegacion()

            }
        }
    }

}