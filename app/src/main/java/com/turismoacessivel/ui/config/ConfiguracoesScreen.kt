@file:OptIn(ExperimentalMaterial3Api::class)

package com.turismoacessivel.ui.config

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.math.roundToInt

@Composable
fun ConfiguracoesScreen(viewModel: ConfiguracoesViewModel = hiltViewModel()) {
    val config by viewModel.configuracoes.collectAsStateWithLifecycle()
    var cidade by rememberSaveable(config.cidade) { mutableStateOf(config.cidade) }
    var escala by remember(config.escalaTexto) { mutableFloatStateOf(config.escalaTexto) }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Ajustes") }) },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Cidade",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() },
            )
            OutlinedTextField(
                value = cidade,
                onValueChange = { cidade = it },
                label = { Text("Nome da cidade") },
                supportingText = { Text("Os locais vêm do OpenStreetMap. Use o nome oficial, como Manaus.") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Button(
                onClick = { viewModel.definirCidade(cidade) },
                enabled = cidade.isNotBlank() && cidade.trim() != config.cidade,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Aplicar cidade")
            }

            Text(
                text = "Acessibilidade do app",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() },
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = config.altoContraste,
                        role = Role.Switch,
                        onValueChange = viewModel::definirAltoContraste,
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Alto contraste",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f),
                )
                Switch(checked = config.altoContraste, onCheckedChange = null)
            }

            Text(
                text = "Tamanho do texto: ${(escala * 100).roundToInt()}%",
                style = MaterialTheme.typography.bodyLarge,
            )
            Slider(
                value = escala,
                onValueChange = { escala = it },
                onValueChangeFinished = { viewModel.definirEscalaTexto(escala) },
                valueRange = 0.85f..1.6f,
                steps = 4,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Tamanho do texto, ${(escala * 100).roundToInt()} por cento" },
            )

            Text(
                text = "Sobre os dados",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                text = "Locais e informações de acessibilidade vêm do OpenStreetMap " +
                    "(© colaboradores do OpenStreetMap, licença ODbL) pela API pública Overpass. " +
                    "As avaliações e fotos que você envia ficam salvas só neste aparelho.",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
