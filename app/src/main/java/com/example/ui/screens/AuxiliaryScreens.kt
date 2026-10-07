package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MirathRepository
import com.example.ui.theme.*
import com.example.util.MirathCatalogExportUtil
import com.example.viewmodel.MirathUiState
import com.example.viewmodel.MirathViewModel

// -------------------------------------------------------------
// Map & Regions Screen
// -------------------------------------------------------------
@Composable
fun MapRegionsScreen(
    onCountryClick: (String) -> Unit
) {
    val regions = MirathRepository.regions
    val countries = MirathRepository.countries

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MirathCream),
                border = androidx.compose.foundation.BorderStroke(1.dp, MirathWarmSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "جغرافية الألعاب التقليدية الموثقة",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MirathDarkBrown
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "استكشف مهد الألعاب عبر أقاليم وحضارات العالم القديم ووثائق نشأتها.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MirathMutedBrown)
                    )
                }
            }
        }

        items(regions) { region ->
            val regionCountries = countries.filter { it.regionId == region.id }
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MirathCream),
                border = androidx.compose.foundation.BorderStroke(1.dp, MirathWarmSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Public, contentDescription = null, tint = MirathTerracotta)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = region.nameAr,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MirathTerracottaDark
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = region.summaryAr,
                        style = MaterialTheme.typography.bodySmall.copy(color = MirathMutedBrown)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    regionCountries.forEach { c ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MirathWarmSurface.copy(alpha = 0.6f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onCountryClick(c.id) }
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = c.nameAr,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = c.historicalGamingCultureAr,
                                        style = MaterialTheme.typography.labelSmall.copy(color = MirathMutedBrown),
                                        maxLines = 1
                                    )
                                }
                                Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = MirathTerracotta)
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Country Detail Screen
// -------------------------------------------------------------
@Composable
fun CountryDetailScreen(
    countryId: String,
    onGameClick: (String) -> Unit
) {
    val country = MirathRepository.getCountryById(countryId)
    val games = MirathRepository.games.filter { it.origin.countryId == countryId }

    if (country == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("الدولة غير محددة")
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MirathCream),
                border = androidx.compose.foundation.BorderStroke(1.dp, MirathWarmSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = country.nameAr,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MirathDarkBrown
                        )
                    )
                    Text(
                        text = country.nameEn,
                        style = MaterialTheme.typography.bodySmall.copy(color = MirathMutedBrown)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = country.summaryAr,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MirathDarkBrown, lineHeight = 22.sp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "ثقافة اللعب التاريخية: ${country.historicalGamingCultureAr}",
                        style = MaterialTheme.typography.bodySmall.copy(color = MirathTerracottaDark)
                    )
                }
            }
        }

        item {
            Text(
                text = "الألعاب الموثقة المرتبطة بهذا القطر (${games.size})",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MirathDarkBrown
                )
            )
        }

        items(games) { game ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MirathCream,
                border = androidx.compose.foundation.BorderStroke(1.dp, MirathWarmSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onGameClick(game.slug) }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = game.titleAr,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${game.primaryCategory} • ${game.players.min}-${game.players.max} لاعبين",
                            style = MaterialTheme.typography.bodySmall.copy(color = MirathMutedBrown)
                        )
                    }
                    Button(
                        onClick = { onGameClick(game.slug) },
                        colors = ButtonDefaults.buttonColors(containerColor = MirathTerracotta),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("عرض")
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Glossary Screen
// -------------------------------------------------------------
@Composable
fun GlossaryScreen(
    onGameClick: (String) -> Unit
) {
    val glossary = MirathRepository.glossary

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MirathCream),
                border = androidx.compose.foundation.BorderStroke(1.dp, MirathWarmSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "معجم مصطلحات ألعاب التراث",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MirathDarkBrown
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "شرح للمفاهيم الميكانيكية والتاريخية (البذر، اللعب غير المتناظر، خانات الروزيت، الأسر الإجباري).",
                        style = MaterialTheme.typography.bodySmall.copy(color = MirathMutedBrown)
                    )
                }
            }
        }

        items(glossary) { term ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MirathCream),
                border = androidx.compose.foundation.BorderStroke(1.dp, MirathWarmSurface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = term.termAr,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MirathTerracottaDark
                            )
                        )
                        if (term.originLanguageAr != null) {
                            Text(
                                text = "أصل المصطلح: ${term.originLanguageAr}",
                                style = MaterialTheme.typography.labelSmall.copy(color = MirathMutedBrown)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = term.definitionAr,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MirathDarkBrown, lineHeight = 20.sp)
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Admin & Moderation Hub Screen
// -------------------------------------------------------------
@Composable
fun AdminHubScreen(
    uiState: MirathUiState,
    onToggleDarkMode: () -> Unit = {}
) {
    val context = LocalContext.current
    var showExportedJson by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Display Settings Section (إعدادات العرض - الوضع الليلي التراثي)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (uiState.isDarkModeEnabled) MirathGoldAccent.copy(alpha = 0.5f) else MirathWarmSurface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (uiState.isDarkModeEnabled) Icons.Default.NightlightRound else Icons.Default.WbSunny,
                            contentDescription = null,
                            tint = if (uiState.isDarkModeEnabled) MirathGoldAccent else MirathTerracotta
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "إعدادات العرض (Display Settings)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = "تخصيص مظهر المنصة البصري وتفضيلات القراءة للمخطوطات التراثية.",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (uiState.isDarkModeEnabled) MirathGoldAccent.copy(alpha = 0.3f) else MirathSand
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "الوضع الليلي (ألوان المخطوطات القديمة)",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (uiState.isDarkModeEnabled)
                                        "مفعّل: درجات الحبر الداكن وجلد المخطوطات القديمة مع خطوط مذهبة مريحة للعين"
                                    else
                                        "غير مفعّل: وضع الرق والورق العاجي الفاتح",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (uiState.isDarkModeEnabled) MirathGoldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Switch(
                                checked = uiState.isDarkModeEnabled,
                                onCheckedChange = { onToggleDarkMode() },
                                modifier = Modifier.testTag("display_settings_dark_mode_switch"),
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MirathGoldAccent,
                                    checkedTrackColor = MirathTerracottaDark,
                                    uncheckedThumbColor = MirathSand,
                                    uncheckedTrackColor = MirathWarmSurface
                                )
                            )
                        }
                    }
                }
            }
        }

        // Admin Header & Gate Notice
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MirathTerracotta)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = MirathTerracotta)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "بوابة الإدارة والمراجعة والنشر (Admin Hub)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "معايير النشر الصارمة: لا تُنشر أي لعبة إلا بمصادر موثقة، محرك لعب تفاعلي، شروط فوز، وتدقيق تاريخي غير مختلق.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }
        }

        // Export Catalog JSON (Single Source of Truth / No Lock-in)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MirathWarmSurface.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "تصدير الكتالوج الموحد (Portable JSON Export)",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MirathDarkBrown
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "تصدير جميع سجلات الألعاب والمصادر والأصول لتفادي الحصر في منصة واحدة.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MirathMutedBrown)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            val json = MirathCatalogExportUtil.exportCatalogToJson()
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Mirath Catalog", json))
                            Toast.makeText(context, "تم نسخ كتالوج مِرث الكامل (JSON) إلى الحافظة بنجاح!", Toast.LENGTH_LONG).show()
                            showExportedJson = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MirathTerracotta),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("export_json_btn")
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("تصدير الكتالوج بصيغة JSON")
                    }

                    if (showExportedJson) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "تم نسخ ملف JSON منظم يطابق مواصفة الكتالوج ويمكن تصديره لأي خادم خارجي.",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF2E7D32))
                        )
                    }
                }
            }
        }

        // Community Suggestions Moderation Queue
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MirathCream),
                border = androidx.compose.foundation.BorderStroke(1.dp, MirathWarmSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "قائمة مراجعة الاقتراحات الجديدة (${uiState.suggestions.size})",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MirathDarkBrown
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (uiState.suggestions.isEmpty()) {
                        Text(
                            text = "لا توجد اقتراحات معلقة حالياً في طابور التدقيق.",
                            style = MaterialTheme.typography.bodySmall.copy(color = MirathMutedBrown)
                        )
                    } else {
                        uiState.suggestions.forEach { sugg ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MirathWarmSurface.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(text = "اسم اللعبة: ${sugg.titleAr} (${sugg.countryAr})", fontWeight = FontWeight.Bold)
                                    Text(text = "الوصف: ${sugg.descriptionAr}", style = MaterialTheme.typography.bodySmall)
                                    if (sugg.sourceUrl != null) {
                                        Text(text = "المصدر: ${sugg.sourceUrl}", style = MaterialTheme.typography.labelSmall, color = MirathTerracottaDark)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Rule Corrections Queue
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MirathCream),
                border = androidx.compose.foundation.BorderStroke(1.dp, MirathWarmSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "قائمة تدقيق تصحيحات القواعد (${uiState.ruleCorrections.size})",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MirathDarkBrown
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (uiState.ruleCorrections.isEmpty()) {
                        Text(
                            text = "لا توجد طلبات تصحيح قواعد معلقة.",
                            style = MaterialTheme.typography.bodySmall.copy(color = MirathMutedBrown)
                        )
                    } else {
                        uiState.ruleCorrections.forEach { corr ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MirathWarmSurface.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(text = "اللعبة: ${corr.gameSlug} — قسم: ${corr.sectionAr}", fontWeight = FontWeight.Bold)
                                    Text(text = "التصحيح المقترح: ${corr.proposedCorrectionAr}", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Suggest Game Screen
// -------------------------------------------------------------
@Composable
fun SuggestGameScreen(
    onSubmit: (String, String, String, String?, String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var sourceUrl by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "اقتراح لعبة تقليدية للتوثيق والتحقيق",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MirathDarkBrown
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "ساعد منصة مِرث في استكشاف ألعاب الأجداد المنسية من ثقافتك أو بلدك. تدخل كل مساهمة طابور التحقق قبل إدراجها.",
                style = MaterialTheme.typography.bodySmall.copy(color = MirathMutedBrown)
            )
        }

        item {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("اسم اللعبة الأصلي أو المتداول") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = country,
                onValueChange = { country = it },
                label = { Text("الدولة أو المنطقة الثقافية") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = desc,
                onValueChange = { desc = it },
                label = { Text("وصف اللعبة، أدواتها، وكيف تلعب؟") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
        }

        item {
            OutlinedTextField(
                value = sourceUrl,
                onValueChange = { sourceUrl = it },
                label = { Text("مصدر موثوق أو مرجع (اختياري)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("اسمك لإضافته في قائمة الشكر والاعتمادات (اختياري)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            Button(
                onClick = {
                    if (title.isNotBlank() && country.isNotBlank()) {
                        onSubmit(title, country, desc, sourceUrl.ifBlank { null }, name.ifBlank { null })
                    }
                },
                enabled = title.isNotBlank() && country.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MirathTerracotta),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_game_suggestion_btn")
            ) {
                Icon(Icons.Default.Send, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("إرسال الاقتراح للمراجعة")
            }
        }
    }
}

// -------------------------------------------------------------
// Rule Correction Screen
// -------------------------------------------------------------
@Composable
fun RuleCorrectionScreen(
    gameSlug: String,
    onSubmit: (String, String, String, String?) -> Unit
) {
    var section by remember { mutableStateOf("قواعد عامة") }
    var correctionText by remember { mutableStateOf("") }
    var sourceCitation by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "اقتراح تصحيح لقواعد لعبة: $gameSlug",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MirathDarkBrown
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "في مِرث، نلتزم بالدقة الصارمة وعدم اختلاق القواعد. إذا كان لديك تصحيح مدعوم بمرجع تاريخي، أرسله هنا.",
                style = MaterialTheme.typography.bodySmall.copy(color = MirathMutedBrown)
            )
        }

        item {
            OutlinedTextField(
                value = section,
                onValueChange = { section = it },
                label = { Text("القسم أو الخطوة المراد تصحيحها") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = correctionText,
                onValueChange = { correctionText = it },
                label = { Text("التصحيح المقترح بالتفصيل") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )
        }

        item {
            OutlinedTextField(
                value = sourceCitation,
                onValueChange = { sourceCitation = it },
                label = { Text("المرجع أو المصدر الأكاديمي الداعم (إن وجد)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }

        item {
            Button(
                onClick = {
                    if (correctionText.isNotBlank()) {
                        onSubmit(gameSlug, section, correctionText, sourceCitation.ifBlank { null })
                    }
                },
                enabled = correctionText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MirathTerracotta),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("إرسال التدقيق لفريق المراجعة")
            }
        }
    }
}
