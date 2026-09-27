package com.wesley.locacaocarros.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "clientes")
data class Cliente(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val contactId: Long,
    val nome: String,
    val telefone: String
)