package com.wesley.locacaocarros.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wesley.locacaocarros.viewmodel.LocacaoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LocacoesAtivasScreen(
    viewModel: LocacaoViewModel
) {

    val locacoes by viewModel.locacoesAtivas.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Locações Ativas",
            style = MaterialTheme.typography.headlineMedium
        )

        if (locacoes.isEmpty()) {

            Text(
                text = "Nenhuma locação ativa.",
                modifier = Modifier.padding(top = 24.dp)
            )

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(locacoes) { item ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = item.cliente.nome,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "${item.veiculo.marca} ${item.veiculo.modelo}"
                            )

                            Text(
                                text = "Placa: ${item.veiculo.placa}"
                            )

                            Text(
                                text = "Telefone: ${item.cliente.telefone}"
                            )

                            Text(
                                text = "Saída: ${
                                    formatarData(
                                        item.locacao.dataSaida
                                    )
                                }"
                            )

                            Text(
                                text = "Entrega: ${
                                    formatarData(
                                        item.locacao.dataEntregaPrevista
                                    )
                                }"
                            )

                            Text(
                                text = "Valor total: R$ %.2f".format(
                                    item.locacao.valorTotal
                                )
                            )

                            Text(
                                text = "Status: ${item.locacao.status}"
                            )

                            Button(
                                onClick = {
                                    viewModel.finalizarLocacao(
                                        locacaoId = item.locacao.id,
                                        veiculoId = item.veiculo.id
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp)
                            ) {
                                Text("Finalizar locação")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatarData(
    timestamp: Long
): String {

    val formato = SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.getDefault()
    )

    return formato.format(
        Date(timestamp)
    )
}