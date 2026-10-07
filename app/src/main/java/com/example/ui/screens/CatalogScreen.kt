package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.MirathRepository
import com.example.model.Game
import com.example.ui.components.GameCard
import com.example.ui.components.ShareGameBottomSheet
import com.example.ui.theme.*
import com.example.viewmodel.MirathUiState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    uiState: MirathUiState,
    filteredGames: List<Game>,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (String?) -> Unit,
    onRegionSelect: (String?) -> Unit,
    onRulesDiffSelect: (Int?) -> Unit,
    onSurpriseMe: () -> Unit,
    onGameClick: (String) -> Unit,
    onPlayClick: (String) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    val context = LocalContext.current
    var gameToShare by remember { mutableStateOf<Game?>(null) }
    val categories = listOf("سباق وتكتيك مسار", "تطويق وقفز إستراتيجي", "حساب إيقاعي وبذر بذور", "تراصف ومطاردة سريعة")
    val regions = MirathRepository.regions
    val quickSearchTags = listOf("أور", "العراق", "القرق", "الأندلس", "أواري", "غانا", "شيسيما", "كينيا", "سباق", "مانكالا", "تطويق", "تراصف")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Search Header with instant filtering by Name, Region, or Genre
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "البحث الفوري في الألعاب",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MirathDarkBrown
                    )
                )
                Text(
                    text = "ابحث بالاسم (أور، القرق...) أو المنطقة (العراق، كينيا...) أو النوع (سباق، مانكالا...)",
                    style = MaterialTheme.typography.bodySmall.copy(color = MirathMutedBrown),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("catalog_search_input"),
                    placeholder = {
                        Text(
                            text = "ابحث بالاسم، المنطقة، أو النوع...",
                            style = MaterialTheme.typography.bodyMedium.copy(color = MirathMutedBrown)
                        )
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "بحث", tint = MirathTerracotta)
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { onSearchChange("") },
                                modifier = Modifier.testTag("clear_search_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "مسح البحث", tint = MirathTerracotta)
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MirathCream,
                        unfocusedContainerColor = MirathCream,
                        focusedBorderColor = MirathTerracotta,
                        unfocusedBorderColor = MirathWarmSurface
                    ),
                    singleLine = true
                )

                // Quick Search Suggestion Chips
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(vertical = 2.dp)
                ) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MirathWarmSurface.copy(alpha = 0.6f)
                        ) {
                            Text(
                                text = "اقتراحات سريعة:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MirathMutedBrown,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                    items(quickSearchTags) { tag ->
                        val isSelected = uiState.searchQuery == tag
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) MirathTerracotta else MirathCream,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) MirathTerracotta else MirathWarmSurface),
                            modifier = Modifier.clickable {
                                if (isSelected) onSearchChange("") else onSearchChange(tag)
                            }
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) Color.White else MirathDarkBrown,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                if (uiState.searchQuery.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MirathTerracotta.copy(alpha = 0.12f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MirathTerracotta.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "تصفية فورية: '${uiState.searchQuery}' (${filteredGames.size} لعبة)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MirathTerracottaDark
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        TextButton(
                            onClick = { onSearchChange("") },
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
                        ) {
                            Text("إلغاء التصفية", style = MaterialTheme.typography.labelSmall.copy(color = MirathTerracotta))
                        }
                    }
                }
            }
        }

        // Surprise Me & Stats Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "النتائج (${filteredGames.size} لعبة)",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MirathDarkBrown
                    )
                )

                FilledTonalButton(
                    onClick = onSurpriseMe,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MirathWarmSurface,
                        contentColor = MirathDarkBrown
                    )
                ) {
                    Icon(imageVector = Icons.Default.Casino, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("فاجئني")
                }
            }
        }

        // Filter: Categories
        item {
            Text(
                text = "الفئة الأساسية:",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MirathMutedBrown
                ),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = uiState.selectedCategory == null,
                        onClick = { onCategorySelect(null) },
                        label = { Text("جميع الفئات") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MirathTerracotta,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                items(categories) { cat ->
                    FilterChip(
                        selected = uiState.selectedCategory == cat,
                        onClick = { onCategorySelect(if (uiState.selectedCategory == cat) null else cat) },
                        label = { Text(cat) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MirathTerracotta,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Filter: Geographic Region
        item {
            Text(
                text = "المنطقة الجغرافية:",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MirathMutedBrown
                ),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = uiState.selectedRegion == null,
                        onClick = { onRegionSelect(null) },
                        label = { Text("كل المناطق") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MirathBurntOrange,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                items(regions) { r ->
                    FilterChip(
                        selected = uiState.selectedRegion == r.id,
                        onClick = { onRegionSelect(if (uiState.selectedRegion == r.id) null else r.id) },
                        label = { Text(r.nameAr) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MirathBurntOrange,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Filter: Rules Difficulty (1 to 5)
        item {
            Text(
                text = "صعوبة القواعد (rulesDifficulty):",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MirathMutedBrown
                ),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.selectedRulesDifficulty == null,
                    onClick = { onRulesDiffSelect(null) },
                    label = { Text("الكل") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MirathTerracottaDark,
                        selectedLabelColor = Color.White
                    )
                )
                (1..4).forEach { diff ->
                    FilterChip(
                        selected = uiState.selectedRulesDifficulty == diff,
                        onClick = { onRulesDiffSelect(if (uiState.selectedRulesDifficulty == diff) null else diff) },
                        label = { Text("مستوى $diff") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MirathTerracottaDark,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Games List
        if (filteredGames.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MirathCream)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.FilterList, contentDescription = null, tint = MirathTerracotta, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "لا توجد ألعاب تطابق معايير البحث الحالية",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MirathDarkBrown
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "جرّب مسح الفلاتر أو كتابة جزء من اسم اللعبة أو ثقافتها الأصلية.",
                            style = MaterialTheme.typography.bodySmall.copy(color = MirathMutedBrown)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                onSearchChange("")
                                onCategorySelect(null)
                                onRegionSelect(null)
                                onRulesDiffSelect(null)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MirathTerracotta),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("إعادة تعيين البحث وإظهار جميع الألعاب")
                        }
                    }
                }
            }
        } else {
            itemsIndexed(
                items = filteredGames,
                key = { _, game -> "${game.slug}_${uiState.searchQuery}_${uiState.selectedCategory}_${uiState.selectedRegion}_${uiState.selectedRulesDifficulty}" }
            ) { index, game ->
                var isVisible by remember(game.slug, uiState.searchQuery, uiState.selectedCategory, uiState.selectedRegion, uiState.selectedRulesDifficulty) {
                    mutableStateOf(false)
                }

                LaunchedEffect(game.slug, uiState.searchQuery, uiState.selectedCategory, uiState.selectedRegion, uiState.selectedRulesDifficulty) {
                    delay((index * 45L).coerceAtMost(300L))
                    isVisible = true
                }

                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 350,
                            easing = FastOutSlowInEasing
                        )
                    ) + slideInVertically(
                        initialOffsetY = { 30 },
                        animationSpec = tween(
                            durationMillis = 350,
                            easing = FastOutSlowInEasing
                        )
                    ),
                    exit = fadeOut(animationSpec = tween(durationMillis = 150))
                ) {
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        GameCard(
                            game = game,
                            isFavorite = uiState.favoritesSlugs.contains(game.slug),
                            isResearcherMode = uiState.isResearcherModeEnabled,
                            onCardClick = { onGameClick(game.slug) },
                            onPlayClick = { onPlayClick(game.slug) },
                            onToggleFavorite = { onToggleFavorite(game.slug) },
                            onShareClick = { gameToShare = game }
                        )
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet for Sharing
    gameToShare?.let { game ->
        ShareGameBottomSheet(
            game = game,
            onDismiss = { gameToShare = null },
            onCopyLink = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("رابط لعبة ${game.titleAr}", "https://mirath.app/games/${game.slug}")
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "تم نسخ رابط اللعبة إلى الحافظة بنجاح 📋", Toast.LENGTH_SHORT).show()
                gameToShare = null
            },
            onShareExternal = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "لعبة ${game.titleAr} على منصة مِرث")
                    putExtra(
                        Intent.EXTRA_TEXT,
                        """
                        تعرّف على لعبة "${game.titleAr}" (${game.origin.culture ?: "أصل تراثي"}):
                        ${game.whatMakesItDifferentAr}
                        
                        العبها ووثّق قواعدها التاريخية:
                        https://mirath.app/games/${game.slug}
                        """.trimIndent()
                    )
                }
                context.startActivity(Intent.createChooser(shareIntent, "مشاركة لعبة ${game.titleAr}"))
                gameToShare = null
            }
        )
    }
}
