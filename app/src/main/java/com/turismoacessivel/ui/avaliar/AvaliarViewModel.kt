package com.turismoacessivel.ui.avaliar

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turismoacessivel.data.LocaisRepository
import com.turismoacessivel.domain.Criterio
import com.turismoacessivel.domain.LocalAcessivel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AvaliarViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repositorio: LocaisRepository,
) : ViewModel() {

    private val localId: Long = checkNotNull(savedStateHandle.get<Long>("id"))

    val local: StateFlow<LocalAcessivel?> = repositorio.observarLocal(localId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _salvando = MutableStateFlow(false)
    val salvando: StateFlow<Boolean> = _salvando

    fun salvar(
        estrelas: Int,
        criterios: Set<Criterio>,
        comentario: String,
        fotoUri: Uri?,
        aoConcluir: () -> Unit,
    ) {
        if (_salvando.value) return
        viewModelScope.launch {
            _salvando.value = true
            repositorio.salvarAvaliacao(localId, estrelas, criterios, comentario, fotoUri)
            _salvando.value = false
            aoConcluir()
        }
    }
}
