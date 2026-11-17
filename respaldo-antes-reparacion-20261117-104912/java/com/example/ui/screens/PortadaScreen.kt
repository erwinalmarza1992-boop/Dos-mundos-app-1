package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.VocabularyItem
import com.example.model.VocabularyRepository
import com.example.ui.theme.BilingoAmber
import com.example.ui.theme.BilingoGreen
import com.example.ui.theme.BilingoPrimary
import com.example.ui.theme.BilingoSecondary
import com.example.viewmodel.AppNavTab
import com.example.viewmodel.BilingoUiState
import com.example.viewmodel.BilingoViewModel

@Composable
fun PortadaScreen(
    viewModel: BilingoViewModel,
    uiState: BilingoUiState,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Gentle pulse animation for the main CTA button
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_cta")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    // Daily featured word (e.g. Ventana / Window or Silla / Chair)
    val dailyWord = remember {
        VocabularyRepository.items.firstOrNull { it.id == "home_window" }
            ?: VocabularyRepository.items.first()
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWide = maxWidth >= 700.dp

        if (isWide) {
            // === TABLET / HORIZONTAL LANDSCAPE LAYOUT (2 Columns) ===
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Left Pane: Cover Artwork & Slogan
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CoverArtworkCard(viewModel = viewModel)

                    Spacer(modifier = Modifier.height(14.dp))

                    SloganBadge()

                    Spacer(modifier = Modifier.height(14.dp))

                    FeaturedWordCard(
                        dailyWord = dailyWord,
                        onSpeakWord = { viewModel.speakItemBoth(dailyWord) }
                    )
                }

                // Right Pane: Action Button & Game Portals
                Column(
                    modifier = Modifier
                        .weight(1.1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Big Play Button
                    PlayCtaButton(
                        pulseScale = pulseScale,
                        onClick = { viewModel.selectTab(AppNavTab.EXPLORE) }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "🎮 Elige tu Modo de Juego:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Portals 2x2
                    PortalsGrid(viewModel = viewModel)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bilingual Pride Card
                    Surface(
                        color = Color(0xFFEFF6FF),
                        shape = RoundedCornerShape(18.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BilingoPrimary.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🌟", fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "En casa hablamos español con amor 🇲🇽, y en la escuela triunfamos en inglés 🇺🇸.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1E3A8A)
                            )
                        }
                    }
                }
            }
        } else {
            // === MOBILE PORTRAIT LAYOUT ===
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CoverArtworkCard(viewModel = viewModel)

                Spacer(modifier = Modifier.height(14.dp))

                SloganBadge()

                Spacer(modifier = Modifier.height(16.dp))

                PlayCtaButton(
                    pulseScale = pulseScale,
                    onClick = { viewModel.selectTab(AppNavTab.EXPLORE) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "🎮 Elige tu Modo de Juego:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                PortalsGrid(viewModel = viewModel)

                Spacer(modifier = Modifier.height(16.dp))

                FeaturedWordCard(
                    dailyWord = dailyWord,
                    onSpeakWord = { viewModel.speakItemBoth(dailyWord) }
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun CoverArtworkCard(viewModel: BilingoViewModel) {
    Card(
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("cover_artwork_card")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.4f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_cover_dos_mundos),
                contentDescription = "Two Worlds - Dos Mundos Portada Oficial",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.30f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.50f)
                            )
                        )
                    )
            )

            Surface(
                color = Color.Black.copy(alpha = 0.55f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = "🇺🇸 English • 🇲🇽 Español",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }

            Surface(
                color = BilingoAmber,
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 4.dp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { viewModel.speakWelcomeGreeting() }
                    .testTag("mascot_voice_greeting_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🦎", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "¡Escúchame! 🔊",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF78350F)
                    )
                }
            }
        }
    }
}

@Composable
private fun SloganBadge() {
    Surface(
        color = Color(0xFFF0FDF4),
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BilingoGreen.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Two Worlds • Dos Mundos",
                fontSize = 19.sp,
                fontWeight = FontWeight.Black,
                color = BilingoPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Learn Today for a Brighter Tomorrow",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF047857),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Aprende Hoy para un Mejor Mañana",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = BilingoSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PlayCtaButton(pulseScale: Float, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BilingoPrimary),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .scale(pulseScale)
            .testTag("portada_play_button")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Jugar",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "¡JUGAR Y APRENDER! ▶",
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }
    }
}

@Composable
private fun PortalsGrid(viewModel: BilingoViewModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            GamePortalCard(
                emoji = "📖",
                titleEs = "Vocabulario",
                titleEn = "Explore Words",
                subtitle = "Silla, Mesa, Ventana...",
                bgColor = Color(0xFFEEF2FF),
                borderColor = BilingoPrimary,
                onClick = { viewModel.selectTab(AppNavTab.EXPLORE) },
                modifier = Modifier.weight(1f).testTag("portal_explore")
            )

            GamePortalCard(
                emoji = "🎯",
                titleEs = "Adivina",
                titleEn = "Guess Quiz",
                subtitle = "Trivia con audio",
                bgColor = Color(0xFFFFFBEB),
                borderColor = BilingoAmber,
                onClick = { viewModel.selectTab(AppNavTab.QUIZ) },
                modifier = Modifier.weight(1f).testTag("portal_quiz")
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            GamePortalCard(
                emoji = "🧠",
                titleEs = "Memoria",
                titleEn = "Memory Match",
                subtitle = "Pares EN ↔ ES",
                bgColor = Color(0xFFECFDF5),
                borderColor = BilingoGreen,
                onClick = { viewModel.selectTab(AppNavTab.MEMORY) },
                modifier = Modifier.weight(1f).testTag("portal_memory")
            )

            GamePortalCard(
                emoji = "🔤",
                titleEs = "Deletreo",
                titleEn = "Word Builder",
                subtitle = "Arma las palabras",
                bgColor = Color(0xFFFDF2F8),
                borderColor = BilingoSecondary,
                onClick = { viewModel.selectTab(AppNavTab.BUILDER) },
                modifier = Modifier.weight(1f).testTag("portal_builder")
            )
        }
    }
}

@Composable
private fun FeaturedWordCard(dailyWord: VocabularyItem, onSpeakWord: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("featured_word_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "🌟 Palabra del Día / Word of the Day",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Surface(
                    color = dailyWord.category.badgeColorCompose,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = dailyWord.category.titleEs,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = dailyWord.category.textColorCompose,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(dailyWord.category.badgeColorCompose.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = dailyWord.emoji, fontSize = 32.sp)
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🇲🇽 ${dailyWord.spanish}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = BilingoSecondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "• 🇺🇸 ${dailyWord.english}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = BilingoPrimary
                        )
                    }
                    Text(
                        text = dailyWord.funTip,
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = BilingoPrimary,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .clickable { onSpeakWord() }
                        .testTag("featured_word_speak_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Escuchar palabra del día",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GamePortalCard(
    emoji: String,
    titleEs: String,
    titleEn: String,
    subtitle: String,
    bgColor: Color,
    borderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .border(1.dp, borderColor.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = emoji, fontSize = 28.sp)
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = borderColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = titleEs,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = titleEn,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = borderColor
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
