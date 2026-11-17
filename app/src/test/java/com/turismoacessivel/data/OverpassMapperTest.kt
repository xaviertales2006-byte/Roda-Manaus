package com.turismoacessivel.data

import com.turismoacessivel.data.remote.OverpassCenter
import com.turismoacessivel.data.remote.OverpassElement
import com.turismoacessivel.data.remote.OverpassMapper
import com.turismoacessivel.domain.Categoria
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OverpassMapperTest {

    @Test
    fun `converte museu com acessibilidade informada`() {
        val elemento = OverpassElement(
            type = "node",
            id = 10,
            lat = -3.13,
            lon = -60.02,
            tags = mapOf(
                "name" to "Museu da Cidade",
                "tourism" to "museum",
                "wheelchair" to "yes",
                "toilets:wheelchair" to "yes",
                "tactile_paving" to "no",
                "addr:street" to "Rua Central",
                "addr:housenumber" to "100",
            ),
        )
        val local = OverpassMapper.paraEntidade(elemento, "Manaus")
        assertNotNull(local)
        assertEquals("Museu da Cidade", local!!.nome)
        assertEquals(Categoria.MUSEUS.name, local.categoria)
        assertEquals(true, local.rampa)
        assertEquals(true, local.banheiroAdaptado)
        assertEquals(false, local.pisoTatil)
        assertNull(local.braille)
        assertNull(local.libras)
        assertEquals("Rua Central, 100", local.endereco)
    }

    @Test
    fun `usa o centro quando o elemento e uma via`() {
        val elemento = OverpassElement(
            type = "way",
            id = 5,
            center = OverpassCenter(-3.1, -60.0),
            tags = mapOf("name" to "Parque do Mindu", "leisure" to "park"),
        )
        val local = OverpassMapper.paraEntidade(elemento, "Manaus")!!
        assertEquals(-3.1, local.latitude, 0.0001)
        assertEquals(Categoria.PRACAS.name, local.categoria)
        assertEquals("Endereço não informado no OpenStreetMap", local.endereco)
    }

    @Test
    fun `descarta elementos sem nome ou sem categoria`() {
        val semNome = OverpassElement("node", 1, -3.0, -60.0, null, mapOf("tourism" to "museum"))
        val semCategoria = OverpassElement("node", 2, -3.0, -60.0, null, mapOf("name" to "Poste", "man_made" to "mast"))
        assertNull(OverpassMapper.paraEntidade(semNome, "Manaus"))
        assertNull(OverpassMapper.paraEntidade(semCategoria, "Manaus"))
    }

    @Test
    fun `acesso limitado nao vira sim nem nao`() {
        val elemento = OverpassElement(
            "node", 3, -3.0, -60.0, null,
            mapOf("name" to "Café", "amenity" to "cafe", "wheelchair" to "limited"),
        )
        val local = OverpassMapper.paraEntidade(elemento, "Manaus")!!
        assertNull(local.rampa)
        assertTrue(local.descricao.contains("parcial"))
        assertFalse(local.descricao.isBlank())
    }

    @Test
    fun `ids de tipos diferentes nao colidem`() {
        val no = OverpassElement(type = "node", id = 7)
        val via = OverpassElement(type = "way", id = 7)
        assertNotEquals(OverpassMapper.idUnico(no), OverpassMapper.idUnico(via))
    }
}
