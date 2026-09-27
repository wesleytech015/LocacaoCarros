package com.wesley.locacaocarros.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.wesley.locacaocarros.data.local.entity.Cliente
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {

    @Query("SELECT * FROM clientes ORDER BY nome")
    fun listarTodos(): Flow<List<Cliente>>

    @Query("SELECT * FROM clientes WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): Cliente?

    @Insert
    suspend fun inserir(cliente: Cliente): Long
}