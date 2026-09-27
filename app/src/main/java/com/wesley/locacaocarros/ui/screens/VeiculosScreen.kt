package com.wesley.locacaocarros.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    onNovoVeiculo: () -> Unit
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

            if (veiculos.isEmpty()) {

                Text(
                    text = "Nenhum veículo cadastrado.",
                    modifier = Modifier.padding(top = 24.dp)
                )

            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 16.dp),
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

                                Text("Placa: ${veiculo.placa}")
                                Text("Ano: ${veiculo.ano}")
                                Text("Diária: R$ %.2f".format(veiculo.valorDiaria))
                                Text("Status: ${veiculo.status}")
                            }
                        }
                    }
                }
            }
        }
    }
}