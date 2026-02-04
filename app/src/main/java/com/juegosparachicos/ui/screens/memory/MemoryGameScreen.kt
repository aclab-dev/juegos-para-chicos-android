package com.juegosparachicos.ui.screens.memory

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.juegosparachicos.ads.AdBanner
import com.juegosparachicos.utils.SoundManager
import kotlinx.coroutines.delay

/**
 * Juego de Memoria
 * - Tres niveles: Fácil (2x2), Medio (3x2) y Difícil (4x3)
 * - Encuentra las parejas de animales
 * - Sin tiempo límite, sin perder
 */
@Composable
fun MemoryGameScreen(
    onNavigateBack: () -> Unit
) {
    // Inicializar SoundManager
    val context = LocalContext.current
    val soundManager = remember { SoundManager(context) }

    // Liberar recursos cuando se desmonte la pantalla
    DisposableEffect(Unit) {
        onDispose { soundManager.release() }
    }

    var selectedLevel by remember { mutableStateOf<MemoryLevel?>(null) }

    if (selectedLevel == null) {
        // Selector de nivel
        LevelSelector(
            soundManager = soundManager,
            onSelectLevel = { selectedLevel = it },
            onNavigateBack = onNavigateBack
        )
    } else {
        // Juego activo
        MemoryGame(
            soundManager = soundManager,
            level = selectedLevel!!,
            onBackToLevelSelector = { selectedLevel = null },
            onNavigateBack = onNavigateBack
        )
    }
}

/**
 * Selector de nivel (Fácil, Medio o Difícil)
 */
