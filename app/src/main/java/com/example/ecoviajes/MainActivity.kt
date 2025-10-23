package com.example.ecoviajes

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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
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
            } else {
                AppNavegacion()

        }
        }
    }

}