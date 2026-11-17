package com.turismoacessivel.data.remote

import com.turismoacessivel.data.local.LocalEntity
import com.turismoacessivel.domain.Categoria

/** Converte elementos do OpenStreetMap em locais do app. */
object OverpassMapper {

    fun paraEntidade(elemento: OverpassElement, cidade: String): LocalEntity? {
        val tags = elemento.tags ?: return null
        val nome = tags["name"]?.takeIf { it.isNotBlank() } ?: return null
        val lat = elemento.lat ?: elemento.center?.lat ?: return null
        val lon = elemento.lon ?: elemento.center?.lon ?: return null
        val categoria = categoriaDe(tags) ?: return null

        val rampa = sim(tags["ramp:wheelchair"]) ?: when (tags["wheelchair"]?.lowercase()) {
            "yes", "designated" -> true
            "no" -> false
            else -> null
        }
        val braille = tags.entries
            .firstOrNull { it.key.startsWith("tactile_writing:braille") || it.key == "braille" }
            ?.let { sim(it.value) }

        var descricao = tags["description:pt"] ?: tags["description"] ?: ""
        if (tags["wheelchair"]?.lowercase() == "limited") {
            descricao = (descricao + " Acesso parcial para cadeira de rodas (OpenStreetMap).").trim()
        }

        return LocalEntity(
            id = idUnico(elemento),
            nome = nome,
            categoria = categoria.name,
            cidade = cidade,
            endereco = montarEndereco(tags),
            latitude = lat,
            longitude = lon,
            descricao = descricao,
            fotoUrl = tags["image"]?.takeIf { it.startsWith("http") }?.replace("http://", "https://"),
            rampa = rampa,
            banheiroAdaptado = sim(tags["toilets:wheelchair"]),
            pisoTatil = sim(tags["tactile_paving"]),
            braille = braille,
            libras = null,
            iluminacao = null,
        )
    }

    /** Nós, vias e relações do OSM podem repetir o mesmo número; o tipo entra no id. */
    fun idUnico(elemento: OverpassElement): Long {
        val sufixo = when (elemento.type) {
            "node" -> 0
            "way" -> 1
            else -> 2
        }
        return elemento.id * 3 + sufixo
    }

    fun categoriaDe(tags: Map<String, String>): Categoria? {
        val turismo = tags["tourism"]
        val comodidade = tags["amenity"]
        val lazer = tags["leisure"]
        return when {
            comodidade == "restaurant" || comodidade == "cafe" -> Categoria.RESTAURANTES
            turismo == "museum" || turismo == "gallery" || comodidade == "arts_centre" -> Categoria.MUSEUS
            lazer == "park" || lazer == "garden" -> Categoria.PRACAS
            comodidade == "theatre" || comodidade == "cinema" -> Categoria.TEATROS
            turismo == "hotel" || turismo == "hostel" || turismo == "guest_house" -> Categoria.HOTEIS
            turismo != null || tags["historic"] != null -> Categoria.ATRACOES
            else -> null
        }
    }

    private fun montarEndereco(tags: Map<String, String>): String {
        val ruaNumero = listOfNotNull(tags["addr:street"], tags["addr:housenumber"]).joinToString(", ")
        val bairro = tags["addr:suburb"] ?: tags["addr:neighbourhood"] ?: ""
        return listOf(ruaNumero, bairro)
            .filter { it.isNotBlank() }
            .joinToString(" - ")
            .ifBlank { "Endereço não informado no OpenStreetMap" }
    }

    private fun sim(valor: String?): Boolean? = when (valor?.lowercase()) {
        "yes", "designated" -> true
        "no" -> false
        else -> null
    }
}
