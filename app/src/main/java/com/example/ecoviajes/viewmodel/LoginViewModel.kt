package com.example.ecoviajes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoviajes.model.User
import com.example.ecoviajes.repository.AuthRepository


class LoginViewModel : ViewModel (){
    private val repositorio = authRepository
}