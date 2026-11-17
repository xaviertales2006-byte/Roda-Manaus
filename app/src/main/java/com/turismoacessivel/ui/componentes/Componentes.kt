@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.turismoacessivel.ui.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.turismoacessivel.domain.Avaliacao
import com.turismoacessivel.domain.Categoria
import com.turismoacessivel.domain.Criterio
import com.turismoacessivel.domain.FonteInfo
import com.turismoacessivel.domain.LocalAcessivel
import com.turismoacessivel.domain.StatusCriterio
import com.turismoacessivel.ui.icone
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val LocalePtBr = Locale("pt", "BR")

fun formatarNota(valor: Double): String = String.format(LocalePtBr, "%.1f", valor)

@Composable
fun FotoLocal(fotoUrl: String?, categoria: Categoria, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        if (fotoUrl != null) {
            AsyncImage(
                model = fotoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )
        } else {
            Icon(
                imageVector = categoria.icone,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }
}

@Composable
fun NotaBadge(score: Double?, modifier: Modifier = Modifier) {
    val descricao = if (score == null) {
        "Sem avaliação de acessibilidade"
    } else {
        "Nota de acessibilidade ${formatarNota(score)} de 5"
    }
    Row(
        modifier = modifier.clearAndSetSemantics { contentDescription = descricao },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = Color(0xFFB26A00),
            modifier = Modifier.size(20.dp),
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = if (score == null) "—" else formatarNota(score),
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun SeloCriterio(texto: String, icone: ImageVector) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icone, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(texto, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
fun CartaoLocal(local: LocalAcessivel, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column {
            FotoLocal(
                fotoUrl = local.fotoUrl,
                categoria = local.categoria,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
            )
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = local.nome,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(Modifier.width(8.dp))
                    NotaBadge(local.score)
                }
                Text(
                    text = "${local.categoria.rotulo} · ${local.cidade}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                val atendidos = local.criterios.filter { it.value.atendido == true }.keys.take(3)
                if (atendidos.isEmpty()) {
                    Text(
                        text = "Nenhum recurso de acessibilidade confirmado ainda",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        atendidos.forEach { criterio -> SeloCriterio(criterio.rotulo, criterio.icone) }
                    }
                }
            }
        }
    }
}

@Composable
fun LinhaCriterio(criterio: Criterio, status: StatusCriterio, modifier: Modifier = Modifier) {
    val textoStatus: String
    val iconeStatus: ImageVector
    when (status.atendido) {
        true -> {
            textoStatus = "Disponível"
            iconeStatus = Icons.Filled.CheckCircle
        }
        false -> {
            textoStatus = "Não disponível"
            iconeStatus = Icons.Filled.Cancel
        }
        null -> {
            textoStatus = "Sem informação"
            iconeStatus = Icons.Filled.Info
        }
    }
    val fonte = when (status.fonte) {
        FonteInfo.OPENSTREETMAP -> "Fonte: OpenStreetMap"
        FonteInfo.COMUNIDADE -> "Fonte: avaliações da comunidade"
        FonteInfo.DESCONHECIDA -> "Ninguém informou ainda"
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {}
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(criterio.icone, contentDescription = null, modifier = Modifier.size(28.dp))
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = criterio.rotulo,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = fonte,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(Modifier.width(8.dp))
        Icon(iconeStatus, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(4.dp))
        Text(textoStatus, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun CartaoAvaliacao(avaliacao: Avaliacao, modifier: Modifier = Modifier) {
    val data = SimpleDateFormat("dd/MM/yyyy", LocalePtBr).format(Date(avaliacao.criadoEm))
    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {},
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Nota ${avaliacao.estrelas} de 5 · $data",
                style = MaterialTheme.typography.titleSmall,
            )
            if (avaliacao.comentario.isNotBlank()) {
                Text(avaliacao.comentario, style = MaterialTheme.typography.bodyMedium)
            }
            if (avaliacao.fotoPath != null) {
                AsyncImage(
                    model = File(avaliacao.fotoPath),
                    contentDescription = "Foto de acessibilidade enviada pela comunidade",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(8.dp)),
                )
            }
        }
    }
}
