package com.wesley.locacaocarros

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.wesley.locacaocarros.data.local.DatabaseProvider
import com.wesley.locacaocarros.data.repository.LocacaoRepository
import com.wesley.locacaocarros.data.repository.VeiculoRepository
import com.wesley.locacaocarros.navigation.AppNavigation
import com.wesley.locacaocarros.ui.theme.LocacaoCarrosTheme
import com.wesley.locacaocarros.viewmodel.LocacaoViewModel
import com.wesley.locacaocarros.viewmodel.LocacaoViewModelFactory
import com.wesley.locacaocarros.viewmodel.VeiculoViewModel
import com.wesley.locacaocarros.viewmodel.VeiculoViewModelFactory

class MainActivity : ComponentActivity() {

    private val veiculoViewModel: VeiculoViewModel by viewModels {
        val banco = DatabaseProvider.getDatabase(applicationContext)

        val repository = VeiculoRepository(
            banco.veiculoDao()
        )

        VeiculoViewModelFactory(repository)
    }

    private val locacaoViewModel: LocacaoViewModel by viewModels {
        val banco = DatabaseProvider.getDatabase(applicationContext)

        val repository = LocacaoRepository(
            veiculoDao = banco.veiculoDao(),
            clienteDao = banco.clienteDao(),
            locacaoDao = banco.locacaoDao()
        )

        LocacaoViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            LocacaoCarrosTheme {
                AppNavigation(
                    veiculoViewModel = veiculoViewModel,
                    locacaoViewModel = locacaoViewModel
                )
            }
        }
    }
}