package com.wesley.locacaocarros.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wesley.locacaocarros.viewmodel.VeiculoViewModel

@Composable
fun VeiculosScreen(
    viewModel: VeiculoViewModel,
    onNovoVeiculo: () -> Unit,
    onAbrirContatos: () -> Unit,
    onNovaLocacao: () -> Unit,
    onLocacoesAtivas: () -> Unit
) {

    val veiculos by viewModel.veiculos.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNovoVeiculo,
                text = {
                    Text("Novo veículo")
                },
                icon = {
                    Text("+")
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Text(
                text = "Veículos",
                style = MaterialTheme.typography.headlineMedium
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 16.dp,
                        bottom = 8.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = onAbrirContatos
                ) {
                    Text("Contatos")
                }

                Button(
                    onClick = onNovaLocacao
                ) {
                    Text("Nova locação")
                }
            }

            Button(
                onClick = onLocacoesAtivas,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text("Locações ativas")
            }

            if (veiculos.isEmpty()) {

                Text(
                    text = "Nenhum veículo cadastrado.",
                    modifier = Modifier.padding(top = 24.dp)
                )

            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        vertical = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(veiculos) { veiculo ->

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    text = "${veiculo.marca} ${veiculo.modelo}",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = "Placa: ${veiculo.placa}"
                                )

                                Text(
                                    text = "Ano: ${veiculo.ano}"
                                )

                                Text(
                                    text = "Diária: R$ %.2f".format(
                                        veiculo.valorDiaria
                                    )
                                )

                                Text(
                                    text = "Status: ${veiculo.status}"
                                )

                                when (veiculo.status) {

                                    "DISPONIVEL" -> {

                                        Button(
                                            onClick = {
                                                viewModel.alterarStatusVeiculo(
                                                    veiculoId = veiculo.id,
                                                    status = "MANUTENCAO"
                                                )
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 12.dp)
                                        ) {
                                            Text("Colocar em manutenção")
                                        }
                                    }

                                    "MANUTENCAO" -> {

                                        Button(
                                            onClick = {
                                                viewModel.alterarStatusVeiculo(
                                                    veiculoId = veiculo.id,
                                                    status = "DISPONIVEL"
                                                )
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 12.dp)
                                        ) {
                                            Text("Tornar disponível")
                                        }
                                    }

                                    "ALUGADO" -> {

                                        Text(
                                            text = "Veículo atualmente alugado.",
                                            modifier = Modifier.padding(top = 12.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}