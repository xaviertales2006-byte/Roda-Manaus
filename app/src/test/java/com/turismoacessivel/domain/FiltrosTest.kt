package com.turismoacessivel.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class FiltrosTest {

    private fun local(
        id: Long,
        nome: String,
        categoria: Categoria,
        nota: Double?,
        endereco: String = "Rua A",
    ) = LocalAcessivel(
        id = id,
        nome = nome,
        categoria = categoria,
        cidade = "Manaus",
        endereco = endereco,
        latitude = 0.0,
        longitude = 0.0,
        descricao = "",
        fotoUrl = null,
        criterios = Criterio.entries.associateWith { StatusCriterio(null, FonteInfo.DESCONHECIDA) },
        notaComunidade = nota,
        totalAvaliacoes = if (nota == null) 0 else 1,
        favorito = false,
    )

    private val locais = listOf(
        local(1, "Museu da Amazônia", Categoria.MUSEUS, 4.5),
        local(2, "Teatro Amazonas", Categoria.TEATROS, 3.0),
        local(3, "Restaurante Sol", Categoria.RESTAURANTES, null, endereco = "Rua do Museu"),
        local(4, "Praça Central", Categoria.PRACAS, 4.0),
    )

    @Test
    fun `ordena do mais para o menos acessivel e deixa sem nota por ultimo`() {
        val ids = filtrarLocais(locais, "", null, false).map { it.id }
        assertEquals(listOf(1L, 4L, 2L, 3L), ids)
    }

    @Test
    fun `busca por nome ou endereco ignora maiusculas`() {
        val ids = filtrarLocais(locais, "museu", null, false).map { it.id }
        assertEquals(listOf(1L, 3L), ids)
    }

    @Test
    fun `filtra por categoria`() {
        val ids = filtrarLocais(locais, "", Categoria.TEATROS, false).map { it.id }
        assertEquals(listOf(2L), ids)
    }

    @Test
    fun `barreira zero mostra so notas a partir de 4`() {
        val ids = filtrarLocais(locais, "", null, true).map { it.id }
        assertEquals(listOf(1L, 4L), ids)
    }
}
