@file:OptIn(ExperimentalMaterial3Api::class)

package com.turismoacessivel.ui.detalhe

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turismoacessivel.domain.Criterio
import com.turismoacessivel.domain.LocalAcessivel
import com.turismoacessivel.ui.componentes.CartaoAvaliacao
import com.turismoacessivel.ui.componentes.FotoLocal
import com.turismoacessivel.ui.componentes.LinhaCriterio
import com.turismoacessivel.ui.componentes.NotaBadge

@Composable
fun DetalheScreen(
    onVoltar: () -> Unit,
    onAvaliar: (Long) -> Unit,
    viewModel: DetalheViewModel = hiltViewModel(),
) {
    val local by viewModel.local.collectAsStateWithLifecycle()
    val avaliacoes by viewModel.avaliacoes.collectAsStateWithLifecycle()
    val contexto = LocalContext.current
    val atual = local

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(atual?.nome ?: "Local", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    if (atual != null) {
                        IconButton(onClick = viewModel::alternarFavorito) {
                            Icon(
                                imageVector = if (atual.favorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = if (atual.favorito) {
                                    "Remover dos favoritos"
                                } else {
                                    "Adicionar aos favoritos"
                                },
                            )
                        }
                    }
                },
            )
        },
    ) { padding ->
        if (atual == null) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    FotoLocal(
                        fotoUrl = atual.fotoUrl,
                        categoria = atual.categoria,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                    )
                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "${atual.categoria.rotulo} · ${atual.cidade}",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            NotaBadge(atual.score)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = if (atual.totalAvaliacoes == 0) {
                                    "sem avaliações da comunidade"
                                } else {
                                    "${atual.totalAvaliacoes} avaliações da comunidade"
                                },
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                        Text(atual.endereco, style = MaterialTheme.typography.bodyMedium)
                        if (atual.descricao.isNotBlank()) {
                            Text(atual.descricao, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { abrirRota(contexto, atual) },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Icon(Icons.Filled.Directions, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Como chegar")
                        }
                        OutlinedButton(
                            onClick = { onAvaliar(atual.id) },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Icon(Icons.Filled.RateReview, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Avaliar a acessibilidade")
                        }
                    }
                }
                item {
                    Text(
                        text = "Checklist de acessibilidade",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.semantics { heading() },
                    )
                }
                items(Criterio.entries) { criterio ->
                    LinhaCriterio(criterio, atual.criterios.getValue(criterio))
                }
                item {
                    Text(
                        text = "Avaliações da comunidade",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.semantics { heading() },
                    )
                }
                if (avaliacoes.isEmpty()) {
                    item {
                        Text(
                            text = "Ninguém avaliou este local ainda. Seja a primeira pessoa!",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                } else {
                    items(avaliacoes, key = { it.id }) { avaliacao ->
                        CartaoAvaliacao(avaliacao)
                    }
                }
            }
        }
    }
}

/** Abre o Google Maps com a rota até o local; se não houver, usa qualquer app de mapas. */
private fun abrirRota(contexto: Context, local: LocalAcessivel) {
    val navegacao = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("google.navigation:q=${local.latitude},${local.longitude}"),
    ).setPackage("com.google.android.apps.maps")
    val generico = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("geo:${local.latitude},${local.longitude}?q=${local.latitude},${local.longitude}(${Uri.encode(local.nome)})"),
    )
    try {
        contexto.startActivity(navegacao)
    } catch (e: ActivityNotFoundException) {
        try {
            contexto.startActivity(generico)
        } catch (e2: ActivityNotFoundException) {
            Toast.makeText(contexto, "Nenhum aplicativo de mapas encontrado.", Toast.LENGTH_LONG).show()
        }
    }
}
