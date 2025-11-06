package com.example.ecoviajes

import androidx.compose.ui.platform.LocalContext
import android.Manifest
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
import androidx.compose.runtime.*
import android.os.Handler
import android.os.Looper
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import com.example.ecoviajes.ui.screens.splash.SplashScreen
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.content.ContextCompat
import com.example.ecoviajes.navigation.AppNavegacion

private const val CHANNEL_ID = "mi_canal_id"

class MainActivity : ComponentActivity() {

    // Lanzador para solicitar permiso de notificaciones
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            // Puedes manejar la respuesta aquí si lo deseas
        }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askNotificationPermission()
        createNotificationChannel(this)

        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}

// 🔔 Canal de notificaciones
fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Notificaciones Generales"
        val descriptionText = "Canal para notificaciones de EcoViajes"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

// 📣 Función para mostrar una notificación simple
fun showBasicNotification(context: Context, title: String, content: String) {
    val notificationId = 1

    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent =
        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.logo2)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    val notificationManager = NotificationManagerCompat.from(context)

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }

    notificationManager.notify(notificationId, builder.build())
}

// 🌍 Punto de entrada Compose
@Composable
fun MyApp() {
    var showLogin by rememberSaveable { mutableStateOf(false) }

    val handler = remember { Handler(Looper.getMainLooper()) }
    val context = LocalContext.current

    // Mostrar login después del splash
    LaunchedEffect(Unit) {
        handler.postDelayed({ showLogin = true }, 2000L)
    }

    MaterialTheme {
        Surface {
            if (!showLogin) {
                SplashScreen()

                // Mostrar notificación luego del splash
                LaunchedEffect(Unit) {
                    handler.postDelayed({
                        showBasicNotification(
                            context,
                            "¡Encuentra tu nuevo destino!",
                            "Visita nuestra app para conocer tu destino soñado 🌎"
                        )
                    }, 2100L)
                }
            } else {
                // ✅ Aquí cargamos la navegación completa de la app
                AppNavegacion()
            }
        }
    }
}
