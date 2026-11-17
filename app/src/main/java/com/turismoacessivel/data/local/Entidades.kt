package com.turismoacessivel.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** Local vindo da API (OpenStreetMap). Os critérios são null quando o mapa não informa. */
@Entity(tableName = "locais")
data class LocalEntity(
    @PrimaryKey val id: Long,
    val nome: String,
    val categoria: String,
    val cidade: String,
    val endereco: String,
    val latitude: Double,
    val longitude: Double,
    val descricao: String,
    val fotoUrl: String?,
    val rampa: Boolean?,
    val banheiroAdaptado: Boolean?,
    val pisoTatil: Boolean?,
    val braille: Boolean?,
    val libras: Boolean?,
    val iluminacao: Boolean?,
)

/** Avaliação feita pelo usuário no aparelho. */
@Entity(tableName = "avaliacoes", indices = [Index("localId")])
data class AvaliacaoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val localId: Long,
    val estrelas: Int,
    val rampa: Boolean,
    val banheiroAdaptado: Boolean,
    val pisoTatil: Boolean,
    val braille: Boolean,
    val libras: Boolean,
    val iluminacao: Boolean,
    val comentario: String,
    val fotoPath: String?,
    val criadoEm: Long,
)

@Entity(tableName = "favoritos")
data class FavoritoEntity(@PrimaryKey val localId: Long)
