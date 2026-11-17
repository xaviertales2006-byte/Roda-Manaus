package com.turismoacessivel.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turismoacessivel.data.LocaisRepository
import com.turismoacessivel.domain.LocalAcessivel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class FavoritosViewModel @Inject constructor(
    repositorio: LocaisRepository,
) : ViewModel() {

    val favoritos: StateFlow<List<LocalAcessivel>> = repositorio.observarFavoritos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
