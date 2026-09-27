package com.wesley.locacaocarros.data.local.relation

import androidx.room3.Embedded
import androidx.room3.Relation
import com.wesley.locacaocarros.data.local.entity.Cliente
import com.wesley.locacaocarros.data.local.entity.Locacao
import com.wesley.locacaocarros.data.local.entity.Veiculo

data class LocacaoCompleta(

    @Embedded
    val locacao: Locacao,

    @Relation(
        parentColumns = ["veiculoId"],
        entityColumns = ["id"]
    )
    val veiculo: Veiculo,

    @Relation(
        parentColumns = ["clienteId"],
        entityColumns = ["id"]
    )
    val cliente: Cliente
)