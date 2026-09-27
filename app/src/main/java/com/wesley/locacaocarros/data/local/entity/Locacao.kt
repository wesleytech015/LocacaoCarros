package com.wesley.locacaocarros.data.local.entity

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "locacoes",
    foreignKeys = [
        ForeignKey(
            entity = Veiculo::class,
            parentColumns = ["id"],
            childColumns = ["veiculoId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Cliente::class,
            parentColumns = ["id"],
            childColumns = ["clienteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["veiculoId"]),
        Index(value = ["clienteId"])
    ]
)
data class Locacao(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val veiculoId: Int,
    val clienteId: Int,
    val dataSaida: Long,
    val dataEntregaPrevista: Long,
    val valorTotal: Double,
    val status: String = "ATIVA"
)