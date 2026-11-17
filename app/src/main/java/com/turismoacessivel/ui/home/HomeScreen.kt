@file:OptIn(ExperimentalMaterial3Api::class)

package com.turismoacessivel.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turismoacessivel.domain.Categoria
import com.turismoacessivel.ui.componentes.CartaoLocal

@Composable
fun HomeScreen(
    onAbrirLocal: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Roda Manaus") },
                actions = {
                    IconButton(onClick = viewModel::atualizar, enabled = !estado.carregando) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Atualizar lista de locais")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            OutlinedTextField(
                value = estado.busca,
                onValueChange = viewModel::buscar,
                label = { Text("Buscar por nome ou endereço") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    FilterChip(
                        selected = estado.categoria == null,
                        onClick = { viewModel.selecionarCategoria(null) },
                        label = { Text("Todos") },
                    )
                }
                items(Categoria.entries) { categoria ->
                    FilterChip(
                        selected = estado.categoria == categoria,
                        onClick = { viewModel.selecionarCategoria(categoria) },
                        label = { Text(categoria.rotulo) },
                    )
                }
                item {
                    FilterChip(
                        selected = estado.somenteBarreiraZero,
                        onClick = viewModel::alternarBarreiraZero,
                        label = { Text("Barreira zero") },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Accessible,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                            )
                        },
                    )
                }
            }

            if (estado.carregando) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                )
            }

            val aviso = estado.mensagem
            if (aviso != null) {
                Text(
                    text = aviso,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }

            if (estado.locais.isEmpty() && !estado.carregando) {
                Text(
                    text = "Nenhum local para mostrar. Use o botão de atualizar ou mude os filtros.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp),
                )
            } else {
                Text(
                    text = "${estado.locais.size} locais em ${estado.cidade}, do mais para o menos acessível",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(estado.locais, key = { it.id }) { local ->
                    CartaoLocal(local = local, onClick = { onAbrirLocal(local.id) })
                }
            }
        }
    }
}
