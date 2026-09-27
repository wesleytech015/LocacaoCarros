package com.wesley.locacaocarros.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.wesley.locacaocarros.data.local.entity.Veiculo
import kotlinx.coroutines.flow.Flow

@Dao
interface VeiculoDao {

    @Query("SELECT * FROM veiculos ORDER BY modelo")
    fun listarTodos(): Flow<List<Veiculo>>

    @Query("SELECT * FROM veiculos WHERE status = 'DISPONIVEL' ORDER BY modelo")
    fun listarDisponiveis(): Flow<List<Veiculo>>

    @Insert
    suspend fun inserir(veiculo: Veiculo): Long

    @Update
    suspend fun atualizar(veiculo: Veiculo)

    @Query("UPDATE veiculos SET status = :status WHERE id = :veiculoId")
    suspend fun atualizarStatus(veiculoId: Int, status: String)
}