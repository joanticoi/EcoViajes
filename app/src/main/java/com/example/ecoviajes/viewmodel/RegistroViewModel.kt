package com.example.ecoviajes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoviajes.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistroViewModel : ViewModel() {
    private val repositorio = UsuarioRepository()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    private val _registroExitoso = MutableStateFlow(false)
    val registroExitoso: StateFlow<Boolean> = _registroExitoso

    private val _errorMensaje = MutableStateFlow("")
    val errorMensaje: StateFlow<String> = _errorMensaje

    fun registroUsuario(
        correo: String,
        clave: String,
        confirmarClave: String,
        nombre: String,
        telefono: String,
        foto: String
    ) {
        if (correo.isEmpty() || clave.isEmpty() || confirmarClave.isEmpty() || nombre.isEmpty()) {
            _errorMensaje.value = "Correo, nombre y contraseña son obligatorios"
            return
        }

        if (clave != confirmarClave) {
            _errorMensaje.value = "Las contraseñas no coinciden"
            return
        }

        if (clave.length < 6) {
            _errorMensaje.value = "La contraseña debe tener al menos 6 caracteres"
            return
        }

        _cargando.value = true
        _errorMensaje.value = ""

        viewModelScope.launch {
            val exitoso = repositorio.registroUsuario(
                correo = correo,
                clave = clave,
                nombre = nombre,
                telefono = telefono,
                foto = foto
            )

            _cargando.value = false
            _registroExitoso.value = exitoso

            if (!exitoso) {
                _errorMensaje.value = "Error al registrar usuario (puede que el correo ya exista)"
            }
        }
    }

    fun limpiarRegistro() {
        _registroExitoso.value = false
        _errorMensaje.value = ""
    }
}
