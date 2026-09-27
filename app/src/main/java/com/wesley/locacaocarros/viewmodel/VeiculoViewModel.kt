package com.wesley.locacaocarros.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wesley.locacaocarros.data.local.entity.Veiculo
import com.wesley.locacaocarros.data.repository.VeiculoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VeiculoViewModel(
    private val repository: VeiculoRepository
) : ViewModel() {

    val veiculos: StateFlow<List<Veiculo>> =
        repository.listarTodos()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun cadastrarVeiculo(
        marca: String,
        modelo: String,
        placa: String,
        ano: Int,
        valorDiaria: Double
    ) {
        val veiculo = Veiculo(
            marca = marca,
            modelo = modelo,
            placa = placa,
            ano = ano,
            valorDiaria = valorDiaria
        )

        viewModelScope.launch {
            repository.inserir(veiculo)
        }
    }
}