package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TopBilingualHeader
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.MemoryGameScreen
import com.example.ui.screens.PortadaScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.StarsAndParentsScreen
import com.example.ui.screens.WordBuilderScreen
import com.example.ui.theme.BilingoAmber
import com.example.ui.theme.BilingoGreen
import com.example.ui.theme.BilingoPrimary
import com.example.ui.theme.BilingoPrimaryContainer
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppNavTab
import com.example.viewmodel.BilingoUiState
import com.example.viewmodel.BilingoViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: BilingoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                BilingoApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BilingoApp(viewModel: BilingoViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // BackHandler: Navigate back to Portada tab when pressed from other tabs
    BackHandler(enabled = uiState.currentTab != AppNavTab.PORTADA) {
        viewModel.selectTab(AppNavTab.PORTADA)
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTabletOrLandscape = maxWidth >= 600.dp

        if (isTabletOrLandscape) {
            // === TABLET & LANDSCAPE LAYOUT (NavigationRail) ===
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                NavigationRail(
                    containerColor = MaterialTheme.colorScheme.surface,
                    header = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .clickable { viewModel.selectTab(AppNavTab.PORTADA) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFFE0F2FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🦎", fontSize = 26.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Dos Mundos",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = BilingoPrimary
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .testTag("tablet_navigation_rail")
                ) {
                    val railScrollState = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .verticalScroll(railScrollState),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Portada
                            NavigationRailItem(
                                selected = uiState.currentTab == AppNavTab.PORTADA,
                                onClick = { viewModel.selectTab(AppNavTab.PORTADA) },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Portada") },
                                label = { Text("Portada", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = BilingoPrimary,
                                    selectedTextColor = BilingoPrimary,
                                    indicatorColor = BilingoPrimaryContainer
                                ),
                                modifier = Modifier.testTag("rail_item_portada")
                            )

                            // Explorar
                            NavigationRailItem(
                                selected = uiState.currentTab == AppNavTab.EXPLORE,
                                onClick = { viewModel.selectTab(AppNavTab.EXPLORE) },
                                icon = { Icon(Icons.Default.School, contentDescription = "Explorar") },
                                label = { Text("Explorar", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = BilingoPrimary,
                                    selectedTextColor = BilingoPrimary,
                                    indicatorColor = BilingoPrimaryContainer
                                ),
                                modifier = Modifier.testTag("rail_item_explore")
                            )

                            // Adivina
                            NavigationRailItem(
                                selected = uiState.currentTab == AppNavTab.QUIZ,
                                onClick = { viewModel.selectTab(AppNavTab.QUIZ) },
                                icon = { Icon(Icons.Default.HelpOutline, contentDescription = "Adivina") },
                                label = { Text("Adivina", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = BilingoPrimary,
                                    selectedTextColor = BilingoPrimary,
                                    indicatorColor = BilingoPrimaryContainer
                                ),
                                modifier = Modifier.testTag("rail_item_quiz")
                            )

                            // Memoria
                            NavigationRailItem(
                                selected = uiState.currentTab == AppNavTab.MEMORY,
                                onClick = { viewModel.selectTab(AppNavTab.MEMORY) },
                                icon = { Icon(Icons.Default.Dashboard, contentDescription = "Memoria") },
                                label = { Text("Memoria", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = BilingoPrimary,
                                    selectedTextColor = BilingoPrimary,
                                    indicatorColor = BilingoPrimaryContainer
                                ),
                                modifier = Modifier.testTag("rail_item_memory")
                            )

                            // Deletreo
                            NavigationRailItem(
                                selected = uiState.currentTab == AppNavTab.BUILDER,
                                onClick = { viewModel.selectTab(AppNavTab.BUILDER) },
                                icon = { Icon(Icons.Default.Spellcheck, contentDescription = "Deletreo") },
                                label = { Text("Deletreo", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = BilingoPrimary,
                                    selectedTextColor = BilingoPrimary,
                                    indicatorColor = BilingoPrimaryContainer
                                ),
                                modifier = Modifier.testTag("rail_item_builder")
                            )

                            // Premios
                            NavigationRailItem(
                                selected = uiState.currentTab == AppNavTab.STARS,
                                onClick = { viewModel.selectTab(AppNavTab.STARS) },
                                icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Premios") },
                                label = { Text("Premios", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = BilingoPrimary,
                                    selectedTextColor = BilingoPrimary,
                                    indicatorColor = BilingoPrimaryContainer
                                ),
                                modifier = Modifier.testTag("rail_item_stars")
                            )
                        }

                        // Bottom Controls on Rail: Stars, Turtle mode, Sound
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            // Stars Chip
                            Surface(
                                color = BilingoAmber.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Star, contentDescription = "Estrellas", tint = Color(0xFFF59E0B), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(text = "${uiState.totalStars}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Turtle Mode
                            Surface(
                                color = if (uiState.isTurtleSlowMode) BilingoGreen.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .clickable { viewModel.toggleTurtleSlowMode() }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = "🐢", fontSize = 16.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Sound Toggle
                            IconButton(
                                onClick = { viewModel.toggleSound() },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Icon(
                                    imageVector = if (uiState.isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                                    contentDescription = "Audio",
                                    tint = if (uiState.isSoundEnabled) BilingoPrimary else Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // Main Content for Tablet / Landscape
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    CurrentTabContent(viewModel = viewModel, uiState = uiState)
                }
            }
        } else {
            // === MOBILE PORTRAIT LAYOUT (Bottom Navigation) ===
            Scaffold(
                contentWindowInsets = WindowInsets.safeDrawing,
                topBar = {
                    TopBilingualHeader(
                        totalStars = uiState.totalStars,
                        isTurtleSlowMode = uiState.isTurtleSlowMode,
                        isSoundEnabled = uiState.isSoundEnabled,
                        onToggleTurtleMode = { viewModel.toggleTurtleSlowMode() },
                        onToggleSound = { viewModel.toggleSound() }
                    )
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 6.dp,
                        modifier = Modifier
                            .testTag("main_bottom_nav_bar")
                            .windowInsetsPadding(WindowInsets.navigationBars)
                    ) {
                        NavigationBarItem(
                            selected = uiState.currentTab == AppNavTab.PORTADA,
                            onClick = { viewModel.selectTab(AppNavTab.PORTADA) },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Portada") },
                            label = { Text("Portada", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BilingoPrimary,
                                selectedTextColor = BilingoPrimary,
                                indicatorColor = BilingoPrimaryContainer
                            ),
                            modifier = Modifier.testTag("nav_item_portada")
                        )

                        NavigationBarItem(
                            selected = uiState.currentTab == AppNavTab.EXPLORE,
                            onClick = { viewModel.selectTab(AppNavTab.EXPLORE) },
                            icon = { Icon(Icons.Default.School, contentDescription = "Explorar") },
                            label = { Text("Explorar", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BilingoPrimary,
                                selectedTextColor = BilingoPrimary,
                                indicatorColor = BilingoPrimaryContainer
                            ),
                            modifier = Modifier.testTag("nav_item_explore")
                        )

                        NavigationBarItem(
                            selected = uiState.currentTab == AppNavTab.QUIZ,
                            onClick = { viewModel.selectTab(AppNavTab.QUIZ) },
                            icon = { Icon(Icons.Default.HelpOutline, contentDescription = "Adivina") },
                            label = { Text("Adivina", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BilingoPrimary,
                                selectedTextColor = BilingoPrimary,
                                indicatorColor = BilingoPrimaryContainer
                            ),
                            modifier = Modifier.testTag("nav_item_quiz")
                        )

                        NavigationBarItem(
                            selected = uiState.currentTab == AppNavTab.MEMORY,
                            onClick = { viewModel.selectTab(AppNavTab.MEMORY) },
                            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Memoria") },
                            label = { Text("Memoria", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BilingoPrimary,
                                selectedTextColor = BilingoPrimary,
                                indicatorColor = BilingoPrimaryContainer
                            ),
                            modifier = Modifier.testTag("nav_item_memory")
                        )

                        NavigationBarItem(
                            selected = uiState.currentTab == AppNavTab.BUILDER,
                            onClick = { viewModel.selectTab(AppNavTab.BUILDER) },
                            icon = { Icon(Icons.Default.Spellcheck, contentDescription = "Deletreo") },
                            label = { Text("Deletreo", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BilingoPrimary,
                                selectedTextColor = BilingoPrimary,
                                indicatorColor = BilingoPrimaryContainer
                            ),
                            modifier = Modifier.testTag("nav_item_builder")
                        )

                        NavigationBarItem(
                            selected = uiState.currentTab == AppNavTab.STARS,
                            onClick = { viewModel.selectTab(AppNavTab.STARS) },
                            icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Premios") },
                            label = { Text("Premios", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BilingoPrimary,
                                selectedTextColor = BilingoPrimary,
                                indicatorColor = BilingoPrimaryContainer
                            ),
                            modifier = Modifier.testTag("nav_item_stars")
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    CurrentTabContent(viewModel = viewModel, uiState = uiState)
                }
            }
        }
    }
}

@Composable
fun CurrentTabContent(viewModel: BilingoViewModel, uiState: BilingoUiState) {
    when (uiState.currentTab) {
        AppNavTab.PORTADA -> PortadaScreen(
            viewModel = viewModel,
            uiState = uiState
        )
        AppNavTab.EXPLORE -> ExploreScreen(
            viewModel = viewModel,
            uiState = uiState
        )
        AppNavTab.QUIZ -> QuizScreen(
            viewModel = viewModel,
            uiState = uiState
        )
        AppNavTab.MEMORY -> MemoryGameScreen(
            viewModel = viewModel,
            uiState = uiState
        )
        AppNavTab.BUILDER -> WordBuilderScreen(
            viewModel = viewModel,
            uiState = uiState
        )
        AppNavTab.STARS -> StarsAndParentsScreen(
            viewModel = viewModel,
            uiState = uiState
        )
    }
}
