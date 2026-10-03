package com.wesley.locacaocarros.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

data class Contato(
    val id: String,
    val nome: String,
    val telefone: String
)

@Composable
fun ContatosScreen(
    onContatoSelecionado: (Contato) -> Unit
) {

    val context = LocalContext.current

    var permissaoConcedida by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var contatos by remember {
        mutableStateOf<List<Contato>>(emptyList())
    }

    var pesquisa by remember {
        mutableStateOf("")
    }

    val launcherPermissao = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedida ->

        permissaoConcedida = concedida

        if (concedida) {
            contatos = carregarContatos(context)
        }
    }

    LaunchedEffect(Unit) {

        if (permissaoConcedida) {
            contatos = carregarContatos(context)
        } else {
            launcherPermissao.launch(
                Manifest.permission.READ_CONTACTS
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Contatos",
            style = MaterialTheme.typography.headlineMedium
        )

        if (!permissaoConcedida) {

            Text(
                text = "É necessário permitir o acesso aos contatos.",
                modifier = Modifier.padding(top = 24.dp)
            )

            Button(
                onClick = {
                    launcherPermissao.launch(
                        Manifest.permission.READ_CONTACTS
                    )
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Permitir acesso")
            }

        } else {

            OutlinedTextField(
                value = pesquisa,
                onValueChange = {
                    pesquisa = it
                },
                label = {
                    Text("Pesquisar contato")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            val contatosFiltrados = contatos.filter { contato ->
                contato.nome.contains(
                    pesquisa,
                    ignoreCase = true
                )
            }

            if (contatosFiltrados.isEmpty()) {

                Text(
                    text = "Nenhum contato encontrado.",
                    modifier = Modifier.padding(top = 24.dp)
                )

            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(contatosFiltrados) { contato ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onContatoSelecionado(contato)
                                }
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
                    }
                }
            }
        }
    }
}

fun carregarContatos(
    context: Context
): List<Contato> {

    val contatos = mutableListOf<Contato>()

    val cursor = context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        ),
        null,
        null,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
    )

    cursor?.use {

        val idIndex = it.getColumnIndex(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        )

        val nomeIndex = it.getColumnIndex(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        )

        val telefoneIndex = it.getColumnIndex(
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        while (it.moveToNext()) {

            val id = it.getString(idIndex)
            val nome = it.getString(nomeIndex)
            val telefone = it.getString(telefoneIndex)

            contatos.add(
                Contato(
                    id = id,
                    nome = nome,
                    telefone = telefone
                )
            )
        }
    }

    return contatos.distinctBy {
        "${it.id}-${it.telefone}"
    }
}