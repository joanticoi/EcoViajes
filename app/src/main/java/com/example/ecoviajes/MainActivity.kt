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

private const val CHANNEL_ID= "mi_canal_id"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

// 1. MODIFICAR LA FIRMA DE LA FUNCIÓN
fun showBasicNotification(context: Context, title: String, content: String) {

    val notificationId = 1

    // Intención para abrir tu MainActivity (o la pantalla que desees)
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    // construyendo notificacion

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.logo2)
        // 2. USAR LOS PARÁMETROS
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)


    // -mostrar

    val notificationManager = NotificationManagerCompat.from(context)

    // permisos
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {

        return
    }

    // mostrar notificacion

    notificationManager.notify(notificationId, builder.build())

}
@Composable
fun MyApp() {
    var showLogin by rememberSaveable{ mutableStateOf(false) }

    val handler = remember { Handler(Looper.getMainLooper()) }
    LaunchedEffect(Unit) {
        handler.postDelayed({showLogin = true}, 2000L)
    }

    MaterialTheme{
        Surface {
            if (!showLogin) {
                SplashScreen()
                createNotificationChannel(LocalContext.current)
                // 3. ACTUALIZAR LA LLAMADA CON EL TEXTO ORIGINAL
                showBasicNotification(
                    LocalContext.current,
                    "Encuentra tu nuevo destino!",
                    "Visita nuestra app para conocer tu destino soñado"
                )

            } else {
                AppNavegacion()

            }
        }
    }

}