package com.turismoacessivel.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalDao {
    @Query("SELECT * FROM locais WHERE cidade = :cidade")
    fun observarPorCidade(cidade: String): Flow<List<LocalEntity>>

    @Query("SELECT * FROM locais")
    fun observarTodos(): Flow<List<LocalEntity>>

    @Query("SELECT * FROM locais WHERE id = :id")
    fun observarPorId(id: Long): Flow<LocalEntity?>

    @Upsert
    suspend fun salvar(locais: List<LocalEntity>)
}

@Dao
interface AvaliacaoDao {
    @Query("SELECT * FROM avaliacoes")
    fun observarTodas(): Flow<List<AvaliacaoEntity>>

    @Query("SELECT * FROM avaliacoes WHERE localId = :localId ORDER BY criadoEm DESC")
    fun observarPorLocal(localId: Long): Flow<List<AvaliacaoEntity>>

    @Insert
    suspend fun inserir(avaliacao: AvaliacaoEntity)
}

@Dao
interface FavoritoDao {
    @Query("SELECT localId FROM favoritos")
    fun observarIds(): Flow<List<Long>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun inserir(favorito: FavoritoEntity)

    @Query("DELETE FROM favoritos WHERE localId = :localId")
    suspend fun remover(localId: Long)
}
