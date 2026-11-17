package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.LetterTile
import com.example.model.VocabularyItem
import com.example.ui.theme.BilingoAmber
import com.example.ui.theme.BilingoGreen
import com.example.ui.theme.BilingoPrimary
import com.example.ui.theme.BilingoSecondary
import com.example.viewmodel.BilingoUiState
import com.example.viewmodel.BilingoViewModel

@Composable
fun WordBuilderScreen(
    viewModel: BilingoViewModel,
    uiState: BilingoUiState,
    modifier: Modifier = Modifier
) {
    val targetItem = uiState.builderTargetItem
    val isEnglish = uiState.builderLanguageIsEnglish
    val targetWord = if (isEnglish) targetItem?.english?.uppercase() ?: "" else targetItem?.spanish?.uppercase() ?: ""

    if (targetItem == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Cargando...")
        }
        return
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWide = maxWidth >= 700.dp

        if (isWide) {
            // === TABLET & LANDSCAPE 2-COLUMN LAYOUT ===
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Left Column: Language selector and Word Card
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LanguageSwitcher(
                        isEnglish = isEnglish,
                        targetItem = targetItem,
                        onSelectLang = { viewModel.startNewWordBuilder(targetItem, languageEn = it) }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    IllustrationCard(
                        targetItem = targetItem,
                        isEnglish = isEnglish,
                        onSpeakWord = {
                            if (isEnglish) viewModel.tts.speakEnglish(targetItem.english)
                            else viewModel.tts.speakSpanish(targetItem.spanish)
                        }
                    )
                }

                // Right Column: Word Slots, Letters, Controls
                Column(
                    modifier = Modifier
                        .weight(1.2f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TargetWordSlots(
                        targetWord = targetWord,
                        uiState = uiState
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    HintAndBackspaceRow(
                        uiState = uiState,
                        targetWordLength = targetWord.length,
                        onHint = { viewModel.provideBuilderHint() },
                        onBackspace = { viewModel.removeLastSpelledLetter() }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Toca las letras para armar la palabra:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LetterTilesGrid(
                        uiState = uiState,
                        onLetterClicked = { viewModel.onBuilderLetterClicked(it) }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    SuccessCelebration(
                        uiState = uiState,
                        isEnglish = isEnglish,
                        onNextWord = { viewModel.startNewWordBuilder(null, isEnglish) }
                    )
                }
            }
        } else {
            // === MOBILE PORTRAIT LAYOUT ===
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LanguageSwitcher(
                    isEnglish = isEnglish,
                    targetItem = targetItem,
                    onSelectLang = { viewModel.startNewWordBuilder(targetItem, languageEn = it) }
                )

                IllustrationCard(
                    targetItem = targetItem,
                    isEnglish = isEnglish,
                    onSpeakWord = {
                        if (isEnglish) viewModel.tts.speakEnglish(targetItem.english)
                        else viewModel.tts.speakSpanish(targetItem.spanish)
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                TargetWordSlots(
                    targetWord = targetWord,
                    uiState = uiState
                )

                Spacer(modifier = Modifier.height(16.dp))

                HintAndBackspaceRow(
                    uiState = uiState,
                    targetWordLength = targetWord.length,
                    onHint = { viewModel.provideBuilderHint() },
                    onBackspace = { viewModel.removeLastSpelledLetter() }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Toca las letras para armar la palabra:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                LetterTilesGrid(
                    uiState = uiState,
                    onLetterClicked = { viewModel.onBuilderLetterClicked(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                SuccessCelebration(
                    uiState = uiState,
                    isEnglish = isEnglish,
                    onNextWord = { viewModel.startNewWordBuilder(null, isEnglish) }
                )
            }
        }
    }
}

@Composable
private fun LanguageSwitcher(
    isEnglish: Boolean,
    targetItem: VocabularyItem,
    onSelectLang: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.padding(4.dp)
        ) {
            Row(modifier = Modifier.padding(4.dp)) {
                Surface(
                    color = if (isEnglish) BilingoPrimary else Color.Transparent,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onSelectLang(true) }
                        .testTag("builder_lang_en")
                ) {
                    Text(
                        text = "🇺🇸 Spell in English",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isEnglish) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }

                Surface(
                    color = if (!isEnglish) BilingoSecondary else Color.Transparent,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onSelectLang(false) }
                        .testTag("builder_lang_es")
                ) {
                    Text(
                        text = "🇲🇽 Deletrear en Español",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (!isEnglish) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun IllustrationCard(
    targetItem: VocabularyItem,
    isEnglish: Boolean,
    onSpeakWord: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(targetItem.category.badgeColorCompose.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = targetItem.emoji, fontSize = 52.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isEnglish) targetItem.spanish else targetItem.english,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onSpeakWord,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .testTag("builder_listen_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Escuchar palabra",
                        tint = BilingoPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = if (isEnglish) "Forma la palabra en inglés:" else "Forma la palabra en español:",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = BilingoPrimary
            )
        }
    }
}

@Composable
private fun TargetWordSlots(targetWord: String, uiState: BilingoUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        targetWord.forEachIndexed { index, _ ->
            val filledLetter = uiState.builderSpelledLetters.getOrNull(index)

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .padding(3.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        when {
                            uiState.isBuilderWordCorrect -> Color(0xFFD1FAE5)
                            filledLetter != null -> Color(0xFFEFF6FF)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                    .border(
                        width = 2.dp,
                        color = when {
                            uiState.isBuilderWordCorrect -> BilingoGreen
                            filledLetter != null -> BilingoPrimary
                            else -> MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = filledLetter?.char?.toString() ?: "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = if (uiState.isBuilderWordCorrect) Color(0xFF065F46) else BilingoPrimary
                )
            }
        }
    }
}

@Composable
private fun HintAndBackspaceRow(
    uiState: BilingoUiState,
    targetWordLength: Int,
    onHint: () -> Unit,
    onBackspace: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onHint,
            colors = ButtonDefaults.buttonColors(containerColor = BilingoAmber.copy(alpha = 0.2f)),
            shape = RoundedCornerShape(14.dp),
            enabled = !uiState.isBuilderWordCorrect && uiState.builderSpelledLetters.size < targetWordLength,
            modifier = Modifier.testTag("builder_hint_button")
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = "Pista",
                tint = Color(0xFFB45309),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Pista / Hint",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB45309)
            )
        }

        Button(
            onClick = onBackspace,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(14.dp),
            enabled = uiState.builderSpelledLetters.isNotEmpty() && !uiState.isBuilderWordCorrect,
            modifier = Modifier.testTag("builder_backspace_button")
        ) {
            Icon(
                imageVector = Icons.Default.Backspace,
                contentDescription = "Borrar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Borrar",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LetterTilesGrid(
    uiState: BilingoUiState,
    onLetterClicked: (com.example.viewmodel.LetterTile) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val chunks = uiState.builderAvailableLetters.chunked(5)
            chunks.forEach { rowTiles ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    rowTiles.forEach { tile ->
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (tile.isUsed) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    else BilingoPrimary
                                )
                                .clickable(enabled = !tile.isUsed && !uiState.isBuilderWordCorrect) {
                                    onLetterClicked(tile)
                                }
                                .testTag("letter_tile_${tile.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tile.char.toString(),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = if (tile.isUsed) Color.LightGray else Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SuccessCelebration(
    uiState: BilingoUiState,
    isEnglish: Boolean,
    onNextWord: () -> Unit
) {
    AnimatedVisibility(
        visible = uiState.isBuilderWordCorrect,
        enter = fadeIn() + scaleIn()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = Color(0xFFD1FAE5),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🎉 ¡Magnífico! ¡Deletreado correctamente! 🎉", fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color(0xFF047857))
                    Text(text = "¡Ganaste +5 estrellas! ⭐", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF065F46))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onNextWord,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BilingoGreen),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("builder_next_word_button")
            ) {
                Text(text = "Siguiente Palabra ▶", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Siguiente")
            }
        }
    }
}