@Composable
private fun LevelSelector(
    soundManager: SoundManager,
    onSelectLevel: (MemoryLevel) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Botón Home
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = {
                soundManager.playSound(SoundManager.SoundType.TAP)
                onNavigateBack()
            }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Volver al inicio",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Memoria",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(64.dp))

        // Botón Fácil
        Button(
            onClick = {
                soundManager.playSound(SoundManager.SoundType.TAP)
                onSelectLevel(MemoryLevel.EASY)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF95E1D3)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Fácil (2x2)",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Medio
        Button(
            onClick = {
                soundManager.playSound(SoundManager.SoundType.TAP)
                onSelectLevel(MemoryLevel.MEDIUM)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFA94D)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Medio (3x2)",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Difícil
        Button(
            onClick = {
                soundManager.playSound(SoundManager.SoundType.TAP)
                onSelectLevel(MemoryLevel.HARD)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6B9D)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Difícil (4x3)",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

/**
 * Componente principal del juego
 */
@Composable
private fun MemoryGame(
    soundManager: SoundManager,
    level: MemoryLevel,
    onBackToLevelSelector: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val cards = remember(level) {
        mutableStateListOf<MemoryCard>().apply {
            addAll(createMemoryCards(level))
        }
    }

    var firstSelectedIndex by remember { mutableStateOf<Int?>(null) }
    var secondSelectedIndex by remember { mutableStateOf<Int?>(null) }
    var isProcessing by remember { mutableStateOf(false) }
    var showCompletionDialog by remember { mutableStateOf(false) }

    // Verificar si el juego está completo
    LaunchedEffect(cards.toList()) {
        if (cards.all { it.isMatched }) {
            delay(500)
            soundManager.playSound(SoundManager.SoundType.END)
            showCompletionDialog = true
        }
    }

    // Manejar lógica de emparejamiento
    LaunchedEffect(firstSelectedIndex, secondSelectedIndex) {
        if (firstSelectedIndex != null && secondSelectedIndex != null && !isProcessing) {
            isProcessing = true
            delay(800)

            val first = cards[firstSelectedIndex!!]
            val second = cards[secondSelectedIndex!!]

            if (first.animalId == second.animalId) {
                // ¡Pareja encontrada!
                soundManager.playSound(SoundManager.SoundType.SUCCESS)
                cards[firstSelectedIndex!!] = first.copy(isMatched = true)
                cards[secondSelectedIndex!!] = second.copy(isMatched = true)
            } else {
                // No coinciden, voltear de nuevo
                cards[firstSelectedIndex!!] = first.copy(isFlipped = false)
                cards[secondSelectedIndex!!] = second.copy(isFlipped = false)
            }

            firstSelectedIndex = null
            secondSelectedIndex = null
            isProcessing = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header con botón home
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
                text = when (level) {
                    MemoryLevel.EASY -> "Fácil"
                    MemoryLevel.MEDIUM -> "Medio"
                    MemoryLevel.HARD -> "Difícil"
                },
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        // Grid de cartas
        LazyVerticalGrid(
            columns = GridCells.Fixed(
                when (level) {
                    MemoryLevel.EASY -> 2
                    MemoryLevel.MEDIUM -> 3
                    MemoryLevel.HARD -> 4
                }
            ),
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cards) { card ->
                val index = cards.indexOf(card)
                MemoryCardItem(
                    card = card,
                    onClick = {
                        if (!isProcessing && !card.isFlipped && !card.isMatched) {
                            soundManager.playSound(SoundManager.SoundType.TAP)
                            cards[index] = card.copy(isFlipped = true)

                            when {
                                firstSelectedIndex == null -> firstSelectedIndex = index
                                secondSelectedIndex == null && firstSelectedIndex != index -> {
                                    secondSelectedIndex = index
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    // Diálogo de completado
    if (showCompletionDialog) {
        CompletionDialog(
            soundManager = soundManager,
            onPlayAgain = {
                showCompletionDialog = false
                cards.clear()
                cards.addAll(createMemoryCards(level))
            },
            onChangeDifficulty = {
                showCompletionDialog = false
                onBackToLevelSelector()
            },
            onHome = {
                showCompletionDialog = false
                onNavigateBack()
            }
        )
    }
}

/**
 * Componente de carta individual
 */
@Composable
private fun MemoryCardItem(
    card: MemoryCard,
    onClick: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (card.isFlipped || card.isMatched) 180f else 0f,
        animationSpec = tween(400),
        label = "card_rotation"
    )

    val scale by animateFloatAsState(
        targetValue = if (card.isMatched) 0.9f else 1f,
        animationSpec = tween(300),
        label = "card_scale"
    )

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .scale(scale)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable(enabled = !card.isFlipped && !card.isMatched) { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (card.isMatched) Color(0xFFFFD93D) else Color(0xFFFF6B9D)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (rotation > 90f) {
                // Cara frontal (animal)
                Text(
                    text = card.animalEmoji,
                    fontSize = 48.sp,
                    modifier = Modifier.graphicsLayer { rotationY = 180f }
                )
            } else {
                // Cara trasera
                Text(
                    text = "?",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * Diálogo de juego completado
 */
@Composable
private fun CompletionDialog(
    soundManager: SoundManager,
    onPlayAgain: () -> Unit,
    onChangeDifficulty: () -> Unit,
    onHome: () -> Unit
) {
    Dialog(onDismissRequest = {}) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¡Muy bien!",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "🎉",
                    fontSize = 72.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Banner publicitario
                AdBanner()

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        soundManager.playSound(SoundManager.SoundType.TAP)
                        onPlayAgain()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF95E1D3)
                    )
                ) {
                    Text("Jugar otra vez", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        soundManager.playSound(SoundManager.SoundType.TAP)
                        onChangeDifficulty()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFA94D)
                    )
                ) {
                    Text("Cambiar dificultad", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        soundManager.playSound(SoundManager.SoundType.TAP)
                        onHome()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Inicio", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ========== Data Classes y Helpers ==========

enum class MemoryLevel {
    EASY,   // 2x2 = 4 cartas
    MEDIUM, // 3x2 = 6 cartas
    HARD    // 4x3 = 12 cartas
}

data class MemoryCard(
    val id: Int,
    val animalId: Int,
    val animalEmoji: String,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false
)

private fun createMemoryCards(level: MemoryLevel): List<MemoryCard> {
    val animals = listOf(
        "🐶", "🐱", "🐭", "🐹", "🐰", "🦊",
        "🐻", "🐼", "🐨", "🐯", "🦁", "🐮"
    )

    val pairsCount = when (level) {
        MemoryLevel.EASY -> 2    // 2 parejas = 4 cartas
        MemoryLevel.MEDIUM -> 3  // 3 parejas = 6 cartas
        MemoryLevel.HARD -> 6    // 6 parejas = 12 cartas
    }

    val selectedAnimals = animals.shuffled().take(pairsCount)

    val cards = selectedAnimals.flatMapIndexed { index, emoji ->
        listOf(
            MemoryCard(id = index * 2, animalId = index, animalEmoji = emoji),
            MemoryCard(id = index * 2 + 1, animalId = index, animalEmoji = emoji)
        )
    }

    return cards.shuffled()
}
