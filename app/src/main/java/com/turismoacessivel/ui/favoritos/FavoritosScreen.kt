@file:OptIn(ExperimentalMaterial3Api::class)

package com.turismoacessivel.ui.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turismoacessivel.ui.componentes.CartaoLocal

@Composable
fun FavoritosScreen(
    onAbrirLocal: (Long) -> Unit,
    viewModel: FavoritosViewModel = hiltViewModel(),
) {
    val favoritos by viewModel.favoritos.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Favoritos") }) },
    ) { padding ->
        if (favoritos.isEmpty()) {
            Text(
                text = "Você ainda não favoritou nenhum local. Abra um local e toque no coração.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(favoritos, key = { it.id }) { local ->
                    CartaoLocal(local = local, onClick = { onAbrirLocal(local.id) })
                }
            }
        }
    }
}
