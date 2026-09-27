package com.wesley.locacaocarros.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "veiculos")
data class Veiculo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val marca: String,
    val modelo: String,
    val placa: String,
    val ano: Int,
    val valorDiaria: Double,
    val status: String = "DISPONIVEL"
)