package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BilingoAmber
import com.example.ui.theme.BilingoGreen
import com.example.ui.theme.BilingoPrimary
import com.example.ui.theme.BilingoSecondary
import com.example.viewmodel.BilingoUiState
import com.example.viewmodel.BilingoViewModel
import com.example.viewmodel.MemoryCard

@Composable
fun MemoryGameScreen(
    viewModel: BilingoViewModel,
    uiState: BilingoUiState,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val columnsCount = if (maxWidth >= 650.dp) 4 else 3

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        // Status Bar: Moves, Matches & Reset Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = BilingoPrimary.copy(alpha = 0.15f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "🎯 Pares: ${uiState.memoryMatchesCount} / 6",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = BilingoPrimary,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Movimientos: ${uiState.memoryMovesCount}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { viewModel.startNewMemoryGame() }
                    .testTag("memory_restart_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reiniciar juego",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Nuevo", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Instructional banner for kids
        Surface(
            color = Color(0xFFF3E8FF),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "💡", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Empareja la tarjeta en English 🇺🇸 con su igual en Español 🇲🇽",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6B21A8)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Win Banner
        AnimatedVisibility(visible = uiState.isMemoryGameWon) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .testTag("memory_win_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🎉 ¡Felicidades! You did it! 🎉", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF047857))
                    Text(text = "¡Ganaste +10 estrellas bilingües! ⭐", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF065F46))
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { viewModel.startNewMemoryGame() },
                        colors = ButtonDefaults.buttonColors(containerColor = BilingoGreen),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(text = "Jugar otra vez 🔄", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Memory Grid (12 cards)
        LazyVerticalGrid(
            columns = GridCells.Fixed(columnsCount),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(uiState.memoryCards, key = { _, card -> card.id }) { index, card ->
                MemoryCardView(
                    card = card,
                    index = index,
                    onClick = { viewModel.onMemoryCardClicked(index) }
                )
            }
        }
    }
    }
}

@Composable
fun MemoryCardView(
    card: MemoryCard,
    index: Int,
    onClick: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (card.isFlipped || card.isMatched) 180f else 0f,
        animationSpec = tween(durationMillis = 350),
        label = "card_flip"
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                card.isMatched -> Color(0xFFD1FAE5)
                card.isFlipped -> Color(0xFFEFF6FF)
                else -> BilingoPrimary
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = !card.isFlipped && !card.isMatched) { onClick() }
            .testTag("memory_card_$index")
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            if (rotation <= 90f) {
                // Card Back (hidden state)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "⭐", fontSize = 28.sp)
                    Text(
                        text = "Bilingo",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            } else {
                // Card Face (flipped state)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.graphicsLayer { rotationY = 180f }
                ) {
                    // Flag Pill
                    Surface(
                        color = if (card.language == "EN") Color(0xFFDBEAFE) else Color(0xFFFFE4E6),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (card.language == "EN") "🇺🇸 EN" else "🇲🇽 ES",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (card.language == "EN") BilingoPrimary else BilingoSecondary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = card.emoji, fontSize = 28.sp)

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = card.displayText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = if (card.isMatched) Color(0xFF065F46) else Color(0xFF1E293B),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (card.isMatched) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Matched",
                            tint = BilingoGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
