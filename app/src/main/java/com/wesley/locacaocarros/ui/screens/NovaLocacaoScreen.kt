package com.wesley.locacaocarros.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wesley.locacaocarros.data.local.entity.Veiculo
import com.wesley.locacaocarros.viewmodel.LocacaoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaLocacaoScreen(
    viewModel: LocacaoViewModel,
    onSelecionarContato: () -> Unit,
    onLocacaoSalva: () -> Unit
) {

    val veiculos by viewModel.veiculosDisponiveis.collectAsStateWithLifecycle()

    val contato = viewModel.contatoSelecionado

    var veiculoSelecionado by remember {
        mutableStateOf<Veiculo?>(null)
    }

    var dataSaida by remember {
        mutableStateOf<Long?>(null)
    }

    var dataEntrega by remember {
        mutableStateOf<Long?>(null)
    }

    var mostrarDatePickerSaida by remember {
        mutableStateOf(false)
    }

    var mostrarDatePickerEntrega by remember {
        mutableStateOf(false)
    }

    var mensagemErro by remember {
        mutableStateOf("")
    }

    val diasLocacao =
        if (
            dataSaida != null &&
            dataEntrega != null &&
            dataEntrega!! >= dataSaida!!
        ) {

            TimeUnit.MILLISECONDS
                .toDays(
                    dataEntrega!! - dataSaida!!
                )
                .toInt()
                .coerceAtLeast(1)

        } else {
            0
        }

    val valorTotal =
        if (
            veiculoSelecionado != null &&
            diasLocacao > 0
        ) {
            veiculoSelecionado!!.valorDiaria * diasLocacao
        } else {
            0.0
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Nova Locação",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Cliente",
            style = MaterialTheme.typography.titleMedium
        )

        if (contato == null) {

            Button(
                onClick = onSelecionarContato,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Selecionar cliente")
            }

        } else {

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = contato.nome,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = contato.telefone
                    )
                }
            }

            Button(
                onClick = onSelecionarContato,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Trocar cliente")
            }
        }

        Text(
            text = "Veículo",
            style = MaterialTheme.typography.titleMedium
        )

        if (veiculos.isEmpty()) {

            Text(
                text = "Nenhum veículo disponível."
            )

        } else {

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                veiculos.forEach { veiculo ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                veiculoSelecionado = veiculo
                            }
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
                                text = "Diária: R$ %.2f".format(
                                    veiculo.valorDiaria
                                )
                            )

                            if (
                                veiculoSelecionado?.id == veiculo.id
                            ) {
                                Text(
                                    text = "Selecionado",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }

        Text(
            text = "Data de saída",
            style = MaterialTheme.typography.titleMedium
        )

        Button(
            onClick = {
                mostrarDatePickerSaida = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = if (dataSaida == null) {
                    "Selecionar data de saída"
                } else {
                    formatarData(dataSaida!!)
                }
            )
        }

        Text(
            text = "Data prevista de entrega",
            style = MaterialTheme.typography.titleMedium
        )

        Button(
            onClick = {
                mostrarDatePickerEntrega = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = if (dataEntrega == null) {
                    "Selecionar data de entrega"
                } else {
                    formatarData(dataEntrega!!)
                }
            )
        }

        if (
            dataSaida != null &&
            dataEntrega != null
        ) {

            if (dataEntrega!! < dataSaida!!) {

                Text(
                    text = "A data de entrega não pode ser anterior à data de saída.",
                    color = MaterialTheme.colorScheme.error
                )

            } else {

                Text(
                    text = "Quantidade de diárias: $diasLocacao"
                )

                Text(
                    text = "Valor total estimado: R$ %.2f".format(
                        valorTotal
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        if (mensagemErro.isNotEmpty()) {

            Text(
                text = mensagemErro,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {

                when {

                    contato == null -> {
                        mensagemErro =
                            "Selecione um cliente."
                    }

                    veiculoSelecionado == null -> {
                        mensagemErro =
                            "Selecione um veículo."
                    }

                    dataSaida == null -> {
                        mensagemErro =
                            "Selecione a data de saída."
                    }

                    dataEntrega == null -> {
                        mensagemErro =
                            "Selecione a data de entrega."
                    }

                    dataEntrega!! < dataSaida!! -> {
                        mensagemErro =
                            "A data de entrega não pode ser anterior à data de saída."
                    }

                    else -> {

                        mensagemErro = ""

                        viewModel.realizarLocacao(
                            veiculoId = veiculoSelecionado!!.id,
                            dataSaida = dataSaida!!,
                            dataEntregaPrevista = dataEntrega!!,
                            valorTotal = valorTotal,
                            onSucesso = onLocacaoSalva
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Finalizar locação")
        }
    }

    if (mostrarDatePickerSaida) {

        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis =
                    dataSaida ?: System.currentTimeMillis()
            )

        DatePickerDialog(
            onDismissRequest = {
                mostrarDatePickerSaida = false
            },
            confirmButton = {

                TextButton(
                    onClick = {

                        dataSaida =
                            datePickerState.selectedDateMillis

                        mostrarDatePickerSaida = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        mostrarDatePickerSaida = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        ) {

            DatePicker(
                state = datePickerState
            )
        }
    }

    if (mostrarDatePickerEntrega) {

        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis =
                    dataEntrega
                        ?: dataSaida
                        ?: System.currentTimeMillis()
            )

        DatePickerDialog(
            onDismissRequest = {
                mostrarDatePickerEntrega = false
            },
            confirmButton = {

                TextButton(
                    onClick = {

                        dataEntrega =
                            datePickerState.selectedDateMillis

                        mostrarDatePickerEntrega = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        mostrarDatePickerEntrega = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        ) {

            DatePicker(
                state = datePickerState
            )
        }
    }
}

private fun formatarData(
    timestamp: Long
): String {

    val formato =
        SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        )

    return formato.format(
        Date(timestamp)
    )
}