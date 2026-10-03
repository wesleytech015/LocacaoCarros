package com.wesley.locacaocarros.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wesley.locacaocarros.ui.screens.CadastroVeiculoScreen
import com.wesley.locacaocarros.ui.screens.ContatosScreen
import com.wesley.locacaocarros.ui.screens.LocacoesAtivasScreen
import com.wesley.locacaocarros.ui.screens.NovaLocacaoScreen
import com.wesley.locacaocarros.ui.screens.VeiculosScreen
import com.wesley.locacaocarros.viewmodel.LocacaoViewModel
import com.wesley.locacaocarros.viewmodel.VeiculoViewModel

@Composable
fun AppNavigation(
    veiculoViewModel: VeiculoViewModel,
    locacaoViewModel: LocacaoViewModel
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "veiculos"
    ) {

        composable("veiculos") {
            VeiculosScreen(
                viewModel = veiculoViewModel,
                onNovoVeiculo = {
                    navController.navigate("cadastro_veiculo")
                },
                onAbrirContatos = {
                    navController.navigate("contatos")
                },
                onNovaLocacao = {
                    navController.navigate("nova_locacao")
                },
                onLocacoesAtivas = {
                    navController.navigate("locacoes_ativas")
                }
            )
        }

        composable("cadastro_veiculo") {
            CadastroVeiculoScreen(
                viewModel = veiculoViewModel,
                onVeiculoSalvo = {
                    navController.popBackStack()
                }
            )
        }

        composable("nova_locacao") {
            NovaLocacaoScreen(
                viewModel = locacaoViewModel,
                onSelecionarContato = {
                    navController.navigate("selecionar_contato")
                },
                onLocacaoSalva = {
                    navController.popBackStack()
                }
            )
        }

        composable("selecionar_contato") {
            ContatosScreen(
                onContatoSelecionado = { contato ->
                    locacaoViewModel.selecionarContato(contato)
                    navController.popBackStack()
                }
            )
        }

        composable("contatos") {
            ContatosScreen(
                onContatoSelecionado = {
                    navController.popBackStack()
                }
            )
        }

        composable("locacoes_ativas") {
            LocacoesAtivasScreen(
                viewModel = locacaoViewModel
            )
        }
    }
}