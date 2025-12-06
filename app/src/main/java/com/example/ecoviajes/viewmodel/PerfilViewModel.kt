package com.example.ecoviajes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoviajes.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PerfilUiState(
    val nombre: String = "",
    val correo: String = "",
    val cargando: Boolean = false,
    val error: String? = null,
    val mensaje: String? = null
)

class PerfilViewModel : ViewModel() {

    private val usuarioRepository = UsuarioRepository()

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState

    /**
     * Debe llamarse después del login, pasándole el correo del usuario autenticado.
     */
    fun inicializar(correo: String) {
        if (correo.isBlank()) return

        val estadoActual = _uiState.value

        // Si ya tenemos cargado el mismo usuario con nombre, no recargamos
        if (estadoActual.correo == correo && estadoActual.nombre.isNotBlank()) {
            return
        }

        _uiState.value = estadoActual.copy(correo = correo)
        cargarDatosUsuario()
    }

    fun cargarDatosUsuario() {
        val correo = _uiState.value.correo
        if (correo.isBlank()) return

        _uiState.value = _uiState.value.copy(cargando = true)

        viewModelScope.launch {
            try {
                val usuario = usuarioRepository.obtenerUsuarioPorCorreo(correo)
                _uiState.value = _uiState.value.copy(
                    nombre = usuario?.nombre ?: "",
                    cargando = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    cargando = false,
                    error = "Error al cargar los datos"
                )
            }
        }
    }

    fun guardarCambios(nuevoNombre: String) {
        val correo = _uiState.value.correo
        if (correo.isBlank()) return

        _uiState.value = _uiState.value.copy(cargando = true, error = null, mensaje = null)

        viewModelScope.launch {
            val ok = usuarioRepository.actualizarNombreUsuario(correo, nuevoNombre)
            _uiState.value = if (ok) {
                _uiState.value.copy(
                    nombre = nuevoNombre,
                    cargando = false,
                    mensaje = "Datos actualizados correctamente"
                )
            } else {
                _uiState.value.copy(
                    cargando = false,
                    error = "No se pudieron guardar los cambios"
                )
            }
        }
    }

    fun eliminarCuenta(onSuccess: () -> Unit) {
        val correo = _uiState.value.correo

        if (correo.isBlank()) {
            _uiState.value = _uiState.value.copy(
                error = "No se pudo obtener el usuario actual"
            )
            return
        }

        _uiState.value = _uiState.value.copy(cargando = true, error = null, mensaje = null)

        viewModelScope.launch {
            try {
                val ok = usuarioRepository.eliminarUsuario(correo)

                if (ok) {
                    _uiState.value = _uiState.value.copy(cargando = false)
                    onSuccess()
                } else {
                    _uiState.value = _uiState.value.copy(
                        cargando = false,
                        error = "Error al eliminar los datos del usuario"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    cargando = false,
                    error = "No se pudo eliminar la cuenta. Intenta de nuevo."
                )
            }
        }
    }

    fun limpiarMensajes() {
        _uiState.value = _uiState.value.copy(
            error = null,
            mensaje = null
        )
    }
}
