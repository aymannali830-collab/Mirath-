package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MirathRepository
import com.example.model.Game
import com.example.ui.components.AudienceSelectorRow
import com.example.ui.components.GameCard
import com.example.ui.components.ShareGameBottomSheet
import com.example.ui.theme.*
import com.example.viewmodel.AudienceMode
import com.example.viewmodel.MirathUiState

@Composable
fun HomeScreen(
    uiState: MirathUiState,
    filteredGames: List<Game>,
    onSelectAudience: (AudienceMode) -> Unit,
    onSurpriseMe: () -> Unit,
    onExploreAll: () -> Unit,
    onGameClick: (String) -> Unit,
    onPlayClick: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onOpenMap: () -> Unit,
    onOpenGlossary: () -> Unit,
    onSuggestGame: () -> Unit,
    onOpenWebBrowser: () -> Unit = {}
) {
    val context = LocalContext.current
    var gameToShare by remember { mutableStateOf<Game?>(null) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Web Replica Promo Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { onOpenWebBrowser() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MirathWarmSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MirathGoldAccent.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MirathTerracotta.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = MirathTerracotta
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "موقع مِرث الإلكتروني (نسخة المتصفح)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MirathDarkBrown
                            )
                        )
                        Text(
                            text = "نسخة ويب مطابقة تماماً للتطبيق تعمل في أي متصفح مع كافة المحركات التفاعلية",
                            style = MaterialTheme.typography.bodySmall.copy(color = MirathMutedBrown)
                        )
                    }
                    Button(
                        onClick = onOpenWebBrowser,
                        colors = ButtonDefaults.buttonColors(containerColor = MirathTerracotta),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("فتح", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
        // Hero Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MirathCream),
                border = androidx.compose.foundation.BorderStroke(1.dp, MirathWarmSurface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MirathTerracotta.copy(alpha = 0.12f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MirathTerracotta.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "مِرث — Mirath",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MirathTerracottaDark
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "ألعاب العالم المنسية",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MirathDarkBrown,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "منصة عربية تجمع الألعاب التقليدية والتاريخية من حضارات الأرض، توثقها بمصادر دقيقة، وتحول كل لعبة إلى تجربة قابلة للتعلم واللعب التفاعلي الفوري.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MirathMutedBrown,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onExploreAll,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("hero_explore_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = MirathTerracotta),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Explore, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("اكتشف الألعاب")
                        }

                        FilledTonalButton(
                            onClick = onSurpriseMe,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("hero_surprise_me_button"),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = MirathWarmSurface,
                                contentColor = MirathDarkBrown
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Casino, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("فاجئني بلعبة")
                        }
                    }
                }
            }
        }

        // Functional Audience Modes Row
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "الأوضاع المخصصة للاستخدام:",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MirathDarkBrown
                    )
                )
            }
            AudienceSelectorRow(
                selected = uiState.selectedAudience,
                onSelect = onSelectAudience
            )
        }

        // Mode Explanatory Card (Schools / Researchers / Families)
        item {
            when (uiState.selectedAudience) {
                AudienceMode.SCHOOLS -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = MirathWarmSurface.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.School, contentDescription = null, tint = MirathTerracotta)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "وضع المدارس: يعرض الألعاب السريعة ذات القواعد الواضحة المرفقة بألواح قابلة للطباعة وأنشطة جماعية.",
                                style = MaterialTheme.typography.bodySmall.copy(color = MirathDarkBrown)
                            )
                        }
                    }
                }
                AudienceMode.RESEARCHERS -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = MirathGoldAccent.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Science, contentDescription = null, tint = MirathTerracottaDark)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "وضع الباحثين: يُظهر حواشي الإحالات التوثيقية [1] [2]، الأصول محل النزاع، وتاريخ المراجعات والتحقيق.",
                                style = MaterialTheme.typography.bodySmall.copy(color = MirathDarkBrown)
                            )
                        }
                    }
                }
                AudienceMode.FAMILIES -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = MirathCream),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FamilyRestroom, contentDescription = null, tint = MirathTerracotta)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "وضع العائلات: ألعاب ممتعة لجميع الأعمار تُلعب بأدوات منزلية بسيطة بدون تعقيد.",
                                style = MaterialTheme.typography.bodySmall.copy(color = MirathDarkBrown)
                            )
                        }
                    }
                }
                else -> {}
            }
        }

        // Quick Navigation Tiles (Map / Glossary / Suggestion)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Map
                OutlinedCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenMap() }
                        .testTag("nav_map_tile"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = MirathCream)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Public, contentDescription = null, tint = MirathTerracotta)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("خريطة الألعاب", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                        Text("الدول والمناطق", style = MaterialTheme.typography.labelSmall.copy(color = MirathMutedBrown))
                    }
                }

                // Glossary
                OutlinedCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenGlossary() }
                        .testTag("nav_glossary_tile"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = MirathCream)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = MirathTerracotta)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("معجم المصطلحات", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                        Text("مفاهيم التراث", style = MaterialTheme.typography.labelSmall.copy(color = MirathMutedBrown))
                    }
                }

                // Suggest
                OutlinedCard(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSuggestGame() }
                        .testTag("nav_suggest_tile"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = MirathCream)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.AddComment, contentDescription = null, tint = MirathTerracotta)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("اقترح لعبة", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                        Text("للتوثيق والبحث", style = MaterialTheme.typography.labelSmall.copy(color = MirathMutedBrown))
                    }
                }
            }
        }

        // Interactive D3 Geographic Regions Chart Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "توزيع الألعاب حسب الأقاليم الجغرافية",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            Text(
                                text = "رسم بياني تفاعلي باستخدام D3 يعرض كثافة الألعاب الموثقة",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MirathTerracotta.copy(alpha = 0.12f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MirathTerracotta.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "مخطط D3 تفاعلي",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MirathTerracotta
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Embedded D3 Chart via AndroidView WebView (Software Layer to avoid DRI rendernode warnings)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Transparent)
                    ) {
                        androidx.compose.ui.viewinterop.AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { ctx ->
                                android.webkit.WebView(ctx).apply {
                                    setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null)
                                    layoutParams = android.view.ViewGroup.LayoutParams(
                                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                        android.view.ViewGroup.LayoutParams.MATCH_PARENT
                                    )
                                    setBackgroundColor(android.graphics.Color.TRANSPARENT)
                                    settings.apply {
                                        javaScriptEnabled = true
                                        domStorageEnabled = true
                                        allowFileAccess = true
                                        allowContentAccess = true
                                    }
                                    loadUrl("file:///android_asset/web/region_chart.html")
                                }
                            },
                            update = { webView ->
                                val isDark = uiState.isDarkModeEnabled
                                webView.evaluateJavascript("if (window.setTheme) window.setTheme($isDark);", null)
                            }
                        )
                    }
                }
            }
        }

        // Section Title: Catalog of Featured Documented Games
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "الألعاب الموثقة القابلة للعب (${filteredGames.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                TextButton(onClick = onExploreAll) {
                    Text("عرض الكل", color = MirathTerracotta)
                }
            }
        }

        // Games List
        items(filteredGames) { game ->
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

    // Modal Bottom Sheet for Sharing from Home
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
