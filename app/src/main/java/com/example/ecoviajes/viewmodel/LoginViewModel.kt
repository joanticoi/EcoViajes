package com.example.ecoviajes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoviajes.model.User
import com.example.ecoviajes.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel (){
    private val repositorio = AuthRepository()

    private val _user = MutableStateFlow<User?>(null)

    val user: StateFlow<User?> = _user

    private val _carga = MutableStateFlow(false)

    val carga: StateFlow<Boolean> = _carga


    fun login(correo: String, clave: String){
        _carga.value = true

        viewModelScope.launch {
            try{
                _user.value = repositorio.login(correo, clave)
            } catch (e: Exception) {
                _user.value = null
            } finally {
                _carga.value = false
            }
            _user.value = repositorio.login(correo, clave)
            _carga.value = false
        }
    }
}