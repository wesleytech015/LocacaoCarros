package com.wesley.locacaocarros.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wesley.locacaocarros.ui.screens.CadastroVeiculoScreen
import com.wesley.locacaocarros.ui.screens.VeiculosScreen
import com.wesley.locacaocarros.viewmodel.VeiculoViewModel

@Composable
fun AppNavigation(
    viewModel: VeiculoViewModel
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "veiculos"
    ) {

        composable("veiculos") {

            VeiculosScreen(
                viewModel = viewModel,
                onNovoVeiculo = {
                    navController.navigate("cadastro_veiculo")
                }
            )
        }

        composable("cadastro_veiculo") {

            CadastroVeiculoScreen(
                viewModel = viewModel,
                onVeiculoSalvo = {
                    navController.popBackStack()
                }
            )
        }
    }
}