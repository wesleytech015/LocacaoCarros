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
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wesley.locacaocarros.viewmodel.LocacaoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun LocacoesAtivasScreen(
    viewModel: LocacaoViewModel,
    onNovaLocacao: () -> Unit
) {

    val locacoes by viewModel.locacoesAtivas.collectAsStateWithLifecycle()

    val mensagensSincronizacao = remember {
        mutableStateMapOf<Int, String>()
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNovaLocacao,
                text = {
                    Text("Nova locação")
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
                text = "Dashboard de Locações",
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

                        val diasFaltantes =
                            calcularDiasFaltantes(
                                item.locacao.dataEntregaPrevista
                            )

                        val atrasada =
                            diasFaltantes < 0L

                        val mensagemSincronizacao =
                            mensagensSincronizacao[item.locacao.id]

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    text = "${item.veiculo.marca} ${item.veiculo.modelo}",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = "Placa: ${item.veiculo.placa}"
                                )

                                Text(
                                    text = "Cliente: ${item.cliente.nome}"
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
                                    text = "Entrega prevista: ${
                                        formatarData(
                                            item.locacao.dataEntregaPrevista
                                        )
                                    }"
                                )

                                Text(
                                    text = when {

                                        diasFaltantes > 1L ->
                                            "Dias faltantes: $diasFaltantes dias"

                                        diasFaltantes == 1L ->
                                            "Dias faltantes: 1 dia"

                                        diasFaltantes == 0L ->
                                            "Entrega prevista para hoje"

                                        else ->
                                            "Atrasada há ${-diasFaltantes} dia(s)"
                                    },
                                    color = if (atrasada) {
                                        Color.Red
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    },
                                    style = MaterialTheme.typography.titleMedium
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
                                        viewModel.sincronizarLocacao(
                                            locacaoId = item.locacao.id,
                                            veiculoId = item.veiculo.id
                                        ) { sucesso ->

                                            mensagensSincronizacao[
                                                item.locacao.id
                                            ] =
                                                if (sucesso) {
                                                    "Sincronização realizada com sucesso."
                                                } else {
                                                    "Erro ao sincronizar com a API."
                                                }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp)
                                ) {
                                    Text("Sincronizar")
                                }

                                if (mensagemSincronizacao != null) {

                                    Text(
                                        text = mensagemSincronizacao,
                                        color = if (
                                            mensagemSincronizacao.startsWith(
                                                "Sincronização"
                                            )
                                        ) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.error
                                        },
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }

                                Button(
                                    onClick = {
                                        viewModel.finalizarLocacao(
                                            locacaoId = item.locacao.id,
                                            veiculoId = item.veiculo.id
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
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

private fun calcularDiasFaltantes(
    dataEntregaPrevista: Long
): Long {

    val hoje = Date()

    val formato = SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.getDefault()
    )

    val hojeSemHorario =
        formato.parse(
            formato.format(hoje)
        )?.time ?: hoje.time

    val entregaSemHorario =
        formato.parse(
            formato.format(
                Date(dataEntregaPrevista)
            )
        )?.time ?: dataEntregaPrevista

    val diferenca =
        entregaSemHorario - hojeSemHorario

    return TimeUnit.MILLISECONDS.toDays(
        diferenca
    )
}