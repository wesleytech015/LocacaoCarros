package com.wesley.locacaocarros.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wesley.locacaocarros.data.local.entity.Veiculo
import com.wesley.locacaocarros.viewmodel.LocacaoViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun NovaLocacaoScreen(
    viewModel: LocacaoViewModel,
    onSelecionarContato: () -> Unit,
    onLocacaoSalva: () -> Unit
) {

    val veiculos by viewModel.veiculosDisponiveis.collectAsStateWithLifecycle()

    var veiculoSelecionado by remember {
        mutableStateOf<Veiculo?>(null)
    }

    var dataSaida by remember {
        mutableStateOf("")
    }

    var dataEntrega by remember {
        mutableStateOf("")
    }

    var mensagemErro by remember {
        mutableStateOf("")
    }

    val contato = viewModel.contatoSelecionado

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Nova Locação",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = if (contato == null) {
                "Nenhum cliente selecionado"
            } else {
                "Cliente: ${contato.nome} - ${contato.telefone}"
            }
        )

        Button(
            onClick = onSelecionarContato,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (contato == null) {
                    "Selecionar cliente"
                } else {
                    "Trocar cliente"
                }
            )
        }

        Text(
            text = "Selecione o veículo:",
            style = MaterialTheme.typography.titleMedium
        )

        if (veiculos.isEmpty()) {

            Text("Nenhum veículo disponível.")

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
                            modifier = Modifier.padding(12.dp)
                        ) {

                            Text(
                                text = "${veiculo.marca} ${veiculo.modelo}"
                            )

                            Text(
                                text = "Placa: ${veiculo.placa}"
                            )

                            Text(
                                text = "Diária: R$ %.2f".format(
                                    veiculo.valorDiaria
                                )
                            )

                            if (veiculoSelecionado?.id == veiculo.id) {
                                Text(
                                    text = "Selecionado",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }

        OutlinedTextField(
            value = dataSaida,
            onValueChange = {
                dataSaida = it
            },
            label = {
                Text(
                    text = "Data de saída - dd/MM/yyyy",
                    color = Color.DarkGray
                )
            },
            textStyle = TextStyle(
                color = Color.Black
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.DarkGray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = dataEntrega,
            onValueChange = {
                dataEntrega = it
            },
            label = {
                Text(
                    text = "Data de entrega - dd/MM/yyyy",
                    color = Color.DarkGray
                )
            },
            textStyle = TextStyle(
                color = Color.Black
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.DarkGray,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth()
        )

        val periodo = calcularPeriodo(
            dataSaida,
            dataEntrega
        )

        val quantidadeDias = periodo?.dias ?: 0

        val valorTotal =
            (veiculoSelecionado?.valorDiaria ?: 0.0) *
                    quantidadeDias

        if (
            quantidadeDias > 0 &&
            veiculoSelecionado != null
        ) {

            Text(
                text = "Quantidade de diárias: $quantidadeDias"
            )

            Text(
                text = "Valor total: R$ %.2f".format(
                    valorTotal
                ),
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (mensagemErro.isNotBlank()) {

            Text(
                text = mensagemErro,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {

                val veiculo = veiculoSelecionado

                val periodoValidado = calcularPeriodo(
                    dataSaida,
                    dataEntrega
                )

                when {

                    contato == null -> {
                        mensagemErro =
                            "Selecione um cliente."
                    }

                    veiculo == null -> {
                        mensagemErro =
                            "Selecione um veículo."
                    }

                    periodoValidado == null -> {
                        mensagemErro =
                            "Informe datas válidas."
                    }

                    else -> {

                        mensagemErro = ""

                        viewModel.realizarLocacao(
                            veiculoId = veiculo.id,
                            dataSaida = periodoValidado.inicio,
                            dataEntregaPrevista = periodoValidado.fim,
                            valorTotal =
                                veiculo.valorDiaria *
                                        periodoValidado.dias,
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
}

data class PeriodoLocacao(
    val inicio: Long,
    val fim: Long,
    val dias: Int
)

private fun calcularPeriodo(
    dataSaida: String,
    dataEntrega: String
): PeriodoLocacao? {

    if (
        dataSaida.isBlank() ||
        dataEntrega.isBlank()
    ) {
        return null
    }

    return try {

        val formato = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        )

        formato.isLenient = false

        val inicio =
            formato.parse(dataSaida)?.time
                ?: return null

        val fim =
            formato.parse(dataEntrega)?.time
                ?: return null

        if (fim < inicio) {
            return null
        }

        val diferenca = fim - inicio

        val dias = TimeUnit.MILLISECONDS
            .toDays(diferenca)
            .toInt()
            .coerceAtLeast(1)

        PeriodoLocacao(
            inicio = inicio,
            fim = fim,
            dias = dias
        )

    } catch (_: Exception) {
        null
    }
}