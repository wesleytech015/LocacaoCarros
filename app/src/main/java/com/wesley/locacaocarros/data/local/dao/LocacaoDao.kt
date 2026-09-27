package com.wesley.locacaocarros.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Transaction
import com.wesley.locacaocarros.data.local.entity.Locacao
import com.wesley.locacaocarros.data.local.relation.LocacaoCompleta
import kotlinx.coroutines.flow.Flow

@Dao
interface LocacaoDao {

    @Query("SELECT * FROM locacoes WHERE status = 'ATIVA' ORDER BY dataEntregaPrevista")
    fun listarAtivas(): Flow<List<Locacao>>

    @Transaction
    @Query("SELECT * FROM locacoes WHERE status = 'ATIVA' ORDER BY dataEntregaPrevista")
    fun listarAtivasComDetalhes(): Flow<List<LocacaoCompleta>>

    @Query("SELECT * FROM locacoes ORDER BY dataSaida DESC")
    fun listarTodas(): Flow<List<Locacao>>

    @Insert
    suspend fun inserir(locacao: Locacao): Long

    @Query("UPDATE locacoes SET status = :status WHERE id = :locacaoId")
    suspend fun atualizarStatus(locacaoId: Int, status: String)
}