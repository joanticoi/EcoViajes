package com.example.ecoviajes.ui.screens.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.ecoviajes.model.Producto
import com.example.ecoviajes.ui.components.PhotoActions
import com.example.ecoviajes.viewmodel.CarritoViewModel
import com.example.ecoviajes.showBasicNotification

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilClienteScreen(
    nombre: String = "Cliente",
    onLogout: () -> Unit = {},
    onVerCarrito: () -> Unit = {},
    onVerComentarios: () -> Unit = {}, // 👈 NUEVO parámetro
    viewModel: CarritoViewModel
) {
    val productos by viewModel.productos.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val carrito by viewModel.carrito.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hola, $nombre") },
                actions = {
                    BadgedBox(badge = {
                        val total = carrito.sumOf { it.cantidad }
                        if (total > 0) Badge { Text(total.toString()) }
                    }) {
                        IconButton(onClick = onVerCarrito) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Itinerario")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {

            // 🟩 Tarjeta superior con acciones del usuario
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Explora experiencias eco",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF00796B)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text("Comparte una foto o elige una de tu galería.")
                    Spacer(Modifier.height(12.dp))
                    PhotoActions { /* Puedes dejarlo vacío o hacer algo con la URI si quieres */ }

                    Spacer(Modifier.height(12.dp))


                    // 🟩 Botón: Deja tu comentario ✍️
                    Button(
                        onClick = onVerComentarios,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0288D1)
                        )
                    ) {
                        Text("Deja tu comentario ✍️")
                    }

                    Spacer(Modifier.height(8.dp))

                    // 🟥 Botón: Cerrar sesión
                    OutlinedButton(
                        onClick = onLogout,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFD32F2F)
                        )
                    ) {
                        Text("Cerrar Sesión")
                    }
                }
            }

            // 🟢 Lista de experiencias
            Text(
                "Experiencias disponibles",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(8.dp))

            when {
                cargando && productos.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                productos.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay experiencias disponibles")
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(productos, key = { it.id }) { producto ->
                            ItemExperiencia(
                                producto = producto,
                                cantidadEnCarrito = carrito.find { it.producto.id == producto.id }?.cantidad ?: 0,
                                onAgregar = { if (producto.stock > 0) viewModel.agregarAlCarrito(producto) },
                                onRemover = { viewModel.removerDelCarrito(producto) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemExperiencia(
    producto: Producto,
    cantidadEnCarrito: Int,
    onAgregar: () -> Unit,
    onRemover: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            if (producto.imagen.isNotBlank()) {
                AsyncImage(
                    model = producto.imagen,
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                )
                Spacer(Modifier.height(8.dp))
            }

            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "$${"%,.0f".format(producto.precio)}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF0288D1),
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            when {
                producto.stock <= 0 ->
                    Text("Sin cupos", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                producto.stock < 10 ->
                    Text("Últimos ${producto.stock} cupos", color = Color(0xFFFF9800), fontWeight = FontWeight.Bold)
                else ->
                    Text("Cupos disponibles: ${producto.stock}", color = Color.Gray)
            }

            Spacer(Modifier.height(10.dp))

            if (cantidadEnCarrito > 0) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("En itinerario: $cantidadEnCarrito", fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onRemover, enabled = cantidadEnCarrito > 0) {
                            Icon(Icons.Outlined.Remove, contentDescription = "Quitar uno")
                        }
                        IconButton(
                            onClick = {
                                onAgregar()
                                showBasicNotification(
                                    context,
                                    "¡Experiencia agregada!",
                                    "Paga tu compra para poder viajar."
                                )
                            },
                            enabled = producto.stock > cantidadEnCarrito
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Agregar uno")
                        }
                    }
                }
            } else {
                Button(
                    onClick = {
                        onAgregar()
                        showBasicNotification(
                            context,
                            "¡Experiencia agregada!",
                            "Paga tu compra para poder viajar."
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = producto.stock > 0,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0288D1)
                    )
                ) {
                    Text(if (producto.stock > 0) "Reservar" else "Sin cupos")
                }
            }
        }
    }
}
