package com.turismoacessivel.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material.icons.filled.Texture
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Wc
import androidx.compose.ui.graphics.vector.ImageVector
import com.turismoacessivel.domain.Categoria
import com.turismoacessivel.domain.Criterio

val Criterio.icone: ImageVector
    get() = when (this) {
        Criterio.RAMPA -> Icons.Filled.Accessible
        Criterio.BANHEIRO_ADAPTADO -> Icons.Filled.Wc
        Criterio.PISO_TATIL -> Icons.Filled.Texture
        Criterio.BRAILLE -> Icons.Filled.TouchApp
        Criterio.LIBRAS -> Icons.Filled.Hearing
        Criterio.ILUMINACAO -> Icons.Filled.WbSunny
    }

val Categoria.icone: ImageVector
    get() = when (this) {
        Categoria.RESTAURANTES -> Icons.Filled.Restaurant
        Categoria.MUSEUS -> Icons.Filled.AccountBalance
        Categoria.PRACAS -> Icons.Filled.Park
        Categoria.TEATROS -> Icons.Filled.TheaterComedy
        Categoria.HOTEIS -> Icons.Filled.Hotel
        Categoria.ATRACOES -> Icons.Filled.Place
    }
