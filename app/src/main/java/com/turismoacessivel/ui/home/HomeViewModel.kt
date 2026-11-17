package com.turismoacessivel.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turismoacessivel.data.ConfiguracoesRepository
import com.turismoacessivel.data.LocaisRepository
import com.turismoacessivel.domain.Categoria
import com.turismoacessivel.domain.LocalAcessivel
import com.turismoacessivel.domain.filtrarLocais
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val locais: List<LocalAcessivel> = emptyList(),
    val cidade: String = "",
    val busca: String = "",
    val categoria: Categoria? = null,
    val somenteBarreiraZero: Boolean = false,
    val carregando: Boolean = false,
    val mensagem: String? = null,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repositorio: LocaisRepository,
    private val configuracoes: ConfiguracoesRepository,
) : ViewModel() {

    private data class Filtros(
        val busca: String = "",
        val categoria: Categoria? = null,
        val somenteBarreiraZero: Boolean = false,
    )

    private data class Status(val carregando: Boolean = false, val mensagem: String? = null)

    private val filtros = MutableStateFlow(Filtros())
    private val status = MutableStateFlow(Status())

    private val cidade = configuracoes.configuracoes
        .map { it.cidade }
        .distinctUntilChanged()

    private val locaisDaCidade = cidade.flatMapLatest { repositorio.observarLocais(it) }

    val estado: StateFlow<HomeUiState> = combine(
        locaisDaCidade,
        filtros,
        status,
        cidade,
    ) { locais, f, s, nomeCidade ->
        HomeUiState(
            locais = filtrarLocais(locais, f.busca, f.categoria, f.somenteBarreiraZero),
            cidade = nomeCidade,
            busca = f.busca,
            categoria = f.categoria,
            somenteBarreiraZero = f.somenteBarreiraZero,
            carregando = s.carregando,
            mensagem = s.mensagem,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    init {
        // Atualiza ao abrir o app e sempre que a cidade mudar nos ajustes.
        viewModelScope.launch {
            cidade.collect { atualizarCidade(it) }
        }
    }

    fun atualizar() {
        viewModelScope.launch {
            atualizarCidade(configuracoes.configuracoes.first().cidade)
        }
    }

    fun buscar(texto: String) = filtros.update { it.copy(busca = texto) }

    fun selecionarCategoria(categoria: Categoria?) = filtros.update { it.copy(categoria = categoria) }

    fun alternarBarreiraZero() =
        filtros.update { it.copy(somenteBarreiraZero = !it.somenteBarreiraZero) }

    private suspend fun atualizarCidade(nomeCidade: String) {
        status.update { Status(carregando = true) }
        val resultado = repositorio.atualizar(nomeCidade)
        val mensagem = resultado.fold(
            onSuccess = { quantidade ->
                if (quantidade == 0) {
                    "Nenhum local encontrado em $nomeCidade. Confira o nome da cidade nos Ajustes."
                } else {
                    null
                }
            },
            onFailure = { "Não foi possível atualizar agora. Mostrando os dados salvos no aparelho." },
        )
        status.update { Status(carregando = false, mensagem = mensagem) }
    }
}
