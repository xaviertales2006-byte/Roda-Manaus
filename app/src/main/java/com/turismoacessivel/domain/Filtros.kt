package com.turismoacessivel.domain

/** Busca por texto, categoria e "barreira zero". Ordena do mais para o menos acessível. */
fun filtrarLocais(
    locais: List<LocalAcessivel>,
    busca: String,
    categoria: Categoria?,
    somenteBarreiraZero: Boolean,
): List<LocalAcessivel> {
    val termo = busca.trim().lowercase()
    return locais
        .filter { local ->
            val bateTexto = termo.isEmpty() ||
                local.nome.lowercase().contains(termo) ||
                local.endereco.lowercase().contains(termo)
            val bateCategoria = categoria == null || local.categoria == categoria
            val bateBarreira = !somenteBarreiraZero || local.barreiraZero
            bateTexto && bateCategoria && bateBarreira
        }
        .sortedWith(
            compareByDescending<LocalAcessivel> { it.score ?: -1.0 }.thenBy { it.nome },
        )
}
