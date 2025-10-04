package com.example.ecoviajes.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.ecoviajes.R
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.math.tan

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier
) {
    // Duración total en milisegundos
    val duration = 800

    // Definimos keyframes como Triple<Int, Float, Float> (tiempo, skewX, skewY)
    val keyframes = listOf(
        Triple(0, 0f, 0f),
        Triple(240, -25f, -25f),  // 30%
        Triple(320, 15f, 15f),    // 40%
        Triple(400, -15f, -15f),  // 50%
        Triple(520, 5f, 5f),      // 65%
        Triple(600, -5f, -5f),    // 75%
        Triple(800, 0f, 0f)       // 100%
    )

    // Animatable para skewX y skewY
    val skewX = remember { Animatable(0f) }
    val skewY = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            for (i in 0 until keyframes.size - 1) {
                val (timeStart, startX, startY) = keyframes[i]
                val (timeEnd, endX, endY) = keyframes[i + 1]
                val segmentDuration = timeEnd - timeStart

                // Animamos ambos valores simultáneamente con coroutineScope
                coroutineScope {
                    val animX = async {
                        skewX.animateTo(
                            targetValue = endX,
                            animationSpec = tween(
                                durationMillis = segmentDuration,
                                easing = CubicBezierEasing(0.55f, 0.085f, 0.68f, 0.53f)
                            )
                        )
                    }
                    val animY = async {
                        skewY.animateTo(
                            targetValue = endY,
                            animationSpec = tween(
                                durationMillis = segmentDuration,
                                easing = CubicBezierEasing(0.55f, 0.085f, 0.68f, 0.53f)
                            )
                        )
                    }
                    // Esperamos a que terminen ambas animaciones
                    animX.await()
                    animY.await()
                }
            }
        }
    }

    // Función para convertir grados a radianes (Double)
    fun degToRad(deg: Float) = deg * (Math.PI / 180.0)

    // Construir las transformaciones usando rotación y escalado en X y Y
    val skewXRad = tan(degToRad(skewX.value))
    val skewYRad = tan(degToRad(skewY.value))

    // Se aplican los valores de transformación usando gráficos y no la matriz
    Box(
        modifier = modifier.graphicsLayer(
            transformOrigin = TransformOrigin(0.5f, 0.5f),
            rotationZ = 0f,  // Para asegurar que no haya rotación inesperada
            scaleX = (1 + skewXRad).toFloat(), // Aplicamos el efecto de skew en X
            scaleY = (1 + skewYRad).toFloat()  // Aplicamos el efecto de skew en Y
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // Asegúrate de tener el logo en tu drawable
            contentDescription = "Logo anima",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}
