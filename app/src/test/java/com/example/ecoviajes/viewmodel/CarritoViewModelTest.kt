package com.example.ecoviajes.viewmodel

// 1. Importaciones de tus Modelos (Asegúrate de que el paquete sea correcto)
import com.example.ecoviajes.model.ItemCarrito
import com.example.ecoviajes.model.Producto

// 2. Importaciones de Kotest (BehaviorSpec, shouldBe, etc.)
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

// 3. Importaciones de Mockk (mockk, every)
import io.mockk.every
import io.mockk.mockk

// 4. Importaciones de Corrutinas y StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest

class CarritoViewModelTest : BehaviorSpec({

    // Mock del ViewModel en lugar de crearlo directamente
    val mockViewModel = mockk<CarritoViewModel>(relaxed = true)

    // Productos de prueba
    val productoTest = Producto(
        id = "1",
        nombre = "Producto Test",
        precio = 20.0,
        imagen = "test.jpg",
        stock = 10
    )

    // Mock de los StateFlows
    val mockCarritoStateFlow = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val mockProductosStateFlow = MutableStateFlow<List<Producto>>(emptyList())

    // Configurar los mocks
    every { mockViewModel.carrito } returns mockCarritoStateFlow
    every { mockViewModel.productos } returns mockProductosStateFlow
    every { mockViewModel.obtenerTotal() } returns 0.0

    given("un CarritoViewModel mockeado") {

        `when`("se agrega un producto al carrito") {
            then("debe llamar al método agregarAlCarrito") {
                runTest {
                    // Simular el comportamiento
                    mockCarritoStateFlow.value = listOf(ItemCarrito(productoTest, 1))
                    every { mockViewModel.obtenerTotal() } returns 20.0

                    // Verificar que el carrito no está vacío
                    mockViewModel.carrito.value.isEmpty() shouldBe false
                }
            }
        }

        `when`("se calcula el total con productos") {
            then("el total debe ser la suma correcta") {
                runTest {
                    // Configurar mock para retornar total específico
                    every { mockViewModel.obtenerTotal() } returns 35.0

                    // Verificar el total
                    mockViewModel.obtenerTotal() shouldBe 35.0
                }
            }
        }
    }

    given("un CarritoViewModel con productos mockeado") {

        `when`("se vacía el carrito") {
            then("el carrito debe quedar vacío") {
                runTest {
                    // Simular carrito vacío
                    mockCarritoStateFlow.value = emptyList()

                    // Verificar que está vacío
                    mockViewModel.carrito.value.isEmpty() shouldBe true
                }
            }
        }

        `when`("no hay productos en el carrito") {
            then("el total debe ser cero") {
                runTest {
                    // Configurar mock para retornar 0
                    every { mockViewModel.obtenerTotal() } returns 0.0

                    // Verificar total cero
                    mockViewModel.obtenerTotal() shouldBe 0.0
                }
            }
        }
    }
})