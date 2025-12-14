package com.example.ecoviajes.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoviajes.repository.StorageRepository
import com.example.ecoviajes.repository.UsuarioRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.EmailAuthProvider


data class PerfilUiState(
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val foto: String = "",
    val cargando: Boolean = false,
    val mensaje: String? = null,
    val error: String? = null
)

class PerfilViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()          // ✅ faltaba
    private val usuarioRepository = UsuarioRepository()
    private val storageRepo = StorageRepository()

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState

    fun inicializar(correo: String) {
        if (correo.isBlank()) return
        val estadoActual = _uiState.value

        if (estadoActual.correo == correo && estadoActual.nombre.isNotBlank()) return

        _uiState.value = estadoActual.copy(correo = correo)
        cargarDatosUsuario()
    }

    fun cargarDatosUsuario() {
        val correoAuth = auth.currentUser?.email.orEmpty()
        val correo = if (_uiState.value.correo.isNotBlank()) _uiState.value.correo else correoAuth

        if (correo.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "No hay usuario autenticado")
            return
        }

        _uiState.value = _uiState.value.copy(correo = correo, cargando = true, error = null)

        viewModelScope.launch {
            try {
                val usuario = usuarioRepository.obtenerUsuarioPorCorreo(correo)
                _uiState.value = _uiState.value.copy(
                    nombre = usuario?.nombre ?: "",
                    telefono = usuario?.telefono ?: "",
                    foto = usuario?.foto ?: "",
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

    fun cambiarContrasenaFirestore(claveActual: String, nuevaClave: String) {
        val correo = _uiState.value.correo
        if (correo.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "No hay usuario autenticado")
            return
        }

        if (claveActual.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Ingresa tu contraseña actual")
            return
        }

        if (nuevaClave.length < 6) {
            _uiState.value = _uiState.value.copy(error = "La nueva contraseña debe tener al menos 6 caracteres")
            return
        }

        _uiState.value = _uiState.value.copy(cargando = true, error = null, mensaje = null)

        viewModelScope.launch {
            try {
                // ✅ 1) Verificar clave actual contra Firestore (como hace tu login)
                val usuario = usuarioRepository.obtenerUsuarioPorCorreo(correo)
                if (usuario == null || usuario.clave != claveActual) {
                    _uiState.value = _uiState.value.copy(
                        cargando = false,
                        error = "La contraseña actual no es correcta"
                    )
                    return@launch
                }

                // ✅ 2) Actualizar clave en Firestore
                val ok = usuarioRepository.actualizarClaveUsuario(correo, nuevaClave)

                _uiState.value = if (ok) {
                    _uiState.value.copy(
                        cargando = false,
                        mensaje = "Contraseña actualizada correctamente"
                    )
                } else {
                    _uiState.value.copy(
                        cargando = false,
                        error = "No se pudo actualizar la contraseña"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    cargando = false,
                    error = "Error: ${e.message ?: "desconocido"}"
                )
            }
        }
    }



    fun guardarCambios(
        nuevoNombre: String,
        nuevoTelefono: String,
        nuevaFotoUri: Uri?
    ) {
        val correo = _uiState.value.correo
        if (correo.isBlank()) return

        _uiState.value = _uiState.value.copy(cargando = true, error = null, mensaje = null)

        viewModelScope.launch {
            try {
                val fotoUrlFinal = if (nuevaFotoUri != null) {
                    storageRepo.subirFotoPerfil(nuevaFotoUri)
                } else {
                    _uiState.value.foto
                }

                val ok = usuarioRepository.actualizarPerfil(
                    correo = correo,
                    nuevoNombre = nuevoNombre,
                    nuevoTelefono = nuevoTelefono,
                    nuevaFotoUrl = fotoUrlFinal
                )

                _uiState.value = if (ok) {
                    _uiState.value.copy(
                        nombre = nuevoNombre,
                        telefono = nuevoTelefono,
                        foto = fotoUrlFinal,
                        cargando = false,
                        mensaje = "Perfil actualizado"
                    )
                } else {
                    _uiState.value.copy(
                        cargando = false,
                        error = "No se pudieron guardar los cambios"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    cargando = false,
                    error = "Error: ${e.message ?: "desconocido"}"
                )
            }
        }
    }

    fun eliminarCuenta(onSuccess: () -> Unit) {
        val correo = _uiState.value.correo
        if (correo.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "No se pudo obtener el usuario actual")
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
        _uiState.value = _uiState.value.copy(error = null, mensaje = null)
    }
}
