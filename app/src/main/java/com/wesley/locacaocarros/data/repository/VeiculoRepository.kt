package com.wesley.locacaocarros.data.repository

import com.wesley.locacaocarros.data.local.dao.VeiculoDao
import com.wesley.locacaocarros.data.local.entity.Veiculo
import kotlinx.coroutines.flow.Flow

class VeiculoRepository(
    private val veiculoDao: VeiculoDao
) {

    fun listarTodos(): Flow<List<Veiculo>> {
        return veiculoDao.listarTodos()
    }

    fun listarDisponiveis(): Flow<List<Veiculo>> {
        return veiculoDao.listarDisponiveis()
    }

    suspend fun inserir(veiculo: Veiculo) {
        veiculoDao.inserir(veiculo)
    }

    suspend fun atualizar(veiculo: Veiculo) {
        veiculoDao.atualizar(veiculo)
    }

    suspend fun atualizarStatus(
        veiculoId: Int,
        status: String
    ) {
        veiculoDao.atualizarStatus(veiculoId, status)
    }
}