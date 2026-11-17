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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VocabularyItem
import com.example.ui.theme.BilingoAmber
import com.example.ui.theme.BilingoGreen
import com.example.ui.theme.BilingoPrimary
import com.example.ui.theme.BilingoSecondary
import com.example.viewmodel.BilingoUiState
import com.example.viewmodel.BilingoViewModel
import com.example.viewmodel.QuizMode
import com.example.viewmodel.QuizQuestion

@Composable
fun QuizScreen(
    viewModel: BilingoViewModel,
    uiState: BilingoUiState,
    modifier: Modifier = Modifier
) {
    val question = uiState.currentQuizQuestion

    if (question == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Cargando pregunta...")
        }
        return
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWide = maxWidth >= 650.dp

        if (isWide) {
            // === TABLET & HORIZONTAL LANDSCAPE LAYOUT ===
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Left Column: Question Card & Score/Streak
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ScoreStreakHeader(uiState = uiState)

                    Spacer(modifier = Modifier.height(16.dp))

                    QuestionCard(
                        question = question,
                        onRepeatAudio = {
                            when (question.mode) {
                                QuizMode.PICTURE_TO_WORD -> viewModel.tts.speakSpanish("Encuentra: ${question.item.spanish}")
                                QuizMode.SPANISH_TO_ENGLISH -> viewModel.tts.speakSpanish(question.item.spanish)
                                QuizMode.ENGLISH_TO_SPANISH -> viewModel.tts.speakEnglish(question.item.english)
                            }
                        }
                    )
                }

                // Right Column: Options & Feedback
                Column(
                    modifier = Modifier
                        .weight(1.2f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OptionsList(
                        question = question,
                        uiState = uiState,
                        onOptionSelected = { viewModel.submitQuizAnswer(it) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FeedbackAndNext(
                        question = question,
                        uiState = uiState,
                        onNextQuestion = { viewModel.startNewQuizQuestion() }
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
                ScoreStreakHeader(uiState = uiState)

                Spacer(modifier = Modifier.height(16.dp))

                QuestionCard(
                    question = question,
                    onRepeatAudio = {
                        when (question.mode) {
                            QuizMode.PICTURE_TO_WORD -> viewModel.tts.speakSpanish("Encuentra: ${question.item.spanish}")
                            QuizMode.SPANISH_TO_ENGLISH -> viewModel.tts.speakSpanish(question.item.spanish)
                            QuizMode.ENGLISH_TO_SPANISH -> viewModel.tts.speakEnglish(question.item.english)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(18.dp))

                OptionsList(
                    question = question,
                    uiState = uiState,
                    onOptionSelected = { viewModel.submitQuizAnswer(it) }
                )

                Spacer(modifier = Modifier.height(18.dp))

                FeedbackAndNext(
                    question = question,
                    uiState = uiState,
                    onNextQuestion = { viewModel.startNewQuizQuestion() }
                )
            }
        }
    }
}

@Composable
private fun ScoreStreakHeader(uiState: BilingoUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = BilingoAmber.copy(alpha = 0.2f),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.testTag("quiz_score_chip")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "⭐ Puntos: ", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                Text(text = "${uiState.quizScore}", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFB45309))
            }
        }

        Surface(
            color = if (uiState.quizStreak > 1) Color(0xFFFFECE0) else MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.testTag("quiz_streak_chip")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = if (uiState.quizStreak > 1) "🔥 Racha: " else "🎯 Racha: ", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "${uiState.quizStreak}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (uiState.quizStreak > 1) Color(0xFFE65100) else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun QuestionCard(question: QuizQuestion, onRepeatAudio: () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("quiz_question_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = question.item.category.badgeColorCompose,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "${question.item.category.iconEmoji} ${question.item.category.titleEs} • ${question.item.category.titleEn}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = question.item.category.textColorCompose,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = question.promptQuestionEs,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = question.promptQuestionEn,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = BilingoPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            IconButton(
                onClick = onRepeatAudio,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(BilingoPrimary.copy(alpha = 0.15f))
                    .testTag("quiz_repeat_audio_button")
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Repetir audio de pregunta",
                    tint = BilingoPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun OptionsList(
    question: QuizQuestion,
    uiState: BilingoUiState,
    onOptionSelected: (VocabularyItem) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        question.options.forEachIndexed { index, option ->
            val isSelected = uiState.quizSelectedOptionId == option.id
            val isCorrectTarget = option.id == question.item.id

            val cardBorderColor = when {
                uiState.isQuizAnswerChecked && isCorrectTarget -> BilingoGreen
                uiState.isQuizAnswerChecked && isSelected && !isCorrectTarget -> Color(0xFFEF4444)
                isSelected -> BilingoPrimary
                else -> Color.Transparent
            }

            val cardBgColor = when {
                uiState.isQuizAnswerChecked && isCorrectTarget -> Color(0xFFE8FDF5)
                uiState.isQuizAnswerChecked && isSelected && !isCorrectTarget -> Color(0xFFFEE2E2)
                isSelected -> Color(0xFFEEF2FF)
                else -> MaterialTheme.colorScheme.surface
            }

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBgColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (uiState.isQuizAnswerChecked && (isCorrectTarget || isSelected)) 3.dp else 1.dp,
                        color = if (uiState.isQuizAnswerChecked && (isCorrectTarget || isSelected)) cardBorderColor else MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .clickable(enabled = !uiState.isQuizAnswerChecked) {
                        onOptionSelected(option)
                    }
                    .testTag("quiz_option_${index}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(option.category.badgeColorCompose.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = option.emoji, fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = option.spanish,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = option.english,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BilingoPrimary
                        )
                    }

                    if (uiState.isQuizAnswerChecked) {
                        if (isCorrectTarget) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Correcto",
                                tint = BilingoGreen,
                                modifier = Modifier.size(28.dp)
                            )
                        } else if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Incorrecto",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeedbackAndNext(
    question: QuizQuestion,
    uiState: BilingoUiState,
    onNextQuestion: () -> Unit
) {
    AnimatedVisibility(
        visible = uiState.isQuizAnswerChecked,
        enter = fadeIn() + scaleIn()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isQuizAnswerCorrect) {
                Surface(
                    color = Color(0xFFD1FAE5),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "🎉", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "¡Excelente trabajo! Ganaste +2 estrellas ⭐",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF065F46)
                        )
                    }
                }
            } else {
                Surface(
                    color = Color(0xFFFEE2E2),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "💪", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "¡Casi! Era '${question.item.spanish}' / '${question.item.english}'",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF991B1B)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onNextQuestion,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BilingoPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("quiz_next_button")
            ) {
                Text(
                    text = "Siguiente Pregunta ▶",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Siguiente",
                    tint = Color.White
                )
            }
        }
    }
}
