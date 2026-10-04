package com.wesley.locacaocarros.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.wesley.locacaocarros.viewmodel.VeiculoViewModel

@Composable
fun CadastroVeiculoScreen(
    viewModel: VeiculoViewModel,
    onVeiculoSalvo: () -> Unit
) {

    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var placa by remember { mutableStateOf("") }
    var ano by remember { mutableStateOf("") }
    var valorDiaria by remember { mutableStateOf("") }

    var mensagemErro by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text("Cadastrar veículo")

        OutlinedTextField(
            value = marca,
            onValueChange = { marca = it },
            label = {
                Text(
                    text = "Marca",
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
            value = modelo,
            onValueChange = { modelo = it },
            label = {
                Text(
                    text = "Modelo",
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
            value = placa,
            onValueChange = {
                placa = it.uppercase()
            },
            label = {
                Text(
                    text = "Placa",
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
            value = ano,
            onValueChange = { ano = it },
            label = {
                Text(
                    text = "Ano",
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
            value = valorDiaria,
            onValueChange = { valorDiaria = it },
            label = {
                Text(
                    text = "Valor da diária",
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

        if (mensagemErro.isNotEmpty()) {
            Text(
                text = mensagemErro,
                color = Color.Red
            )
        }

        Button(
            onClick = {

                val anoConvertido =
                    ano.toIntOrNull()

                val diariaConvertida =
                    valorDiaria
                        .replace(",", ".")
                        .toDoubleOrNull()

                when {

                    marca.isBlank() ||
                            modelo.isBlank() ||
                            placa.isBlank() ||
                            ano.isBlank() ||
                            valorDiaria.isBlank() -> {

                        mensagemErro =
                            "Preencha todos os campos."
                    }

                    !placaValida(placa) -> {

                        mensagemErro =
                            "Placa inválida. Use AAA-1234 ou AAA1A23."
                    }

                    anoConvertido == null -> {

                        mensagemErro =
                            "Ano inválido."
                    }

                    diariaConvertida == null -> {

                        mensagemErro =
                            "Valor da diária inválido."
                    }

                    diariaConvertida <= 0 -> {

                        mensagemErro =
                            "O valor da diária deve ser maior que zero."
                    }

                    else -> {

                        mensagemErro = ""

                        viewModel.cadastrarVeiculo(
                            marca = marca,
                            modelo = modelo,
                            placa = placa.trim(),
                            ano = anoConvertido,
                            valorDiaria = diariaConvertida
                        )

                        onVeiculoSalvo()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar veículo")
        }
    }
}

private fun placaValida(
    placa: String
): Boolean {

    val placaFormatada =
        placa.uppercase().trim()

    val padraoAntigo =
        Regex("^[A-Z]{3}-\\d{4}$")

    val padraoMercosul =
        Regex("^[A-Z]{3}\\d[A-Z]\\d{2}$")

    return padraoAntigo.matches(placaFormatada) ||
            padraoMercosul.matches(placaFormatada)
}