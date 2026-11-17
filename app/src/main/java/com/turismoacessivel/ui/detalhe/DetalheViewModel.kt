package com.turismoacessivel.ui.detalhe

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turismoacessivel.data.LocaisRepository
import com.turismoacessivel.domain.Avaliacao
import com.turismoacessivel.domain.LocalAcessivel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class DetalheViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repositorio: LocaisRepository,
) : ViewModel() {

    private val localId: Long = checkNotNull(savedStateHandle.get<Long>("id"))

    val local: StateFlow<LocalAcessivel?> = repositorio.observarLocal(localId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val avaliacoes: StateFlow<List<Avaliacao>> = repositorio.observarAvaliacoes(localId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun alternarFavorito() {
        val atual = local.value ?: return
        viewModelScope.launch { repositorio.alternarFavorito(atual.id, atual.favorito) }
    }
}
