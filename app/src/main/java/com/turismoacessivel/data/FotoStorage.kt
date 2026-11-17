package com.turismoacessivel.data

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Copia a foto escolhida na galeria para a pasta privada do app. */
@Singleton
class FotoStorage @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    suspend fun copiar(uri: Uri): String? = withContext(Dispatchers.IO) {
        runCatching {
            val pasta = File(context.filesDir, "fotos").apply { mkdirs() }
            val destino = File(pasta, "foto_${System.currentTimeMillis()}.jpg")
            val entrada = context.contentResolver.openInputStream(uri)
                ?: return@runCatching null
            entrada.use { origem ->
                destino.outputStream().use { saida -> origem.copyTo(saida) }
            }
            destino.absolutePath
        }.getOrNull()
    }
}
