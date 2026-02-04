package com.juegosparachicos.ui.screens.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juegosparachicos.ads.AdBanner
import com.juegosparachicos.utils.SoundManager

/**
 * Pantalla del menú principal
 * Muestra los 3 botones de juegos y un banner publicitario en la parte inferior
 */
@Composable
fun MenuScreen(
    onNavigateToMemory: () -> Unit,
    onNavigateToPaint: () -> Unit,
    onNavigateToColors: () -> Unit
) {
    // Inicializar SoundManager
    val context = LocalContext.current
    val soundManager = remember { SoundManager(context) }

    // Liberar recursos cuando se desmonte la pantalla
    DisposableEffect(Unit) {
        onDispose { soundManager.release() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Contenido principal
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título
            Text(
                text = "¡A Jugar!",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Botón Juego de Memoria
            GameButton(
                text = "Memoria",
                icon = Icons.Default.Psychology,
                onClick = {
                    soundManager.playSound(SoundManager.SoundType.TAP)
                    onNavigateToMemory()
                },
                backgroundColor = Color(0xFFFF6B9D)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Juego de Pintar
            GameButton(
                text = "Pintar",
                icon = Icons.Default.Brush,
                onClick = {
                    soundManager.playSound(SoundManager.SoundType.TAP)
                    onNavigateToPaint()
                },
                backgroundColor = Color(0xFF4ECDC4)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Juego de Colores y Formas
            GameButton(
                text = "Colores y Formas",
                icon = Icons.Default.Extension,
                onClick = {
                    soundManager.playSound(SoundManager.SoundType.TAP)
                    onNavigateToColors()
                },
                backgroundColor = Color(0xFFFFD93D)
            )
        }

        // Banner publicitario en la parte inferior
        AdBanner()
    }
}

/**
 * Componente de botón grande para el menú
 */
@Composable
private fun GameButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    backgroundColor: Color
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .padding(end = 16.dp),
            tint = Color.White
        )
        Text(
            text = text,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
