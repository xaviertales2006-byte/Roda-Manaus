package com.turismoacessivel.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

private val EsquemaClaro = lightColorScheme(
    primary = Color(0xFF3F51B5),
    onPrimary = Color.White,
)

private val EsquemaEscuro = darkColorScheme(
    primary = Color(0xFFB8C4FF),
    onPrimary = Color(0xFF0B1A6B),
)

private val EsquemaAltoContraste = lightColorScheme(
    primary = Color.Black,
    onPrimary = Color.White,
    primaryContainer = Color.White,
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFF0D47A1),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE3F2FD),
    onSecondaryContainer = Color.Black,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color.White,
    onSurfaceVariant = Color.Black,
    outline = Color.Black,
    outlineVariant = Color.Black,
    error = Color(0xFF8B0000),
    onError = Color.White,
)

@Composable
fun TurismoTheme(
    altoContraste: Boolean,
    escalaTexto: Float,
    content: @Composable () -> Unit,
) {
    val esquema = when {
        altoContraste -> EsquemaAltoContraste
        isSystemInDarkTheme() -> EsquemaEscuro
        else -> EsquemaClaro
    }
    val densidade = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(densidade.density, densidade.fontScale * escalaTexto),
    ) {
        MaterialTheme(colorScheme = esquema, content = content)
    }
}
