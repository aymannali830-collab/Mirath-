package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
import com.example.data.MirathRepository
import com.example.engine.AlignmentPlayer
import com.example.ui.theme.*
import com.example.viewmodel.MirathUiState
import com.example.viewmodel.MirathViewModel

@Composable
fun PlayGameScreen(
    slug: String,
    uiState: MirathUiState,
    viewModel: MirathViewModel,
    onBack: () -> Unit
) {
    val game = MirathRepository.getGameBySlug(slug)

    if (game == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("اللعبة غير موجودة")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MirathIvory)
            .padding(16.dp)
    ) {
        // Mode Switcher: Local Pass & Play vs Play with AI vs Tutorial
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = uiState.playMode == "local",
                onClick = { viewModel.startPlay(slug, "local") },
                label = { Text("لعب محلي (لاعبان)") },
                leadingIcon = { Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(16.dp)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MirathTerracotta,
                    selectedLabelColor = Color.White
                )
            )

            FilterChip(
                selected = uiState.playMode == "ai",
                onClick = { viewModel.startPlay(slug, "ai") },
                label = { Text("ضد الذكاء الاصطناعي") },
                leadingIcon = { Icon(Icons.Default.SmartToy, contentDescription = null, modifier = Modifier.size(16.dp)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MirathBurntOrange,
                    selectedLabelColor = Color.White
                )
            )

            FilterChip(
                selected = uiState.playMode == "tutorial",
                onClick = { viewModel.startPlay(slug, "tutorial") },
                label = { Text("تدريب تفاعلي") },
                leadingIcon = { Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(16.dp)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MirathGoldAccent,
                    selectedLabelColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Game Play Surface based on game slug / engine
        when (slug) {
            "royal-game-of-ur" -> {
                RoyalGameOfUrBoard(
                    uiState = uiState,
                    onRollDice = { viewModel.onDiceTrackRoll() },
                    onAdvance = { idx -> viewModel.onDiceTrackAdvance(idx) },
                    onRestart = { viewModel.restartGame(slug) }
                )
            }
            "oware" -> {
                OwareBoard(
                    uiState = uiState,
                    onPitClick = { idx -> viewModel.onMancalaPitClick(idx) },
                    onRestart = { viewModel.restartGame(slug) }
                )
            }
            "shisima", "alquerque" -> {
                AlignmentBoard(
                    slug = slug,
                    uiState = uiState,
                    onCellClick = { idx -> viewModel.onAlignmentCellClick(idx) },
                    onRestart = { viewModel.restartGame(slug) }
                )
            }
            else -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("المحرك قيد الإعداد لهذه اللعبة.")
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Interactive Royal Game of Ur
// -------------------------------------------------------------
@Composable
fun RoyalGameOfUrBoard(
    uiState: MirathUiState,
    onRollDice: () -> Unit,
    onAdvance: (Int) -> Unit,
    onRestart: () -> Unit
) {
    val state = uiState.diceTrackState
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MirathCream),
        border = androidx.compose.foundation.BorderStroke(1.dp, MirathWarmSurface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "لعبة أور الملكية (Royal Game of Ur)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MirathDarkBrown
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = state.messageAr,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MirathTerracottaDark,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dice Roller
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onRollDice,
                    enabled = state.currentDiceRoll == null && state.winner == null,
                    colors = ButtonDefaults.buttonColors(containerColor = MirathTerracotta),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("roll_ur_dice_btn")
                ) {
                    Icon(Icons.Default.Casino, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("ارمِ النرد الهرمي (4x)")
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MirathWarmSurface,
                    modifier = Modifier.size(48.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MirathSand)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = state.currentDiceRoll?.toString() ?: "-",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MirathDarkBrown
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Player 1 Pieces (Terracotta)
            Text(
                text = "قطع اللاعب 1 (التيراكوتا)",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = MirathTerracottaDark)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                state.p1Positions.forEachIndexed { idx, pos ->
                    val isBorneOff = pos > state.trackLength
                    Surface(
                        shape = CircleShape,
                        color = if (isBorneOff) Color(0xFF2E7D32) else MirathTerracotta,
                        modifier = Modifier
                            .size(44.dp)
                            .clickable(enabled = state.currentTurnPlayer == 1 && state.currentDiceRoll != null && !isBorneOff) {
                                onAdvance(idx)
                            },
                        border = androidx.compose.foundation.BorderStroke(2.dp, MirathDarkBrown)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = if (isBorneOff) "تم" else if (pos == 0) "بدء" else "خ$pos",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Shared Track Visual Representation (Rosette at 8, 4, 14)
            Text(
                text = "المسار الأوسط المشترك (خانات 5 إلى 12):",
                style = MaterialTheme.typography.labelSmall.copy(color = MirathMutedBrown)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                (5..12).forEach { cellIdx ->
                    val isRosette = cellIdx == 8
                    val hasP1 = state.p1Positions.contains(cellIdx)
                    val hasP2 = state.p2Positions.contains(cellIdx)
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (isRosette) MirathGoldAccent.copy(alpha = 0.3f) else MirathWarmSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isRosette) MirathGoldAccent else MirathSand),
                        modifier = Modifier
                            .padding(2.dp)
                            .size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            when {
                                hasP1 -> Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(MirathTerracotta))
                                hasP2 -> Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(MirathDarkBrown))
                                isRosette -> Text("🌸", fontSize = 12.sp)
                                else -> Text("$cellIdx", fontSize = 10.sp, color = MirathMutedBrown)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Player 2 Pieces (Dark Brown)
            Text(
                text = "قطع اللاعب 2 (البني الداكن)",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = MirathDarkBrown)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                state.p2Positions.forEachIndexed { idx, pos ->
                    val isBorneOff = pos > state.trackLength
                    Surface(
                        shape = CircleShape,
                        color = if (isBorneOff) Color(0xFF2E7D32) else MirathDarkBrown,
                        modifier = Modifier
                            .size(44.dp)
                            .clickable(enabled = state.currentTurnPlayer == 2 && state.currentDiceRoll != null && !isBorneOff && uiState.playMode != "ai") {
                                onAdvance(idx)
                            },
                        border = androidx.compose.foundation.BorderStroke(2.dp, MirathCream)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = if (isBorneOff) "تم" else if (pos == 0) "بدء" else "خ$pos",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            IconButton(onClick = onRestart) {
                Icon(Icons.Default.Refresh, contentDescription = "إعادة اللعبة", tint = MirathTerracotta)
            }
        }
    }
}

// -------------------------------------------------------------
// Interactive Mancala / Oware Board
// -------------------------------------------------------------
@Composable
fun OwareBoard(
    uiState: MirathUiState,
    onPitClick: (Int) -> Unit,
    onRestart: () -> Unit
) {
    val state = uiState.mancalaState
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MirathCream),
        border = androidx.compose.foundation.BorderStroke(1.dp, MirathWarmSurface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "لعبة أواري (Oware Mancala)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MirathDarkBrown
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = state.messageAr,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MirathTerracottaDark,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Player 2 Pits (indices 12 down to 7)
            Text(text = "صف اللاعب 2", style = MaterialTheme.typography.labelSmall.copy(color = MirathMutedBrown))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                (12 downTo 7).forEach { pitIdx ->
                    val count = state.pits[pitIdx]
                    MancalaPitView(
                        pitIndex = pitIdx,
                        count = count,
                        isClickable = state.currentTurnPlayer == 2 && uiState.playMode != "ai" && count > 0,
                        onClick = { onPitClick(pitIdx) },
                        playerColor = MirathDarkBrown
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stores (Player 2 Store index 13, Player 1 Store index 6)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MirathDarkBrown,
                    modifier = Modifier.size(width = 60.dp, height = 48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "م2: ${state.pits[13]}",
                            style = MaterialTheme.typography.labelMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Text(text = "← اتجاه البذر عكس عقارب الساعة →", style = MaterialTheme.typography.labelSmall.copy(color = MirathMutedBrown))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MirathTerracotta,
                    modifier = Modifier.size(width = 60.dp, height = 48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "م1: ${state.pits[6]}",
                            style = MaterialTheme.typography.labelMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Player 1 Pits (indices 0 to 5)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                (0..5).forEach { pitIdx ->
                    val count = state.pits[pitIdx]
                    MancalaPitView(
                        pitIndex = pitIdx,
                        count = count,
                        isClickable = state.currentTurnPlayer == 1 && count > 0,
                        onClick = { onPitClick(pitIdx) },
                        playerColor = MirathTerracotta
                    )
                }
            }
            Text(text = "صف اللاعب 1", style = MaterialTheme.typography.labelSmall.copy(color = MirathMutedBrown))

            Spacer(modifier = Modifier.height(16.dp))

            IconButton(onClick = onRestart) {
                Icon(Icons.Default.Refresh, contentDescription = "إعادة", tint = MirathTerracotta)
            }
        }
    }
}

@Composable
fun MancalaPitView(
    pitIndex: Int,
    count: Int,
    isClickable: Boolean,
    onClick: () -> Unit,
    playerColor: Color
) {
    Surface(
        shape = CircleShape,
        color = if (isClickable) MirathWarmSurface else MirathCream,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, playerColor),
        modifier = Modifier
            .size(46.dp)
            .clickable(enabled = isClickable) { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "$count",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MirathDarkBrown
                )
            )
        }
    }
}

// -------------------------------------------------------------
// Interactive Alignment Board (Shisima / Alquerque)
// -------------------------------------------------------------
@Composable
fun AlignmentBoard(
    slug: String,
    uiState: MirathUiState,
    onCellClick: (Int) -> Unit,
    onRestart: () -> Unit
) {
    val state = uiState.alignmentState
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MirathCream),
        border = androidx.compose.foundation.BorderStroke(1.dp, MirathWarmSurface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (slug == "shisima") "لعبة شيسيما الكينية (Shisima)" else "لعبة القِرْق الأندلسي (Alquerque)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MirathDarkBrown
                )
            )
            Spacer(modifier = Modifier.height(4.dp))

            val statusText = when {
                state.winner == AlignmentPlayer.PLAYER_ONE -> "فاز اللاعب 1 (أحجار التيراكوتا)!"
                state.winner == AlignmentPlayer.PLAYER_TWO -> "فاز اللاعب 2 (أحجار الكريمة الذهبية)!"
                state.phase == 1 -> "مرحلة التنزيل: ضع قطعك الثلاث على الرقعة (دور اللاعب ${if (state.currentTurn == AlignmentPlayer.PLAYER_ONE) "1" else "2"})"
                else -> "مرحلة الحركة: حرّك حجرك نحو نقطة مجاورة (دور اللاعب ${if (state.currentTurn == AlignmentPlayer.PLAYER_ONE) "1" else "2"})"
            }

            Text(
                text = statusText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MirathTerracottaDark,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3x3 Grid
            Column(
                modifier = Modifier
                    .size(240.dp)
                    .background(MirathWarmSurface, RoundedCornerShape(12.dp))
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                for (row in 0 until 3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (col in 0 until 3) {
                            val idx = row * 3 + col
                            val piece = state.board[idx]
                            val isSelected = state.selectedIndex == idx

                            Surface(
                                shape = CircleShape,
                                color = when (piece) {
                                    AlignmentPlayer.PLAYER_ONE -> MirathTerracotta
                                    AlignmentPlayer.PLAYER_TWO -> MirathGoldAccent
                                    AlignmentPlayer.NONE -> if (idx == 4) MirathSand else MirathCream
                                },
                                border = androidx.compose.foundation.BorderStroke(
                                    if (isSelected) 3.dp else 1.dp,
                                    if (isSelected) Color.Red else MirathDarkBrown
                                ),
                                modifier = Modifier
                                    .size(56.dp)
                                    .clickable { onCellClick(idx) }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    if (idx == 4 && piece == AlignmentPlayer.NONE) {
                                        Text("المركز", fontSize = 10.sp, color = MirathMutedBrown)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            IconButton(onClick = onRestart) {
                Icon(Icons.Default.Refresh, contentDescription = "إعادة", tint = MirathTerracotta)
            }
        }
    }
}
