package com.wesley.locacaocarros.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.wesley.locacaocarros.data.local.dao.ClienteDao
import com.wesley.locacaocarros.data.local.dao.LocacaoDao
import com.wesley.locacaocarros.data.local.dao.VeiculoDao
import com.wesley.locacaocarros.data.local.entity.Cliente
import com.wesley.locacaocarros.data.local.entity.Locacao
import com.wesley.locacaocarros.data.local.entity.Veiculo

@Database(
    entities = [
        Veiculo::class,
        Cliente::class,
        Locacao::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BancoDeDados : RoomDatabase() {

    abstract fun veiculoDao(): VeiculoDao

    abstract fun clienteDao(): ClienteDao

    abstract fun locacaoDao(): LocacaoDao
}