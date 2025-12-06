package com.example.ecoviajes.viewmodel

import com.google.firebase.firestore.FirebaseFirestore
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class RegistroValidacionesTest : BehaviorSpec({

    // 1. Configuración del entorno de prueba
    val dispatcher = StandardTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(dispatcher)

        // TRUCO: Mockeamos la clase estática de Firebase.
        // Esto evita que la app se rompa al intentar conectarse a la base de datos real
        // cuando el ViewModel crea el 'UsuarioRepository' internamente.
        mockkStatic(FirebaseFirestore::class)
        every { FirebaseFirestore.getInstance() } returns mockk(relaxed = true)
    }

    afterSpec {
        Dispatchers.resetMain()
        unmockkAll() // Limpiamos los mocks para no afectar otros tests
    }

    given("un RegistroViewModel real sin modificaciones") {

        // Instanciamos el ViewModel tal cual está en tu código
        val viewModel = RegistroViewModel()

        `when`("se intenta registrar con algún campo vacío") {
            viewModel.registroUsuario(
                correo = "",
                clave = "123456",
                confirmarClave = "123456",
                nombre = "Juan"
            )

            then("debe mostrar error de campos obligatorios") {
                viewModel.errorMensaje.value shouldBe "Todos los campos son obligatorios"
            }
        }

        `when`("las contraseñas no son iguales") {
            viewModel.registroUsuario(
                correo = "juan@ecoviajes.cl",
                clave = "secret123",
                confirmarClave = "otraCosa", // Diferente
                nombre = "Juan"
            )

            then("debe detectar que no coinciden") {
                viewModel.errorMensaje.value shouldBe "Las contraseñas no coinciden"
            }
        }

        `when`("la contraseña es demasiado corta") {
            viewModel.registroUsuario(
                correo = "juan@ecoviajes.cl",
                clave = "123", // Menos de 6 caracteres
                confirmarClave = "123",
                nombre = "Juan"
            )

            then("debe pedir un mínimo de caracteres") {
                viewModel.errorMensaje.value shouldBe "La contraseña debe tener al menos 6 caracteres"
            }
        }

        `when`("se limpian los errores") {
            // Generamos un error primero
            viewModel.registroUsuario("", "", "", "")

            // Ejecutamos la función de limpieza
            viewModel.limpiarRegistro()

            then("el mensaje de error debe desaparecer") {
                viewModel.errorMensaje.value shouldBe ""
            }
        }
    }
})