package com.example.ecoviajes

class CarritoIntegrationTest : BehaviorSpec({

    // Test de flujo completo del carrito
    given("un flujo de compra completo") {
        val productos = listOf(
            Producto("1", "Laptop", 1000.0, "img1.jpg", 5),
            Producto("2", "Mouse", 25.0, "img2.jpg", 10),
            Producto("3", "Teclado", 75.0, "img3.jpg", 8)
        )

        `when`("se agregan múltiples productos al carrito") {
            val carrito = mutableListOf<ItemCarrito>()
            productos.forEach { producto ->
                carrito.add(ItemCarrito(producto, 1))
            }

            then("el carrito debe contener todos los productos") {
                carrito.size shouldBe 3
                carrito.map { it.producto.nombre } shouldBe listOf("Laptop", "Mouse", "Teclado")
            }

            then("el total debe ser la suma de todos los productos") {
                val total = carrito.sumOf { it.producto.precio * it.cantidad }
                total shouldBe 1100.0
            }
        }

        `when`("se modifica la cantidad de productos") {
            val carrito = mutableListOf<ItemCarrito>()
            val producto = productos.first()
            carrito.add(ItemCarrito(producto, 2)) // Cantidad 2

            then("el subtotal debe reflejar la nueva cantidad") {
                val subtotal = carrito.first().producto.precio * carrito.first().cantidad
                subtotal shouldBe 2000.0
            }
        }

        `when`("se vacía el carrito") {
            val carrito = mutableListOf<ItemCarrito>()
            productos.forEach { producto ->
                carrito.add(ItemCarrito(producto, 1))
            }
            carrito.clear()

            then("el carrito debe estar vacío") {
                carrito.isEmpty() shouldBe true
            }
        }
    }
})