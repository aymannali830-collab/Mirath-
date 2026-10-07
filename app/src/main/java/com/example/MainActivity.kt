package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.MirathRepository
import com.example.ui.components.MirathTopAppBar
import com.example.ui.screens.*
import com.example.ui.theme.MirathTheme
import com.example.viewmodel.MirathViewModel
import com.example.viewmodel.ScreenDestination

class MainActivity : ComponentActivity() {

    private val viewModel: MirathViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val filteredGames = remember(uiState) { viewModel.getFilteredGames() }
            val snackbarHostState = remember { SnackbarHostState() }

            // Force RTL layout direction across the entire platform
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                MirathTheme(darkTheme = uiState.isDarkModeEnabled) {
                    // Trigger snackbar when state updates
                    LaunchedEffect(uiState.snackbarMessage) {
                        uiState.snackbarMessage?.let { msg ->
                            snackbarHostState.showSnackbar(msg)
                            viewModel.clearSnackbar()
                        }
                    }

                    // System Back Handler
                    BackHandler(enabled = uiState.currentScreen != ScreenDestination.Home) {
                        viewModel.navigateBack()
                    }

                    Scaffold(
                        topBar = {
                            val title = when (val s = uiState.currentScreen) {
                                is ScreenDestination.Home -> "مِرث"
                                is ScreenDestination.Catalog -> "كتالوج الألعاب"
                                is ScreenDestination.GameDetail -> MirathRepository.getGameBySlug(s.slug)?.titleAr ?: "تفاصيل اللعبة"
                                is ScreenDestination.PlayGame -> "تجربة اللعب: ${MirathRepository.getGameBySlug(s.slug)?.titleAr ?: ""}"
                                is ScreenDestination.MapRegions -> "خريطة الأقاليم"
                                is ScreenDestination.CountryDetail -> MirathRepository.getCountryById(s.countryId)?.nameAr ?: "الدولة"
                                is ScreenDestination.GlossaryList -> "معجم المصطلحات"
                                is ScreenDestination.AdminHub -> "بوابة الإدارة والمراجعة"
                                is ScreenDestination.SuggestGame -> "اقترح لعبة"
                                is ScreenDestination.RuleCorrectionScreen -> "تصحيح القواعد"
                                is ScreenDestination.WebBrowserReplica -> "نسخة المتصفح (Web)"
                            }
                            val subtitle = if (uiState.currentScreen is ScreenDestination.Home) "ألعاب العالم المنسية" else null

                            MirathTopAppBar(
                                title = title,
                                subtitle = subtitle,
                                canNavigateBack = uiState.currentScreen != ScreenDestination.Home,
                                onNavigateBack = { viewModel.navigateBack() },
                                isResearcherMode = uiState.isResearcherModeEnabled,
                                onToggleResearcherMode = { viewModel.toggleResearcherMode() },
                                onAdminClick = { viewModel.navigateTo(ScreenDestination.AdminHub) },
                                onWebBrowserClick = { viewModel.navigateTo(ScreenDestination.WebBrowserReplica) },
                                isDarkMode = uiState.isDarkModeEnabled,
                                onToggleDarkMode = { viewModel.toggleDarkMode() }
                            )
                        },
                        bottomBar = {
                            // Bottom Navigation for main hubs
                            NavigationBar(
                                windowInsets = WindowInsets.navigationBars
                            ) {
                                NavigationBarItem(
                                    selected = uiState.currentScreen is ScreenDestination.Home,
                                    onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                                    icon = { Icon(Icons.Default.Home, contentDescription = "الرئيسية") },
                                    label = { Text("الرئيسية") }
                                )
                                NavigationBarItem(
                                    selected = uiState.currentScreen is ScreenDestination.Catalog,
                                    onClick = { viewModel.navigateTo(ScreenDestination.Catalog) },
                                    icon = { Icon(Icons.Default.Casino, contentDescription = "الألعاب") },
                                    label = { Text("الألعاب") }
                                )
                                NavigationBarItem(
                                    selected = uiState.currentScreen is ScreenDestination.MapRegions,
                                    onClick = { viewModel.navigateTo(ScreenDestination.MapRegions) },
                                    icon = { Icon(Icons.Default.Public, contentDescription = "الخريطة") },
                                    label = { Text("الخريطة") }
                                )
                                NavigationBarItem(
                                    selected = uiState.currentScreen is ScreenDestination.GlossaryList,
                                    onClick = { viewModel.navigateTo(ScreenDestination.GlossaryList) },
                                    icon = { Icon(Icons.Default.MenuBook, contentDescription = "المعجم") },
                                    label = { Text("المعجم") }
                                )
                            }
                        },
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (val screen = uiState.currentScreen) {
                                is ScreenDestination.Home -> {
                                    HomeScreen(
                                        uiState = uiState,
                                        filteredGames = filteredGames,
                                        onSelectAudience = { mode -> viewModel.setAudience(mode) },
                                        onSurpriseMe = { viewModel.surpriseMe() },
                                        onExploreAll = { viewModel.navigateTo(ScreenDestination.Catalog) },
                                        onGameClick = { slug -> viewModel.navigateTo(ScreenDestination.GameDetail(slug)) },
                                        onPlayClick = { slug -> viewModel.startPlay(slug) },
                                        onToggleFavorite = { slug -> viewModel.toggleFavorite(slug) },
                                        onOpenMap = { viewModel.navigateTo(ScreenDestination.MapRegions) },
                                        onOpenGlossary = { viewModel.navigateTo(ScreenDestination.GlossaryList) },
                                        onSuggestGame = { viewModel.navigateTo(ScreenDestination.SuggestGame) },
                                        onOpenWebBrowser = { viewModel.navigateTo(ScreenDestination.WebBrowserReplica) }
                                    )
                                }
                                is ScreenDestination.Catalog -> {
                                    CatalogScreen(
                                        uiState = uiState,
                                        filteredGames = filteredGames,
                                        onSearchChange = { q -> viewModel.setSearchQuery(q) },
                                        onCategorySelect = { cat -> viewModel.setFilterCategory(cat) },
                                        onRegionSelect = { reg -> viewModel.setFilterRegion(reg) },
                                        onRulesDiffSelect = { diff -> viewModel.setFilterRulesDifficulty(diff) },
                                        onSurpriseMe = { viewModel.surpriseMe() },
                                        onGameClick = { slug -> viewModel.navigateTo(ScreenDestination.GameDetail(slug)) },
                                        onPlayClick = { slug -> viewModel.startPlay(slug) },
                                        onToggleFavorite = { slug -> viewModel.toggleFavorite(slug) }
                                    )
                                }
                                is ScreenDestination.GameDetail -> {
                                    GameDetailScreen(
                                        slug = screen.slug,
                                        isResearcherMode = uiState.isResearcherModeEnabled,
                                        isFavorite = uiState.favoritesSlugs.contains(screen.slug),
                                        onToggleFavorite = { viewModel.toggleFavorite(screen.slug) },
                                        onPlayClick = { viewModel.startPlay(screen.slug) },
                                        onNavigateToSlug = { target -> viewModel.navigateTo(ScreenDestination.GameDetail(target)) },
                                        onRuleCorrectionClick = { viewModel.navigateTo(ScreenDestination.RuleCorrectionScreen(screen.slug)) }
                                    )
                                }
                                is ScreenDestination.PlayGame -> {
                                    PlayGameScreen(
                                        slug = screen.slug,
                                        uiState = uiState,
                                        viewModel = viewModel,
                                        onBack = { viewModel.navigateBack() }
                                    )
                                }
                                is ScreenDestination.MapRegions -> {
                                    MapRegionsScreen(
                                        onCountryClick = { countryId -> viewModel.navigateTo(ScreenDestination.CountryDetail(countryId)) }
                                    )
                                }
                                is ScreenDestination.CountryDetail -> {
                                    CountryDetailScreen(
                                        countryId = screen.countryId,
                                        onGameClick = { slug -> viewModel.navigateTo(ScreenDestination.GameDetail(slug)) }
                                    )
                                }
                                is ScreenDestination.GlossaryList -> {
                                    GlossaryScreen(
                                        onGameClick = { slug -> viewModel.navigateTo(ScreenDestination.GameDetail(slug)) }
                                    )
                                }
                                is ScreenDestination.AdminHub -> {
                                    AdminHubScreen(
                                        uiState = uiState,
                                        onToggleDarkMode = { viewModel.toggleDarkMode() }
                                    )
                                }
                                is ScreenDestination.SuggestGame -> {
                                    SuggestGameScreen(
                                        onSubmit = { title, country, desc, source, submitter ->
                                            viewModel.submitSuggestion(title, country, desc, source, submitter)
                                        }
                                    )
                                }
                                is ScreenDestination.RuleCorrectionScreen -> {
                                    RuleCorrectionScreen(
                                        gameSlug = screen.gameSlug,
                                        onSubmit = { slug, section, text, source ->
                                            viewModel.submitRuleCorrection(slug, section, text, source)
                                        }
                                    )
                                }
                                is ScreenDestination.WebBrowserReplica -> {
                                    WebBrowserReplicaScreen(
                                        onNavigateBack = { viewModel.navigateBack() }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
