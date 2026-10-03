package com.wesley.locacaocarros.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wesley.locacaocarros.data.repository.LocacaoRepository

class LocacaoViewModelFactory(
    private val repository: LocacaoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(LocacaoViewModel::class.java)) {
            return LocacaoViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}