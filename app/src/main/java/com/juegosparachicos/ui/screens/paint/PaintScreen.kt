package com.juegosparachicos.ui.screens.paint

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juegosparachicos.ui.theme.PaintBlue
import com.juegosparachicos.ui.theme.PaintGreen
import com.juegosparachicos.ui.theme.PaintOrange
import com.juegosparachicos.ui.theme.PaintPurple
import com.juegosparachicos.ui.theme.PaintRed
import com.juegosparachicos.ui.theme.PaintYellow
import com.juegosparachicos.utils.SoundManager

/**
 * Juego de Pintar
 * - Canvas de dibujo a pantalla completa
 * - Paleta de 6 colores fijos
 * - Botón borrar
 * - Sin guardar, sin capas, sin herramientas avanzadas
 */
@Composable
fun PaintScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val soundManager = remember { SoundManager(context) }

    DisposableEffect(Unit) {
        onDispose { soundManager.release() }
    }

    val paths = remember { mutableStateListOf<DrawingPath>() }
    var currentPoints by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var selectedColor by remember { mutableStateOf(PaintRed) }

    val colors = listOf(
        PaintRed,
        PaintBlue,
        PaintYellow,
        PaintGreen,
        PaintPurple,
        PaintOrange
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header con controles
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón Home
            IconButton(onClick = {
                soundManager.playSound(SoundManager.SoundType.TAP)
                onNavigateBack()
            }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Volver",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = "Pintar",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón Deshacer
                IconButton(
                    onClick = {
                        if (paths.isNotEmpty()) {
                            soundManager.playSound(SoundManager.SoundType.TAP)
                            paths.removeAt(paths.size - 1)
                        }
                    },
                    enabled = paths.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Default.Undo,
                        contentDescription = "Deshacer",
                        modifier = Modifier.size(32.dp),
                        tint = if (paths.isNotEmpty())
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Gray.copy(alpha = 0.3f)
                    )
                }

                // Botón Borrar
                IconButton(
                    onClick = {
                        soundManager.playSound(SoundManager.SoundType.TAP)
                        paths.clear()
                        currentPoints = emptyList()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Borrar todo",
                        modifier = Modifier.size(32.dp),
                        tint = Color.Red
                    )
                }
            }
        }

        // Paleta de colores
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            colors.forEach { color ->
                ColorButton(
                    color = color,
                    isSelected = color == selectedColor,
                    onClick = {
                        soundManager.playSound(SoundManager.SoundType.TAP)
                        selectedColor = color
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Canvas de dibujo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(4.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                currentPoints = listOf(offset)
                            },
                            onDrag = { change, _ ->
                                currentPoints = currentPoints + change.position
                            },
                            onDragEnd = {
                                if (currentPoints.isNotEmpty()) {
                                    paths.add(DrawingPath(currentPoints, selectedColor))
                                    currentPoints = emptyList()
                                }
                            }
                        )
                    }
            ) {
                // Dibujar todos los paths guardados
                paths.forEach { drawingPath ->
                    if (drawingPath.points.isNotEmpty()) {
                        val path = Path().apply {
                            moveTo(drawingPath.points[0].x, drawingPath.points[0].y)
                            drawingPath.points.drop(1).forEach { point ->
                                lineTo(point.x, point.y)
                            }
                        }
                        drawPath(
                            path = path,
                            color = drawingPath.color,
                            style = Stroke(width = 12f)
                        )
                    }
                }

                // Dibujar trazo actual en tiempo real
                if (currentPoints.isNotEmpty()) {
                    val path = Path().apply {
                        moveTo(currentPoints[0].x, currentPoints[0].y)
                        currentPoints.drop(1).forEach { point ->
                            lineTo(point.x, point.y)
                        }
                    }
                    drawPath(
                        path = path,
                        color = selectedColor,
                        style = Stroke(width = 12f)
                    )
                }
            }
        }
    }
}

/**
 * Botón de color para la paleta
 */
@Composable
private fun ColorButton(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(if (isSelected) 60.dp else 50.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = if (isSelected) 4.dp else 2.dp,
                color = if (isSelected) Color.Black else Color.Gray,
                shape = CircleShape
            )
            .clickable(onClick = onClick)
    )
}

/**
 * Data class para almacenar una línea dibujada
 * Usa lista de puntos en lugar de Path para que sea observable por Compose
 */
private data class DrawingPath(
    val points: List<Offset>,
    val color: Color
)
