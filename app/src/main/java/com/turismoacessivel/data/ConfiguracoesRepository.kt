package com.turismoacessivel.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "configuracoes")

data class Configuracoes(
    val cidade: String = "Manaus",
    val altoContraste: Boolean = false,
    val escalaTexto: Float = 1f,
)

/** Preferências do usuário salvas com DataStore. */
@Singleton
class ConfiguracoesRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private object Chaves {
        val CIDADE = stringPreferencesKey("cidade")
        val ALTO_CONTRASTE = booleanPreferencesKey("alto_contraste")
        val ESCALA_TEXTO = floatPreferencesKey("escala_texto")
    }

    val configuracoes: Flow<Configuracoes> = context.dataStore.data.map { prefs ->
        Configuracoes(
            cidade = prefs[Chaves.CIDADE] ?: "Manaus",
            altoContraste = prefs[Chaves.ALTO_CONTRASTE] ?: false,
            escalaTexto = prefs[Chaves.ESCALA_TEXTO] ?: 1f,
        )
    }

    suspend fun definirCidade(cidade: String) {
        context.dataStore.edit { it[Chaves.CIDADE] = cidade.trim() }
    }

    suspend fun definirAltoContraste(ativo: Boolean) {
        context.dataStore.edit { it[Chaves.ALTO_CONTRASTE] = ativo }
    }

    suspend fun definirEscalaTexto(escala: Float) {
        context.dataStore.edit { it[Chaves.ESCALA_TEXTO] = escala }
    }
}
