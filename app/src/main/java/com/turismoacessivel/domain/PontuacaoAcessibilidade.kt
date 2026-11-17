package com.turismoacessivel.domain

import kotlin.math.roundToInt

/**
 * Regra do score de acessibilidade (0 a 5):
 * 1. Se a comunidade já avaliou, vale a média das estrelas.
 * 2. Senão, vale a proporção de critérios confirmados (de 6) vezes 5.
 * 3. Sem nenhuma informação, o score é nulo ("sem avaliação").
 */
object PontuacaoAcessibilidade {

    fun calcular(notaComunidade: Double?, criterios: Map<Criterio, Boolean?>): Double? {
        if (notaComunidade != null) return arredondar(notaComunidade)
        if (criterios.values.all { it == null }) return null
        val confirmados = criterios.values.count { it == true }
        return arredondar(confirmados * 5.0 / Criterio.entries.size)
    }

    private fun arredondar(valor: Double): Double = (valor * 10).roundToInt() / 10.0
}
