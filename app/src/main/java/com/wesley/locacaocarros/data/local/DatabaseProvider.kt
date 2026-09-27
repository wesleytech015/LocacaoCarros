package com.wesley.locacaocarros.data.local

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

object DatabaseProvider {

    @Volatile
    private var INSTANCE: BancoDeDados? = null

    fun getDatabase(context: Context): BancoDeDados {

        return INSTANCE ?: synchronized(this) {

            val database = Room.databaseBuilder<BancoDeDados>(
                context = context.applicationContext,
                name = "locacao_carros.db"
            )
                .setDriver(BundledSQLiteDriver())
                .build()

            INSTANCE = database

            database
        }
    }
}