@file:OptIn(ExperimentalMaterial3Api::class)

package com.turismoacessivel.ui.avaliar

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.turismoacessivel.domain.Criterio
import com.turismoacessivel.ui.icone

@Composable
fun AvaliarScreen(
    onConcluir: () -> Unit,
    viewModel: AvaliarViewModel = hiltViewModel(),
) {
    val local by viewModel.local.collectAsStateWithLifecycle()
    val salvando by viewModel.salvando.collectAsStateWithLifecycle()

    var estrelas by rememberSaveable { mutableStateOf(0) }
    var marcados by rememberSaveable { mutableStateOf(setOf<String>()) }
    var comentario by rememberSaveable { mutableStateOf("") }
    var fotoUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val seletorDeFoto = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
    ) { uri -> if (uri != null) fotoUri = uri }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Avaliar acessibilidade") },
                navigationIcon = {
                    IconButton(onClick = onConcluir) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar sem salvar")
                    }
                },
            )
        },
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
                text = local?.nome ?: "",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() },
            )

            Text("Sua nota de acessibilidade", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                for (nota in 1..5) {
                    IconButton(onClick = { estrelas = nota }) {
                        Icon(
                            imageVector = if (nota <= estrelas) Icons.Filled.Star else Icons.Filled.StarBorder,
                            contentDescription = "Dar nota $nota de 5",
                            tint = Color(0xFFB26A00),
                        )
                    }
                }
                Spacer(Modifier.width(8.dp))
                Text(if (estrelas == 0) "escolha uma nota" else "$estrelas de 5")
            }

            Text("O que existe neste local?", style = MaterialTheme.typography.titleMedium)
            for (criterio in Criterio.entries) {
                val marcado = criterio.name in marcados
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = marcado,
                            role = Role.Switch,
                            onValueChange = { novo ->
                                marcados = if (novo) marcados + criterio.name else marcados - criterio.name
                            },
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(criterio.icone, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(criterio.rotulo, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            criterio.descricao,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Switch(checked = marcado, onCheckedChange = null)
                }
            }

            OutlinedTextField(
                value = comentario,
                onValueChange = { comentario = it },
                label = { Text("Comentário (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )

            OutlinedButton(
                onClick = {
                    seletorDeFoto.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.Filled.AddAPhoto, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(if (fotoUri == null) "Anexar foto de acessibilidade" else "Trocar foto")
            }
            val foto = fotoUri
            if (foto != null) {
                AsyncImage(
                    model = foto,
                    contentDescription = "Foto selecionada para esta avaliação",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp)),
                )
            }

            Button(
                onClick = {
                    viewModel.salvar(
                        estrelas = estrelas,
                        criterios = marcados.map { Criterio.valueOf(it) }.toSet(),
                        comentario = comentario,
                        fotoUri = fotoUri,
                        aoConcluir = onConcluir,
                    )
                },
                enabled = estrelas > 0 && !salvando,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (salvando) "Salvando..." else "Salvar avaliação")
            }
        }
    }
}
