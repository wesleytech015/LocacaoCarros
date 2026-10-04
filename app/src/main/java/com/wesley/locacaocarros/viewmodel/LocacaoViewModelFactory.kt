package com.wesley.locacaocarros.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.createSavedStateHandle
import com.wesley.locacaocarros.data.repository.LocacaoRepository

class LocacaoViewModelFactory(
    private val repository: LocacaoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {

        if (modelClass.isAssignableFrom(LocacaoViewModel::class.java)) {

            val savedStateHandle: SavedStateHandle =
                extras.createSavedStateHandle()

            return LocacaoViewModel(
                repository = repository,
                savedStateHandle = savedStateHandle
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconhecida")
    }
}