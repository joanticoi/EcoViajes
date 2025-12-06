package com.example.ecoviajes.ui.carrito

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.ecoviajes.model.ItemCarrito
import com.example.ecoviajes.model.Producto
import com.example.ecoviajes.ui.screens.carrito.CarritoScreen
import com.example.ecoviajes.viewmodel.CarritoViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class CarritoScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun cuando_el_carrito_esta_vacio_debe_mostrar_mensaje_vacio() {
        // 1. Crear un Mock del ViewModel (relaxed = true evita errores en métodos void)
        val mockViewModel = mockk<CarritoViewModel>(relaxed = true)

        // 2. Configurar el comportamiento: Carrito vacío
        every { mockViewModel.carrito } returns MutableStateFlow(emptyList())
        every { mockViewModel.obtenerTotal() } returns 0.0

        // 3. Renderizar la pantalla inyectando el Mock
        composeTestRule.setContent {
            CarritoScreen(
                onVolverAlCatalogo = {},
                onConfirmarPago = {},
                viewModel = mockViewModel // 👈 Falta esto en tu código original
            )
        }

        // 4. Verificar
        composeTestRule.onNodeWithText("El carrito está vacío").assertExists()
    }

    @Test
    fun cuando_hay_productos_debe_mostrar_lista_y_total() {
        val mockViewModel = mockk<CarritoViewModel>(relaxed = true)

        // Crear datos de prueba
        val producto = Producto("1", "Bicicleta", 500.0, "", 5)
        val items = listOf(ItemCarrito(producto, 1))

        // Configurar comportamiento: Carrito con 1 producto
        every { mockViewModel.carrito } returns MutableStateFlow(items)
        every { mockViewModel.obtenerTotal() } returns 500.0

        composeTestRule.setContent {
            CarritoScreen(
                onVolverAlCatalogo = {},
                onConfirmarPago = {},
                viewModel = mockViewModel
            )
        }

        // Verificar que aparecen los textos clave
        composeTestRule.onNodeWithText("Mi Carrito").assertExists()
        composeTestRule.onNodeWithText("Bicicleta").assertExists() // Nombre del producto
        composeTestRule.onNodeWithText("Total:").assertExists()
    }

    @Test
    fun al_hacer_clic_en_confirmar_compra_debe_llamar_al_viewmodel() {
        val mockViewModel = mockk<CarritoViewModel>(relaxed = true)

        // Necesitamos productos para que aparezca el botón de compra
        val producto = Producto("1", "Bicicleta", 500.0, "", 5)
        val items = listOf(ItemCarrito(producto, 1))

        every { mockViewModel.carrito } returns MutableStateFlow(items)
        every { mockViewModel.obtenerTotal() } returns 500.0

        composeTestRule.setContent {
            CarritoScreen(
                onVolverAlCatalogo = {},
                onConfirmarPago = {}, // La navegación real no ocurre en test unitario
                viewModel = mockViewModel
            )
        }

        // Verificar que el botón existe y hacer clic
        val botonCompra = composeTestRule.onNodeWithText("Confirmar Compra")
        botonCompra.assertExists()
        botonCompra.assertIsEnabled()
        botonCompra.performClick()

        // Verificar que se llamó a la función confirmarCompra() del ViewModel
        verify { mockViewModel.confirmarCompra() }
    }
}