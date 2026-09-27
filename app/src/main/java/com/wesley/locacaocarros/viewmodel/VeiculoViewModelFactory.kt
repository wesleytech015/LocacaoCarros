package com.wesley.locacaocarros.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wesley.locacaocarros.data.repository.VeiculoRepository

class VeiculoViewModelFactory(
    private val repository: VeiculoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(VeiculoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VeiculoViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconhecido")
    }
}