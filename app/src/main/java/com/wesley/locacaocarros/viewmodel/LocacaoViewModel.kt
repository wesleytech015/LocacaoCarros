package com.wesley.locacaocarros.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wesley.locacaocarros.data.repository.LocacaoRepository
import com.wesley.locacaocarros.ui.screens.Contato
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LocacaoViewModel(
    private val repository: LocacaoRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val CONTATO_ID = "contato_id"
        private const val CONTATO_NOME = "contato_nome"
        private const val CONTATO_TELEFONE = "contato_telefone"
    }

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

    val contatoSelecionado: Contato?
        get() {
            val id = savedStateHandle.get<String>(CONTATO_ID)
                ?: return null

            val nome = savedStateHandle.get<String>(CONTATO_NOME)
                ?: return null

            val telefone = savedStateHandle.get<String>(CONTATO_TELEFONE)
                ?: return null

            return Contato(
                id = id,
                nome = nome,
                telefone = telefone
            )
        }

    fun selecionarContato(contato: Contato) {
        savedStateHandle[CONTATO_ID] = contato.id
        savedStateHandle[CONTATO_NOME] = contato.nome
        savedStateHandle[CONTATO_TELEFONE] = contato.telefone
    }

    private fun limparContatoSelecionado() {
        savedStateHandle.remove<String>(CONTATO_ID)
        savedStateHandle.remove<String>(CONTATO_NOME)
        savedStateHandle.remove<String>(CONTATO_TELEFONE)
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

            limparContatoSelecionado()

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

    fun sincronizarLocacao(
        locacaoId: Int,
        veiculoId: Int,
        onResultado: (Boolean) -> Unit
    ) {
        viewModelScope.launch {

            val sucesso =
                repository.sincronizarLocacaoComApi(
                    locacaoId = locacaoId,
                    veiculoId = veiculoId
                )

            onResultado(sucesso)
        }
    }
}