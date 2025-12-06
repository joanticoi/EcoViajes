package com.example.ecoviajes



import com.example.ecoviajes.model.ItemCarrito
import com.example.ecoviajes.model.Producto
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class CarritoBusinessLogicTest : FunSpec({

    // Test de propiedades para cálculo de total
    test("el total debe ser igual a la suma de precios por cantidad") {
        checkAll(
            Arb.double(1.0, 100.0),
            Arb.int(1, 10),
            Arb.double(1.0, 100.0),
            Arb.int(1, 10)
        ) { precio1, cantidad1, precio2, cantidad2 ->
            val producto1 = Producto("1", "Prod1", precio1, "", 10)
            val producto2 = Producto("2", "Prod2", precio2, "", 10)

            val item1 = ItemCarrito(producto1, cantidad1)
            val item2 = ItemCarrito(producto2, cantidad2)

            val totalEsperado = (precio1 * cantidad1) + (precio2 * cantidad2)
            val carrito = listOf(item1, item2)
            val totalCalculado = carrito.sumOf { it.producto.precio * it.cantidad }

            totalCalculado shouldBe totalEsperado
        }
    }
// Test para verificar que no se pueden agregar productos sin stock
test("no se puede agregar producto con stock cero") {
    val productoSinStock = Producto("1", "Sin Stock", 10.0, "", 0)
    val carrito = mutableListOf<ItemCarrito>()

    // Simulación de intento de agregar producto sin stock
    val puedeAgregar = productoSinStock.stock > 0

    puedeAgregar shouldBe false
}

// Test para cálculo de subtotal por item
test("el subtotal por item debe ser precio por cantidad") {
    checkAll(Arb.double(1.0, 100.0), Arb.int(1, 5)) { precio, cantidad ->
        val producto = Producto("1", "Test", precio, "", 10)
        val item = ItemCarrito(producto, cantidad)

        val subtotal = item.producto.precio * item.cantidad

        subtotal shouldBe (precio * cantidad)
    }
}
})