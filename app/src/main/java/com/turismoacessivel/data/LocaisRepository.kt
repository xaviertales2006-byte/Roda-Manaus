package com.turismoacessivel.data

import android.net.Uri
import com.turismoacessivel.data.local.AvaliacaoDao
import com.turismoacessivel.data.local.AvaliacaoEntity
import com.turismoacessivel.data.local.FavoritoDao
import com.turismoacessivel.data.local.FavoritoEntity
import com.turismoacessivel.data.local.LocalDao
import com.turismoacessivel.data.local.LocalEntity
import com.turismoacessivel.data.remote.OverpassApi
import com.turismoacessivel.data.remote.OverpassMapper
import com.turismoacessivel.data.remote.OverpassQuery
import com.turismoacessivel.domain.Avaliacao
import com.turismoacessivel.domain.Criterio
import com.turismoacessivel.domain.LocalAcessivel
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

/**
 * Fonte única de dados do app: busca na API, guarda no Room (funciona offline)
 * e junta com as avaliações e favoritos do usuário.
 */
@Singleton
class LocaisRepository @Inject constructor(
    private val api: OverpassApi,
    private val localDao: LocalDao,
    private val avaliacaoDao: AvaliacaoDao,
    private val favoritoDao: FavoritoDao,
    private val fotoStorage: FotoStorage,
) {

    fun observarLocais(cidade: String): Flow<List<LocalAcessivel>> =
        combine(
            localDao.observarPorCidade(cidade),
            avaliacaoDao.observarTodas(),
            favoritoDao.observarIds(),
        ) { locais, avaliacoes, favoritos -> montar(locais, avaliacoes, favoritos) }

    fun observarFavoritos(): Flow<List<LocalAcessivel>> =
        combine(
            localDao.observarTodos(),
            avaliacaoDao.observarTodas(),
            favoritoDao.observarIds(),
        ) { locais, avaliacoes, favoritos -> montar(locais, avaliacoes, favoritos) }
            .map { lista -> lista.filter { it.favorito } }

    fun observarLocal(id: Long): Flow<LocalAcessivel?> =
        combine(
            localDao.observarPorId(id),
            avaliacaoDao.observarPorLocal(id),
            favoritoDao.observarIds(),
        ) { local, avaliacoes, favoritos -> local?.paraDominio(avaliacoes, id in favoritos) }

    fun observarAvaliacoes(localId: Long): Flow<List<Avaliacao>> =
        avaliacaoDao.observarPorLocal(localId).map { lista -> lista.map { it.paraDominio() } }

    /** Busca os locais da cidade na API e salva no banco. Retorna quantos locais vieram. */
    suspend fun atualizar(cidade: String): Result<Int> = runCatching {
        val resposta = api.consultar(OverpassQuery.porCidade(cidade))
        val entidades = resposta.elements.orEmpty()
            .mapNotNull { OverpassMapper.paraEntidade(it, cidade) }
        localDao.salvar(entidades)
        entidades.size
    }

    suspend fun alternarFavorito(localId: Long, atualmenteFavorito: Boolean) {
        if (atualmenteFavorito) {
            favoritoDao.remover(localId)
        } else {
            favoritoDao.inserir(FavoritoEntity(localId))
        }
    }

    suspend fun salvarAvaliacao(
        localId: Long,
        estrelas: Int,
        criterios: Set<Criterio>,
        comentario: String,
        fotoUri: Uri?,
    ) {
        val caminhoFoto = fotoUri?.let { fotoStorage.copiar(it) }
        avaliacaoDao.inserir(
            AvaliacaoEntity(
                localId = localId,
                estrelas = estrelas,
                rampa = Criterio.RAMPA in criterios,
                banheiroAdaptado = Criterio.BANHEIRO_ADAPTADO in criterios,
                pisoTatil = Criterio.PISO_TATIL in criterios,
                braille = Criterio.BRAILLE in criterios,
                libras = Criterio.LIBRAS in criterios,
                iluminacao = Criterio.ILUMINACAO in criterios,
                comentario = comentario.trim(),
                fotoPath = caminhoFoto,
                criadoEm = System.currentTimeMillis(),
            ),
        )
    }

    private fun montar(
        locais: List<LocalEntity>,
        avaliacoes: List<AvaliacaoEntity>,
        favoritos: List<Long>,
    ): List<LocalAcessivel> {
        val porLocal = avaliacoes.groupBy { it.localId }
        val idsFavoritos = favoritos.toSet()
        return locais.map { it.paraDominio(porLocal[it.id].orEmpty(), it.id in idsFavoritos) }
    }
}
