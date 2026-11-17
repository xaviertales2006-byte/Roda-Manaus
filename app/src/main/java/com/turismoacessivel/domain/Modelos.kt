package com.turismoacessivel.domain

enum class FonteInfo { OPENSTREETMAP, COMUNIDADE, DESCONHECIDA }

/** Situação de um critério: [atendido] é null quando ninguém informou. */
data class StatusCriterio(val atendido: Boolean?, val fonte: FonteInfo)

data class LocalAcessivel(
    val id: Long,
    val nome: String,
    val categoria: Categoria,
    val cidade: String,
    val endereco: String,
    val latitude: Double,
    val longitude: Double,
    val descricao: String,
    val fotoUrl: String?,
    val criterios: Map<Criterio, StatusCriterio>,
    val notaComunidade: Double?,
    val totalAvaliacoes: Int,
    val favorito: Boolean,
) {
    /** Nota de 0 a 5, ou null quando não há nenhuma informação. */
    val score: Double?
        get() = PontuacaoAcessibilidade.calcular(
            notaComunidade,
            criterios.mapValues { it.value.atendido },
        )

    /** "Barreira zero": locais com nota de acessibilidade 4,0 ou mais. */
    val barreiraZero: Boolean
        get() = (score ?: 0.0) >= 4.0
}

data class Avaliacao(
    val id: Long,
    val estrelas: Int,
    val comentario: String,
    val fotoPath: String?,
    val criadoEm: Long,
)
