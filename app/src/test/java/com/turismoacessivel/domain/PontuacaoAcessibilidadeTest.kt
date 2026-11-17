package com.turismoacessivel.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PontuacaoAcessibilidadeTest {

    private fun todosDesconhecidos(): Map<Criterio, Boolean?> =
        Criterio.entries.associateWith { null }

    @Test
    fun `nota da comunidade tem prioridade sobre os criterios`() {
        val criterios = todosDesconhecidos() + (Criterio.RAMPA to true)
        assertEquals(3.7, PontuacaoAcessibilidade.calcular(3.66, criterios)!!, 0.0001)
    }

    @Test
    fun `sem avaliacoes o score vem dos criterios confirmados`() {
        val criterios = todosDesconhecidos() +
            (Criterio.RAMPA to true) +
            (Criterio.BANHEIRO_ADAPTADO to true) +
            (Criterio.PISO_TATIL to true)
        // 3 de 6 critérios = 2,5 de 5
        assertEquals(2.5, PontuacaoAcessibilidade.calcular(null, criterios)!!, 0.0001)
    }

    @Test
    fun `sem nenhuma informacao o score e nulo`() {
        assertNull(PontuacaoAcessibilidade.calcular(null, todosDesconhecidos()))
    }

    @Test
    fun `todos os criterios confirmados dao nota maxima`() {
        val criterios = Criterio.entries.associateWith<Criterio, Boolean?> { true }
        assertEquals(5.0, PontuacaoAcessibilidade.calcular(null, criterios)!!, 0.0001)
    }
}
