package com.juegosparachicos.ui.screens.colors

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juegosparachicos.ui.theme.ShapeBlue
import com.juegosparachicos.ui.theme.ShapeGreen
import com.juegosparachicos.ui.theme.ShapeRed
import com.juegosparachicos.ui.theme.ShapeYellow
import com.juegosparachicos.utils.SoundManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.Canvas as ComposeCanvas

/**
 * Juego de Colores y Formas
 * - Instrucción: "Tocá el [forma] [color]"
 * - 3 opciones
 * - Sin perder, solo feedback positivo
 */
@Composable
fun ColorsScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val soundManager = remember { SoundManager(context) }

    DisposableEffect(Unit) {
        onDispose { soundManager.release() }
    }

    var currentRound by remember { mutableStateOf(generateRound()) }
    var isProcessing by remember { mutableStateOf(false) }
    var feedbackType by remember { mutableStateOf(FeedbackType.NONE) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                text = "Colores y Formas",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Instrucción
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = currentRound.instruction,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Opciones y feedback en Box para no mover el layout
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Opciones (3 formas)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                currentRound.options.forEach { option ->
                    ShapeOption(
                        shape = option.shape,
                        color = option.color,
                        onClick = {
                            if (!isProcessing) {
                                isProcessing = true
                                val isCorrect = option == currentRound.correctAnswer

                                if (isCorrect) {
                                    // ¡Correcto! Reproducir sonido y mostrar feedback
                                    soundManager.playSound(SoundManager.SoundType.SUCCESS)
                                    feedbackType = FeedbackType.SUCCESS

                                    // Avanzar de nivel después de un delay
                                    coroutineScope.launch {
                                        delay(1500)
                                        feedbackType = FeedbackType.NONE
                                        currentRound = generateRound()
                                        isProcessing = false
                                    }
                                } else {
                                    // Incorrecto - mostrar feedback sutil sin sonido ni avanzar
                                    feedbackType = FeedbackType.TRY_AGAIN

                                    // Ocultar feedback después de un delay corto
                                    coroutineScope.launch {
                                        delay(1000)
                                        feedbackType = FeedbackType.NONE
                                        isProcessing = false
                                    }
                                }
                            }
                        }
                    )
                }
            }

            // Feedback overlay (no mueve el layout)
            when (feedbackType) {
                FeedbackType.SUCCESS -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFFFF4E6).copy(alpha = 0.95f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier.padding(32.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.9f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(40.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🎉",
                                    style = MaterialTheme.typography.displayLarge,
                                    fontSize = 72.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "¡Muy bien!",
                                    style = MaterialTheme.typography.displaySmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
                FeedbackType.TRY_AGAIN -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text(
                            text = "Casi 😊",
                            modifier = Modifier
                                .background(
                                    Color(0xFFFFE0B2).copy(alpha = 0.75f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF6D4C41),
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                FeedbackType.NONE -> {
                    // Sin feedback
                }
            }
        }
    }
}

/**
 * Componente de opción (forma + color)
 */
@Composable
private fun ShapeOption(
    shape: Shape,
    color: Color,
    onClick: () -> Unit
) {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.05f,
            animationSpec = tween(500)
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(500)
        )
    }

    Box(
        modifier = Modifier
            .size(120.dp)
            .scale(scale.value)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        when (shape) {
            Shape.CIRCLE -> {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
            Shape.SQUARE -> {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(color)
                )
            }
            Shape.TRIANGLE -> {
                ComposeCanvas(modifier = Modifier.size(120.dp)) {
                    val path = Path().apply {
                        moveTo(size.width / 2f, 0f)
                        lineTo(size.width, size.height)
                        lineTo(0f, size.height)
                        close()
                    }
                    drawPath(path, color)
                }
            }
        }
    }
}

// ========== Data Classes y Helpers ==========

enum class FeedbackType {
    NONE,
    SUCCESS,
    TRY_AGAIN
}

enum class Shape(val displayName: String) {
    CIRCLE("círculo"),
    SQUARE("cuadrado"),
    TRIANGLE("triángulo")
}

enum class GameColor(val displayName: String, val color: Color) {
    RED("rojo", ShapeRed),
    BLUE("azul", ShapeBlue),
    YELLOW("amarillo", ShapeYellow),
    GREEN("verde", ShapeGreen)
}

data class ShapeColorOption(
    val shape: Shape,
    val color: Color,
    val colorName: String
)

data class GameRound(
    val instruction: String,
    val options: List<ShapeColorOption>,
    val correctAnswer: ShapeColorOption
)

/**
 * Genera una ronda aleatoria del juego
 */
private fun generateRound(): GameRound {
    val allShapes = Shape.values().toList()
    val allColors = GameColor.values().toList()

    // Seleccionar respuesta correcta
    val correctShape = allShapes.random()
    val correctColor = allColors.random()
    val correctOption = ShapeColorOption(
        shape = correctShape,
        color = correctColor.color,
        colorName = correctColor.displayName
    )

    // Generar 2 opciones incorrectas (diferentes en al menos una característica)
    val incorrectOptions = mutableListOf<ShapeColorOption>()

    while (incorrectOptions.size < 2) {
        val shape = allShapes.random()
        val color = allColors.random()

        // Evitar duplicados y la respuesta correcta
        val option = ShapeColorOption(
            shape = shape,
            color = color.color,
            colorName = color.displayName
        )

        val isDifferent = shape != correctShape || color != correctColor
        val isNotDuplicate = incorrectOptions.none {
            it.shape == shape && it.colorName == color.displayName
        }

        if (isDifferent && isNotDuplicate) {
            incorrectOptions.add(option)
        }
    }

    // Mezclar opciones
    val allOptions = (listOf(correctOption) + incorrectOptions).shuffled()

    // Crear instrucción
    val instruction = "Tocá el ${correctShape.displayName} ${correctColor.displayName}"

    return GameRound(
        instruction = instruction,
        options = allOptions,
        correctAnswer = correctOption
    )
}
