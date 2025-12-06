package com.example.ecoviajes.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ecoviajes.ui.screens.login.LoginScreen
import com.example.ecoviajes.ui.screens.registro.RegistroScreen
import com.example.ecoviajes.ui.screens.perfil.PerfilAdminScreen
import com.example.ecoviajes.ui.screens.perfil.PerfilClienteScreen
import com.example.ecoviajes.ui.screens.carrito.CarritoScreen
import com.example.ecoviajes.ui.screens.Pago.PagoConfirmacionScreen
import com.example.ecoviajes.viewmodel.CarritoViewModel
import com.example.ecoviajes.ui.screens.perfil.PerfilEditarScreen
import com.example.ecoviajes.viewmodel.PerfilViewModel


@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val carritoViewModel: CarritoViewModel = viewModel()
    val perfilViewModel: PerfilViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // 🟢 Pantalla de Login

        composable("login") {
            LoginScreen(
                onRegisterClick = {
                    navController.navigate("register")
                },
                onLoginSuccess = { user ->

                    perfilViewModel.inicializar(user.correo)

                    when (user.rol) {
                        "admin" -> navController.navigate("perfil_admin/${user.nombre}")
                        else -> navController.navigate("perfil_cliente/${user.nombre}")
                    }
                }
            )
        }


        // 🟢 Registro
        composable("register") {
            RegistroScreen(
                onBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.popBackStack()
                }
            )
        }

        // 🟢 Perfil del Administrador
        composable(
            "perfil_admin/{nombre}",
            arguments = listOf(navArgument("nombre") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: "Administrador"
            PerfilAdminScreen(
                nombre = nombre,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // 🟢 Perfil del Cliente
        composable("perfil_cliente/{nombre}") { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: "Cliente"

            PerfilClienteScreen(
                nombre = nombre,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onVerCarrito = { navController.navigate("carrito") },
                onVerComentarios = { navController.navigate("comentarios") },
                onEditarPerfil = { navController.navigate("perfil_editar") },
                viewModel = carritoViewModel,
                perfilViewModel = perfilViewModel
            )
        }

        // 🟢 Pantalla para editar/eliminar el perfil
        composable("perfil_editar") {
            PerfilEditarScreen(
                onBack = { navController.popBackStack() },
                onCuentaEliminada = {
                    // Si se elimina la cuenta, mandamos al login y limpiamos el back stack
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                viewModel = perfilViewModel
            )
        }


        // 🟢 Pantalla de Comentarios
        composable("comentarios") {
            com.example.ecoviajes.ui.screens.comentarios.ComentariosScreen(
                onVolver = { navController.popBackStack() }
            )
        }

        // 🟢 Carrito de compras
        composable("carrito") {
            CarritoScreen(
                onVolverAlCatalogo = { navController.popBackStack() },
                onConfirmarPago = {
                    navController.navigate("pago") {
                        popUpTo("perfil_cliente") { inclusive = false }
                    }
                },
                viewModel = carritoViewModel
            )
        }

        // 🟢 Confirmación de pago
        composable("pago") {
            PagoConfirmacionScreen(
                nombreUsuario = "Cliente",
                onVolverAlPerfil = {
                    navController.navigate("perfil_cliente/Cliente") {
                        popUpTo("login") { inclusive = false }
                    }
                }
            )
        }
    }
}
