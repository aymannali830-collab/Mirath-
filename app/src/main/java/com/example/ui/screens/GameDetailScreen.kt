package com.example.ui.screens

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MirathRepository
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun GameDetailScreen(
    slug: String,
    isResearcherMode: Boolean,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onPlayClick: () -> Unit,
    onNavigateToSlug: (String) -> Unit,
    onRuleCorrectionClick: () -> Unit
) {
    val game = MirathRepository.getGameBySlug(slug)

    if (game == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MirathIvory),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "اللعبة غير موجودة", style = MaterialTheme.typography.titleMedium)
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MirathIvory),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Breadcrumb & Title Header
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MirathCream),
                border = androidx.compose.foundation.BorderStroke(1.dp, MirathWarmSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "الرئيسية > الألعاب > ${game.primaryCategory} > ${game.titleAr}",
                        style = MaterialTheme.typography.labelSmall.copy(color = MirathMutedBrown)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = game.titleAr,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MirathDarkBrown
                                )
                            )
                            if (game.titleEn != null) {
                                Text(
                                    text = "${game.titleEn} (${game.originalTitle ?: ""})",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = MirathMutedBrown)
                                )
                            }
                        }

                        IconButton(onClick = onToggleFavorite) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "تفضيل",
                                tint = if (isFavorite) MirathTerracotta else MirathMutedBrown
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Origin & Cultural context
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MirathWarmSurface.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Place, contentDescription = null, tint = MirathTerracotta, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "الأصل: ${game.origin.culture ?: "غير محدد"} • العصر: ${game.historicalContext.era ?: "غير متاح"}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MirathDarkBrown
                            )
                        )
                    }
                }
            }
        }

        // 2. Play CTA Bar
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MirathTerracotta),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "تجربة لعب تفاعلية فورية وموثقة",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "محرك ${game.playability.engine.name} — يدعم اللعب المحلي ومواجهة الذكاء الاصطناعي ووضع التدريب",
                        style = MaterialTheme.typography.bodySmall.copy(color = MirathCream)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onPlayClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MirathCream, contentColor = MirathTerracottaDark),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("play_game_cta_button")
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "ابدأ اللعب الآن", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 3. Technical Parameters (Players, Time, Dual Difficulty, Rules vs Mastery)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MirathCream),
                border = androidx.compose.foundation.BorderStroke(1.dp, MirathWarmSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "المواصفات الفنية للعبة",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MirathTerracottaDark
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.People, contentDescription = null, tint = MirathTerracotta)
                            Text("${game.players.min}-${game.players.max} لاعبين", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                            Text("العدد", style = MaterialTheme.typography.labelSmall.copy(color = MirathMutedBrown))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = MirathTerracotta)
                            Text("${game.estimatedPlayTime.minMinutes}-${game.estimatedPlayTime.maxMinutes} دقيقة", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                            Text("مدة المباراة", style = MaterialTheme.typography.labelSmall.copy(color = MirathMutedBrown))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.School, contentDescription = null, tint = MirathTerracotta)
                            Text("${game.timeToLearnMinutes} دقائق", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                            Text("وقت التعلم", style = MaterialTheme.typography.labelSmall.copy(color = MirathMutedBrown))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = MirathTerracotta)
                            Text("${game.rulesDifficulty} / 5", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                            Text("صعوبة القواعد", style = MaterialTheme.typography.labelSmall.copy(color = MirathMutedBrown))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = MirathTerracotta)
                            Text("${game.masteryDifficulty} / 5", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                            Text("صعوبة الإتقان", style = MaterialTheme.typography.labelSmall.copy(color = MirathMutedBrown))
                        }
                    }
                }
            }
        }

        // 4. Why This Game & What Makes It Unique
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MirathCream),
                border = androidx.compose.foundation.BorderStroke(1.dp, MirathWarmSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ما الذي يميز هذه اللعبة؟",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MirathDarkBrown
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = game.whatMakesItDifferentAr,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MirathDarkBrown, lineHeight = 22.sp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "لماذا توثقها مِرث؟",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MirathDarkBrown
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = game.whyThisGameAr,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MirathMutedBrown, lineHeight = 22.sp)
                    )
                }
            }
        }

        // 5. Rules & How To Play
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MirathCream),
                border = androidx.compose.foundation.BorderStroke(1.dp, MirathWarmSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "قواعد اللعبة التفصيلية",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MirathTerracottaDark
                            )
                        )
                        TextButton(onClick = onRuleCorrectionClick) {
                            Text("اقتراح تصحيح للقواعد", style = MaterialTheme.typography.labelSmall.copy(color = MirathTerracotta))
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = game.rules.overviewAr,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MirathDarkBrown)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    game.rules.steps.forEach { step ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MirathWarmSurface.copy(alpha = 0.5f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "الخطوة ${step.stepNumber}: ${step.titleAr}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MirathDarkBrown
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = step.detailAr,
                                    style = MaterialTheme.typography.bodySmall.copy(color = MirathDarkBrown)
                                )
                                if (step.tipAr != null) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "💡 نصيحة تكتيكية: ${step.tipAr}",
                                        style = MaterialTheme.typography.labelSmall.copy(color = MirathTerracottaDark)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "شرط الفوز: ${game.rules.winCondition}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MirathDarkBrown
                        )
                    )
                }
            }
        }

        // 6. Printable Board Specification (Schools & Families)
        if (game.printableBoard != null) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MirathWarmSurface.copy(alpha = 0.5f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MirathSand)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Print, contentDescription = null, tint = MirathTerracotta)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "اللوحة القابلة للطباعة (${game.printableBoard.titleAr})",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MirathDarkBrown
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "الأبعاد المناسبة: ${game.printableBoard.dimensionsAr}",
                            style = MaterialTheme.typography.bodySmall.copy(color = MirathMutedBrown)
                        )
                        Text(
                            text = "القطع المطلوبة: ${game.printableBoard.pieceRequirementsAr}",
                            style = MaterialTheme.typography.bodySmall.copy(color = MirathDarkBrown)
                        )
                        Text(
                            text = "التعليمات: ${game.printableBoard.instructionsAr}",
                            style = MaterialTheme.typography.bodySmall.copy(color = MirathMutedBrown)
                        )
                    }
                }
            }
        }

        // 7. Researcher Mode Section: Claims, References, Review Audit, Methodology
        if (isResearcherMode) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MirathCream),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MirathGoldAccent)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Science, contentDescription = null, tint = MirathGoldAccent)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ملف التحقيق والتوثيق الأكاديمي (Researcher Mode)",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MirathDarkBrown
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Origin Claims (disputed / documented)
                        Text(
                            text = "حالة الأصل الجغرافي: ${game.origin.status.name}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MirathTerracottaDark
                            )
                        )

                        if (game.origin.claims.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            game.origin.claims.forEach { claim ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MirathWarmSurface,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = claim.narrativeTitleAr,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = claim.narrativeSummaryAr,
                                            style = MaterialTheme.typography.bodySmall.copy(color = MirathDarkBrown)
                                        )
                                        Text(
                                            text = "إحالة المصدر: ${claim.sourceRefs.joinToString(", ")}",
                                            style = MaterialTheme.typography.labelSmall.copy(color = MirathMutedBrown)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Historical Claims
                        Text(
                            text = "الادعاءات التاريخية المحققة (Claims):",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        game.historicalContext.claims.forEach { c ->
                            Text(
                                text = "• ${c.textAr} [${c.sourceRefs.joinToString()}] (${c.status.name})",
                                style = MaterialTheme.typography.bodySmall.copy(color = MirathDarkBrown),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Sources
                        Text(
                            text = "قائمة المصادر والمراجع (${game.sources.size}):",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        game.sources.forEachIndexed { idx, s ->
                            Text(
                                text = "[${idx + 1}] ${s.label} — ${s.author ?: "مجهول"} (${s.year ?: "بدون تاريخ"}) — ${s.publisher ?: ""} (${s.type.name})",
                                style = MaterialTheme.typography.bodySmall.copy(color = MirathMutedBrown),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Review Status
                        Text(
                            text = "حالة التدقيق: ${game.review.status.name} | الإصدار: v${game.version}",
                            style = MaterialTheme.typography.labelSmall.copy(color = MirathMutedBrown)
                        )
                    }
                }
            }
        }

        // 8. Related Games with explicit reasons
        if (game.relatedGames.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MirathCream),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MirathWarmSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "ألعاب ذات صلة في كتالوج مِرث",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MirathDarkBrown
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        game.relatedGames.forEach { link ->
                            val relatedGame = MirathRepository.getGameBySlug(link.targetSlug)
                            if (relatedGame != null) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MirathWarmSurface.copy(alpha = 0.5f),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onNavigateToSlug(relatedGame.slug) }
                                        .padding(vertical = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = relatedGame.titleAr,
                                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = "سبب الصلة: ${link.reasonAr}",
                                                style = MaterialTheme.typography.labelSmall.copy(color = MirathMutedBrown)
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
    }
}
