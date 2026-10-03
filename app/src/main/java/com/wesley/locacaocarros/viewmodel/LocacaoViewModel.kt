package com.wesley.locacaocarros.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wesley.locacaocarros.data.repository.LocacaoRepository
import com.wesley.locacaocarros.ui.screens.Contato
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LocacaoViewModel(
    private val repository: LocacaoRepository
) : ViewModel() {

    val veiculosDisponiveis = repository.veiculosDisponiveis.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    val locacoesAtivas = repository.locacoesAtivas.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    var contatoSelecionado: Contato? = null
        private set

    fun selecionarContato(contato: Contato) {
        contatoSelecionado = contato
    }

    fun realizarLocacao(
        veiculoId: Int,
        dataSaida: Long,
        dataEntregaPrevista: Long,
        valorTotal: Double,
        onSucesso: () -> Unit
    ) {
        val contato = contatoSelecionado ?: return

        viewModelScope.launch {

            repository.realizarLocacao(
                contactId = contato.id.toLongOrNull() ?: 0L,
                nomeCliente = contato.nome,
                telefoneCliente = contato.telefone,
                veiculoId = veiculoId,
                dataSaida = dataSaida,
                dataEntregaPrevista = dataEntregaPrevista,
                valorTotal = valorTotal
            )

            contatoSelecionado = null

            onSucesso()
        }
    }

    fun finalizarLocacao(
        locacaoId: Int,
        veiculoId: Int
    ) {
        viewModelScope.launch {

            repository.finalizarLocacao(
                locacaoId = locacaoId,
                veiculoId = veiculoId
            )
        }
    }
}