package com.turismoacessivel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turismoacessivel.data.Configuracoes
import com.turismoacessivel.data.ConfiguracoesRepository
import com.turismoacessivel.ui.navegacao.AppNavigation
import com.turismoacessivel.ui.theme.TurismoTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var configuracoesRepository: ConfiguracoesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val config by configuracoesRepository.configuracoes
                .collectAsStateWithLifecycle(initialValue = Configuracoes())
            TurismoTheme(
                altoContraste = config.altoContraste,
                escalaTexto = config.escalaTexto,
            ) {
                AppNavigation()
            }
        }
    }
}
