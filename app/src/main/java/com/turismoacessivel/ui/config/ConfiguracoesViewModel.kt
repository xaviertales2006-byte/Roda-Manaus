package com.turismoacessivel.ui.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turismoacessivel.data.Configuracoes
import com.turismoacessivel.data.ConfiguracoesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ConfiguracoesViewModel @Inject constructor(
    private val repositorio: ConfiguracoesRepository,
) : ViewModel() {

    val configuracoes: StateFlow<Configuracoes> = repositorio.configuracoes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Configuracoes())

    fun definirCidade(cidade: String) {
        viewModelScope.launch { repositorio.definirCidade(cidade) }
    }

    fun definirAltoContraste(ativo: Boolean) {
        viewModelScope.launch { repositorio.definirAltoContraste(ativo) }
    }

    fun definirEscalaTexto(escala: Float) {
        viewModelScope.launch { repositorio.definirEscalaTexto(escala) }
    }
}
