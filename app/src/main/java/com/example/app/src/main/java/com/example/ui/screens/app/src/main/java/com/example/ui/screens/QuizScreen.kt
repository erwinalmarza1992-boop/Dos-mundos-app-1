package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.VocabularyItem
import com.example.model.VocabularyRepository
import com.example.model.WordCategory
import com.example.ui.components.CategoryChipsRow
import com.example.ui.components.FlashcardDetailDialog
import com.example.ui.components.VocabularyGridCard
import com.example.ui.theme.BilingoGreen
import com.example.ui.theme.BilingoPrimary
import com.example.viewmodel.BilingoUiState
import com.example.viewmodel.BilingoViewModel

@Composable
fun ExploreScreen(
    viewModel: BilingoViewModel,
    uiState: BilingoUiState,
    modifier: Modifier = Modifier
) {
    var dialogItem by remember { mutableStateOf<VocabularyItem?>(null) }

    // Filter items
    val filteredItems = remember(uiState.selectedCategory, uiState.searchQuery) {
        VocabularyRepository.items.filter { item ->
            val matchesCategory = uiState.selectedCategory == null || item.category == uiState.selectedCategory
            val matchesSearch = uiState.searchQuery.isBlank() ||
                item.english.contains(uiState.searchQuery, ignoreCase = true) ||
                item.spanish.contains(uiState.searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    val totalItemsCount = VocabularyRepository.items.size
    val listenedCount = uiState.listenedWordIds.size
    val progressFraction = (listenedCount.toFloat() / totalItemsCount.toFloat()).coerceIn(0f, 1f)

    Box(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Hero Banner
            item(span = { GridItemSpan(maxLineSpan) }) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("hero_banner_card")
                ) {
                    Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.img_cover_dos_mundos),
                            contentDescription = "Two Worlds Dos Mundos Banner",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        // Gradient overlay for crisp readability
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.65f)
                                        )
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Two Worlds • Dos Mundos",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = "Learn Today for a Brighter Tomorrow • Aprende Hoy",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFFFD166)
                            )
                        }
                    }
                }
            }

            // Progress bar card
            item(span = { GridItemSpan(maxLineSpan) }) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🌟 Tu Progreso Bilingüe:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$listenedCount / $totalItemsCount palabras",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = BilingoPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { progressFraction },
                            color = BilingoGreen,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                    }
                }
            }

            // Search Bar
            item(span = { GridItemSpan(maxLineSpan) }) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = {
                        Text(
                            text = "Buscar: ventana, chair, manzana, dog...",
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar palabra",
                            tint = BilingoPrimary
                        )
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpiar búsqueda"
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = BilingoPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("vocabulary_search_input")
                )
            }

            // Categories horizontal scroller
            item(span = { GridItemSpan(maxLineSpan) }) {
                CategoryChipsRow(
                    selectedCategory = uiState.selectedCategory,
                    onSelectCategory = { viewModel.setCategoryFilter(it) }
                )
            }

            // Section Title
            item(span = { GridItemSpan(maxLineSpan) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (uiState.selectedCategory == null) "Vocabulario Completo (${filteredItems.size})"
                        else "${uiState.selectedCategory!!.titleEs} (${filteredItems.size})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Toca para detalles ✨",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Grid Items
            if (filteredItems.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🔍", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No encontramos esa palabra.",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Prueba con otra palabra en español o inglés.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredItems, key = { it.id }) { item ->
                    val isFav = uiState.favoriteWordIds.contains(item.id)
                    val isListened = uiState.listenedWordIds.contains(item.id)
                    VocabularyGridCard(
                        item = item,
                        isFavorite = isFav,
                        isListened = isListened,
                        onClick = {
                            dialogItem = item
                            viewModel.selectFlashcard(item)
                        },
                        onQuickSpeak = {
                            viewModel.speakItemBoth(item)
                        },
                        onToggleFavorite = {
                            viewModel.toggleFavorite(item.id)
                        }
                    )
                }
            }
        }

        // Active Dialog
        dialogItem?.let { currentItem ->
            val currentIndex = filteredItems.indexOfFirst { it.id == currentItem.id }
            val hasNext = currentIndex in 0 until filteredItems.lastIndex
            val hasPrev = currentIndex > 0

            FlashcardDetailDialog(
                item = currentItem,
                isFavorite = uiState.favoriteWordIds.contains(currentItem.id),
                onDismiss = { dialogItem = null },
                onToggleFavorite = { viewModel.toggleFavorite(currentItem.id) },
                onSpeakEnglish = { viewModel.speakItemEnglish(currentItem) },
                onSpeakSpanish = { viewModel.speakItemSpanish(currentItem) },
                onSpeakBoth = { viewModel.speakItemBoth(currentItem) },
                onSpeakSentenceEn = { viewModel.speakSentenceEnglish(currentItem) },
                onSpeakSentenceEs = { viewModel.speakSentenceSpanish(currentItem) },
                onNext = if (hasNext) {
                    { dialogItem = filteredItems[currentIndex + 1] }
                } else null,
                onPrevious = if (hasPrev) {
                    { dialogItem = filteredItems[currentIndex - 1] }
                } else null
            )
        }
    }
}
