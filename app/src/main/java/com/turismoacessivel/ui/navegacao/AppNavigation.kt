package com.turismoacessivel.ui.navegacao

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.turismoacessivel.ui.avaliar.AvaliarScreen
import com.turismoacessivel.ui.config.ConfiguracoesScreen
import com.turismoacessivel.ui.detalhe.DetalheScreen
import com.turismoacessivel.ui.favoritos.FavoritosScreen
import com.turismoacessivel.ui.home.HomeScreen

object Rotas {
    const val HOME = "home"
    const val FAVORITOS = "favoritos"
    const val CONFIG = "config"
    const val DETALHE = "detalhe/{id}"
    const val AVALIAR = "avaliar/{id}"

    fun detalhe(id: Long) = "detalhe/$id"
    fun avaliar(id: Long) = "avaliar/$id"
}

private data class Aba(val rota: String, val rotulo: String, val icone: ImageVector)

private val abas = listOf(
    Aba(Rotas.HOME, "Explorar", Icons.Filled.Explore),
    Aba(Rotas.FAVORITOS, "Favoritos", Icons.Filled.Favorite),
    Aba(Rotas.CONFIG, "Ajustes", Icons.Filled.Settings),
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val entradaAtual by navController.currentBackStackEntryAsState()
    val rotaAtual = entradaAtual?.destination?.route

    Scaffold(
        bottomBar = {
            if (abas.any { it.rota == rotaAtual }) {
                NavigationBar {
                    abas.forEach { aba ->
                        NavigationBarItem(
                            selected = rotaAtual == aba.rota,
                            onClick = {
                                navController.navigate(aba.rota) {
                                    popUpTo(Rotas.HOME) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(aba.icone, contentDescription = null) },
                            label = { Text(aba.rotulo) },
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Rotas.HOME,
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding),
        ) {
            composable(Rotas.HOME) {
                HomeScreen(onAbrirLocal = { navController.navigate(Rotas.detalhe(it)) })
            }
            composable(Rotas.FAVORITOS) {
                FavoritosScreen(onAbrirLocal = { navController.navigate(Rotas.detalhe(it)) })
            }
            composable(Rotas.CONFIG) {
                ConfiguracoesScreen()
            }
            composable(
                route = Rotas.DETALHE,
                arguments = listOf(navArgument("id") { type = NavType.LongType }),
            ) {
                DetalheScreen(
                    onVoltar = { navController.popBackStack() },
                    onAvaliar = { navController.navigate(Rotas.avaliar(it)) },
                )
            }
            composable(
                route = Rotas.AVALIAR,
                arguments = listOf(navArgument("id") { type = NavType.LongType }),
            ) {
                AvaliarScreen(onConcluir = { navController.popBackStack() })
            }
        }
    }
}
