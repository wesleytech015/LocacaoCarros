package com.wesley.locacaocarros.data.repository

import com.wesley.locacaocarros.data.local.dao.ClienteDao
import com.wesley.locacaocarros.data.local.dao.LocacaoDao
import com.wesley.locacaocarros.data.local.dao.VeiculoDao
import com.wesley.locacaocarros.data.local.entity.Cliente
import com.wesley.locacaocarros.data.local.entity.Locacao
import com.wesley.locacaocarros.data.remote.RetrofitClient
import com.wesley.locacaocarros.data.remote.SincronizacaoRequest

class LocacaoRepository(
    private val veiculoDao: VeiculoDao,
    private val clienteDao: ClienteDao,
    private val locacaoDao: LocacaoDao
) {

    val veiculosDisponiveis = veiculoDao.listarDisponiveis()

    val locacoesAtivas = locacaoDao.listarAtivasComDetalhes()

    suspend fun realizarLocacao(
        contactId: Long,
        nomeCliente: String,
        telefoneCliente: String,
        veiculoId: Int,
        dataSaida: Long,
        dataEntregaPrevista: Long,
        valorTotal: Double
    ) {
        val clienteId = clienteDao.inserir(
            Cliente(
                contactId = contactId,
                nome = nomeCliente,
                telefone = telefoneCliente
            )
        ).toInt()

        locacaoDao.inserir(
            Locacao(
                veiculoId = veiculoId,
                clienteId = clienteId,
                dataSaida = dataSaida,
                dataEntregaPrevista = dataEntregaPrevista,
                valorTotal = valorTotal,
                status = "ATIVA"
            )
        )

        veiculoDao.atualizarStatus(
            veiculoId = veiculoId,
            status = "ALUGADO"
        )
    }

    suspend fun finalizarLocacao(
        locacaoId: Int,
        veiculoId: Int
    ) {
        locacaoDao.atualizarStatus(
            locacaoId = locacaoId,
            status = "FINALIZADA"
        )

        veiculoDao.atualizarStatus(
            veiculoId = veiculoId,
            status = "DISPONIVEL"
        )
    }

    suspend fun sincronizarLocacaoComApi(
        locacaoId: Int,
        veiculoId: Int
    ): Boolean {
        return try {
            val resposta =
                RetrofitClient.apiService.sincronizarLocacao(
                    SincronizacaoRequest(
                        tipo = "LOCACAO",
                        descricao = "Locacao $locacaoId - Veiculo $veiculoId"
                    )
                )

            resposta.id != null

        } catch (e: Exception) {
            false
        }
    }
}