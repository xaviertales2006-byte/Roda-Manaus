package com.turismoacessivel.data

import com.turismoacessivel.data.local.AvaliacaoEntity
import com.turismoacessivel.data.local.LocalEntity
import com.turismoacessivel.domain.Avaliacao
import com.turismoacessivel.domain.Categoria
import com.turismoacessivel.domain.Criterio
import com.turismoacessivel.domain.FonteInfo
import com.turismoacessivel.domain.LocalAcessivel
import com.turismoacessivel.domain.StatusCriterio

/**
 * Junta os dados do mapa com as avaliações da comunidade.
 * Para cada critério, a maioria dos votos da comunidade vale mais que o OpenStreetMap.
 */
fun LocalEntity.paraDominio(avaliacoes: List<AvaliacaoEntity>, favorito: Boolean): LocalAcessivel {
    val doMapa = mapOf(
        Criterio.RAMPA to rampa,
        Criterio.BANHEIRO_ADAPTADO to banheiroAdaptado,
        Criterio.PISO_TATIL to pisoTatil,
        Criterio.BRAILLE to braille,
        Criterio.LIBRAS to libras,
        Criterio.ILUMINACAO to iluminacao,
    )
    val criterios = Criterio.entries.associateWith { criterio ->
        val votos = avaliacoes.map { it.voto(criterio) }
        when {
            votos.isNotEmpty() ->
                StatusCriterio(votos.count { it } > votos.count { !it }, FonteInfo.COMUNIDADE)
            doMapa[criterio] != null ->
                StatusCriterio(doMapa[criterio], FonteInfo.OPENSTREETMAP)
            else -> StatusCriterio(null, FonteInfo.DESCONHECIDA)
        }
    }
    return LocalAcessivel(
        id = id,
        nome = nome,
        categoria = runCatching { Categoria.valueOf(categoria) }.getOrDefault(Categoria.ATRACOES),
        cidade = cidade,
        endereco = endereco,
        latitude = latitude,
        longitude = longitude,
        descricao = descricao,
        fotoUrl = fotoUrl,
        criterios = criterios,
        notaComunidade = if (avaliacoes.isEmpty()) null else avaliacoes.map { it.estrelas }.average(),
        totalAvaliacoes = avaliacoes.size,
        favorito = favorito,
    )
}

fun AvaliacaoEntity.paraDominio(): Avaliacao =
    Avaliacao(id = id, estrelas = estrelas, comentario = comentario, fotoPath = fotoPath, criadoEm = criadoEm)

private fun AvaliacaoEntity.voto(criterio: Criterio): Boolean = when (criterio) {
    Criterio.RAMPA -> rampa
    Criterio.BANHEIRO_ADAPTADO -> banheiroAdaptado
    Criterio.PISO_TATIL -> pisoTatil
    Criterio.BRAILLE -> braille
    Criterio.LIBRAS -> libras
    Criterio.ILUMINACAO -> iluminacao
}
