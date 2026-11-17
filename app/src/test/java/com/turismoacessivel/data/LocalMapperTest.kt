package com.turismoacessivel.data

import com.turismoacessivel.data.local.AvaliacaoEntity
import com.turismoacessivel.data.local.LocalEntity
import com.turismoacessivel.domain.Criterio
import com.turismoacessivel.domain.FonteInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LocalMapperTest {

    private val entidade = LocalEntity(
        id = 1,
        nome = "Teatro",
        categoria = "TEATROS",
        cidade = "Manaus",
        endereco = "Rua A",
        latitude = 0.0,
        longitude = 0.0,
        descricao = "",
        fotoUrl = null,
        rampa = true,
        banheiroAdaptado = null,
        pisoTatil = null,
        braille = null,
        libras = null,
        iluminacao = null,
    )

    private fun avaliacao(estrelas: Int, rampa: Boolean) = AvaliacaoEntity(
        localId = 1,
        estrelas = estrelas,
        rampa = rampa,
        banheiroAdaptado = false,
        pisoTatil = false,
        braille = false,
        libras = false,
        iluminacao = false,
        comentario = "",
        fotoPath = null,
        criadoEm = 0L,
    )

    @Test
    fun `sem avaliacoes usa o OpenStreetMap`() {
        val local = entidade.paraDominio(emptyList(), favorito = false)
        val rampa = local.criterios.getValue(Criterio.RAMPA)
        assertEquals(true, rampa.atendido)
        assertEquals(FonteInfo.OPENSTREETMAP, rampa.fonte)
        assertEquals(FonteInfo.DESCONHECIDA, local.criterios.getValue(Criterio.LIBRAS).fonte)
        assertNull(local.notaComunidade)
    }

    @Test
    fun `a maioria da comunidade vence o OpenStreetMap`() {
        val avaliacoes = listOf(avaliacao(2, rampa = false), avaliacao(3, rampa = false), avaliacao(5, rampa = true))
        val local = entidade.paraDominio(avaliacoes, favorito = true)
        val rampa = local.criterios.getValue(Criterio.RAMPA)
        assertEquals(false, rampa.atendido)
        assertEquals(FonteInfo.COMUNIDADE, rampa.fonte)
        assertEquals(3.3, local.score!!, 0.0001)
        assertEquals(3, local.totalAvaliacoes)
        assertEquals(true, local.favorito)
    }
}
