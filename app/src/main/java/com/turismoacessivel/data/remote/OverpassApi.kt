package com.turismoacessivel.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/** API pública Overpass (OpenStreetMap). Sem chave de acesso. */
interface OverpassApi {
    @GET("api/interpreter")
    suspend fun consultar(@Query("data") consulta: String): OverpassResponse
}

data class OverpassResponse(val elements: List<OverpassElement>? = null)

data class OverpassElement(
    val type: String? = null,
    val id: Long = 0,
    val lat: Double? = null,
    val lon: Double? = null,
    val center: OverpassCenter? = null,
    val tags: Map<String, String>? = null,
)

data class OverpassCenter(val lat: Double, val lon: Double)

object OverpassQuery {
    /** Pontos turísticos, restaurantes, teatros, parques e hotéis com nome em uma cidade. */
    fun porCidade(cidade: String, limite: Int = 300): String {
        val nome = cidade.replace("\"", "").trim()
        return """
            [out:json][timeout:40];
            area["name"="$nome"]["boundary"="administrative"]["admin_level"="8"]->.a;
            (
              nwr["name"]["tourism"~"^(museum|gallery|attraction|zoo|hotel|hostel|guest_house)${'$'}"](area.a);
              nwr["name"]["amenity"~"^(restaurant|cafe|theatre|cinema|arts_centre)${'$'}"](area.a);
              nwr["name"]["leisure"~"^(park|garden)${'$'}"](area.a);
              nwr["name"]["historic"~"^(monument|memorial|building|castle|ruins)${'$'}"](area.a);
            );
            out center tags $limite;
        """.trimIndent()
    }
}
