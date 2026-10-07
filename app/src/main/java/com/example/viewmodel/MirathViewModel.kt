package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MirathRepository
import com.example.engine.*
import com.example.model.*
import com.example.util.ArabicSearchUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ScreenDestination {
    object Home : ScreenDestination()
    object Catalog : ScreenDestination()
    data class GameDetail(val slug: String) : ScreenDestination()
    data class PlayGame(val slug: String) : ScreenDestination()
    object MapRegions : ScreenDestination()
    data class CountryDetail(val countryId: String) : ScreenDestination()
    object GlossaryList : ScreenDestination()
    object AdminHub : ScreenDestination()
    object SuggestGame : ScreenDestination()
    data class RuleCorrectionScreen(val gameSlug: String) : ScreenDestination()
    object WebBrowserReplica : ScreenDestination()
}

enum class AudienceMode(val labelAr: String) {
    ALL("الكل"),
    FAMILIES("العائلات"),
    SCHOOLS("المدارس"),
    RESEARCHERS("الباحثون"),
    HISTORY_LOVERS("محبو التاريخ")
}

data class MirathUiState(
    val currentScreen: ScreenDestination = ScreenDestination.Home,
    val selectedAudience: AudienceMode = AudienceMode.ALL,
    val isResearcherModeEnabled: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val selectedRegion: String? = null,
    val selectedRulesDifficulty: Int? = null,
    val favoritesSlugs: Set<String> = emptySet(),
    val recentlyPlayedSlugs: List<String> = emptyList(),
    // Submissions in memory (portable)
    val suggestions: List<Suggestion> = emptyList(),
    val ruleCorrections: List<RuleCorrection> = emptyList(),
    // Active interactive engine states
    val alignmentState: AlignmentState = AlignmentEngine(3, 3).getInitialState(),
    val mancalaState: MancalaState = MancalaEngine(4).getInitialState(),
    val diceTrackState: DiceTrackState = DiceTrackEngine(14, 4).getInitialState(),
    val playMode: String = "local", // local, ai, tutorial
    val aiDifficulty: String = "intermediate",
    val tutorialStepIndex: Int = 0,
    val isDarkModeEnabled: Boolean = false,
    val snackbarMessage: String? = null
)

class MirathViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MirathUiState())
    val uiState: StateFlow<MirathUiState> = _uiState.asStateFlow()

    private val alignmentEngine = AlignmentEngine(3, 3)
    private val mancalaEngine = MancalaEngine(4)
    private val diceTrackEngine = DiceTrackEngine(14, 4)

    private val screenBackStack = mutableListOf<ScreenDestination>()

    fun navigateTo(destination: ScreenDestination) {
        val current = _uiState.value.currentScreen
        if (current != destination) {
            screenBackStack.add(current)
            _uiState.value = _uiState.value.copy(currentScreen = destination)
        }
    }

    fun navigateBack(): Boolean {
        if (screenBackStack.isNotEmpty()) {
            val previous = screenBackStack.removeAt(screenBackStack.size - 1)
            _uiState.value = _uiState.value.copy(currentScreen = previous)
            return true
        }
        if (_uiState.value.currentScreen != ScreenDestination.Home) {
            _uiState.value = _uiState.value.copy(currentScreen = ScreenDestination.Home)
            return true
        }
        return false
    }

    fun setAudience(audience: AudienceMode) {
        val enableResearcher = (audience == AudienceMode.RESEARCHERS)
        _uiState.value = _uiState.value.copy(
            selectedAudience = audience,
            isResearcherModeEnabled = enableResearcher
        )
    }

    fun toggleResearcherMode() {
        val toggled = !_uiState.value.isResearcherModeEnabled
        _uiState.value = _uiState.value.copy(
            isResearcherModeEnabled = toggled,
            selectedAudience = if (toggled) AudienceMode.RESEARCHERS else AudienceMode.ALL
        )
    }

    fun toggleDarkMode() {
        val newMode = !_uiState.value.isDarkModeEnabled
        _uiState.value = _uiState.value.copy(
            isDarkModeEnabled = newMode,
            snackbarMessage = if (newMode) "تم تفعيل الوضع الليلي (المخطوطات القديمة)" else "تم تفعيل الوضع النهاري"
        )
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun setFilterCategory(category: String?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun setFilterRegion(regionId: String?) {
        _uiState.value = _uiState.value.copy(selectedRegion = regionId)
    }

    fun setFilterRulesDifficulty(diff: Int?) {
        _uiState.value = _uiState.value.copy(selectedRulesDifficulty = diff)
    }

    fun toggleFavorite(slug: String) {
        val current = _uiState.value.favoritesSlugs.toMutableSet()
        if (current.contains(slug)) current.remove(slug) else current.add(slug)
        _uiState.value = _uiState.value.copy(favoritesSlugs = current)
    }

    fun surpriseMe() {
        val candidates = MirathRepository.games.filter {
            !_uiState.value.recentlyPlayedSlugs.contains(it.slug)
        }.ifEmpty { MirathRepository.games }

        val picked = candidates.randomOrNull() ?: MirathRepository.games.first()
        navigateTo(ScreenDestination.GameDetail(picked.slug))
    }

    // Engine Interactions
    fun startPlay(slug: String, mode: String = "local") {
        val updatedRecent = (listOf(slug) + _uiState.value.recentlyPlayedSlugs.filter { it != slug }).take(10)
        _uiState.value = _uiState.value.copy(
            playMode = mode,
            recentlyPlayedSlugs = updatedRecent,
            tutorialStepIndex = 0
        )
        // Reset engines
        when (slug) {
            "royal-game-of-ur" -> _uiState.value = _uiState.value.copy(diceTrackState = diceTrackEngine.getInitialState())
            "oware" -> _uiState.value = _uiState.value.copy(mancalaState = mancalaEngine.getInitialState())
            "alquerque", "shisima" -> _uiState.value = _uiState.value.copy(alignmentState = alignmentEngine.getInitialState())
        }
        navigateTo(ScreenDestination.PlayGame(slug))
    }

    // Alignment / Shisima Move
    fun onAlignmentCellClick(index: Int) {
        val state = _uiState.value.alignmentState
        if (state.winner != null) return

        if (state.phase == 1) {
            // Placement phase
            val nextState = alignmentEngine.applyMove(state, AlignmentMove.PlacePiece(index))
            _uiState.value = _uiState.value.copy(alignmentState = nextState)
            triggerAiIfNeededForAlignment(nextState)
        } else {
            // Movement phase
            if (state.selectedIndex == null) {
                if (state.board[index] == state.currentTurn) {
                    _uiState.value = _uiState.value.copy(
                        alignmentState = state.copy(selectedIndex = index)
                    )
                }
            } else {
                val from = state.selectedIndex
                if (from == index) {
                    // Deselect
                    _uiState.value = _uiState.value.copy(alignmentState = state.copy(selectedIndex = null))
                } else {
                    val nextState = alignmentEngine.applyMove(state, AlignmentMove.MovePiece(from, index))
                    _uiState.value = _uiState.value.copy(alignmentState = nextState)
                    triggerAiIfNeededForAlignment(nextState)
                }
            }
        }
    }

    private fun triggerAiIfNeededForAlignment(state: AlignmentState) {
        if (_uiState.value.playMode != "ai" || state.winner != null) return
        if (state.currentTurn == AlignmentPlayer.PLAYER_TWO) {
            viewModelScope.launch {
                kotlinx.coroutines.delay(400)
                val legal = alignmentEngine.getLegalMoves(state)
                if (legal.isNotEmpty()) {
                    val chosen = legal.random()
                    val afterAi = alignmentEngine.applyMove(state, chosen)
                    _uiState.value = _uiState.value.copy(alignmentState = afterAi)
                }
            }
        }
    }

    // Mancala Move
    fun onMancalaPitClick(pitIndex: Int) {
        val state = _uiState.value.mancalaState
        if (state.isGameOver) return
        val next = mancalaEngine.applyMove(state, MancalaMove(pitIndex))
        _uiState.value = _uiState.value.copy(mancalaState = next)

        if (_uiState.value.playMode == "ai" && !next.isGameOver && next.currentTurnPlayer == 2) {
            viewModelScope.launch {
                kotlinx.coroutines.delay(500)
                val legal = mancalaEngine.getLegalMoves(next)
                if (legal.isNotEmpty()) {
                    val aiPit = legal.random().pitIndex
                    val afterAi = mancalaEngine.applyMove(next, MancalaMove(aiPit))
                    _uiState.value = _uiState.value.copy(mancalaState = afterAi)
                }
            }
        }
    }

    // Dice Track (Ur) Move
    fun onDiceTrackRoll() {
        val state = _uiState.value.diceTrackState
        if (state.currentDiceRoll != null || state.winner != null) return
        val next = diceTrackEngine.applyMove(state, DiceTrackMove.RollDice)
        _uiState.value = _uiState.value.copy(diceTrackState = next)

        if (_uiState.value.playMode == "ai" && next.currentTurnPlayer == 2 && next.currentDiceRoll != null) {
            viewModelScope.launch {
                kotlinx.coroutines.delay(600)
                val legal = diceTrackEngine.getLegalMoves(next).filterIsInstance<DiceTrackMove.AdvancePiece>()
                if (legal.isNotEmpty()) {
                    val chosen = legal.random()
                    val afterAi = diceTrackEngine.applyMove(next, chosen)
                    _uiState.value = _uiState.value.copy(diceTrackState = afterAi)
                }
            }
        }
    }

    fun onDiceTrackAdvance(pieceIndex: Int) {
        val state = _uiState.value.diceTrackState
        if (state.currentDiceRoll == null || state.winner != null) return
        val next = diceTrackEngine.applyMove(state, DiceTrackMove.AdvancePiece(pieceIndex))
        _uiState.value = _uiState.value.copy(diceTrackState = next)

        if (_uiState.value.playMode == "ai" && !diceTrackEngine.isGameOver(next) && next.currentTurnPlayer == 2) {
            viewModelScope.launch {
                kotlinx.coroutines.delay(500)
                val rollState = diceTrackEngine.applyMove(next, DiceTrackMove.RollDice)
                _uiState.value = _uiState.value.copy(diceTrackState = rollState)
                if (rollState.currentDiceRoll != null && rollState.currentDiceRoll > 0) {
                    kotlinx.coroutines.delay(400)
                    val legal = diceTrackEngine.getLegalMoves(rollState).filterIsInstance<DiceTrackMove.AdvancePiece>()
                    if (legal.isNotEmpty()) {
                        val pieceMove = legal.random()
                        val finalState = diceTrackEngine.applyMove(rollState, pieceMove)
                        _uiState.value = _uiState.value.copy(diceTrackState = finalState)
                    }
                }
            }
        }
    }

    fun restartGame(slug: String) {
        startPlay(slug, _uiState.value.playMode)
    }

    fun setTutorialStep(index: Int) {
        _uiState.value = _uiState.value.copy(tutorialStepIndex = index)
    }

    // Community Submissions
    fun submitSuggestion(title: String, country: String, desc: String, sourceUrl: String?, submitter: String?) {
        val newSugg = Suggestion(
            id = "sugg_${System.currentTimeMillis()}",
            titleAr = title,
            countryAr = country,
            descriptionAr = desc,
            sourceUrl = sourceUrl,
            submitterName = submitter,
            date = "2026-10-07"
        )
        _uiState.value = _uiState.value.copy(
            suggestions = listOf(newSugg) + _uiState.value.suggestions,
            snackbarMessage = "تم إرسال اقتراحك لمراجعة المحتوى والتحقق التاريخي بنجاح!"
        )
        navigateBack()
    }

    fun submitRuleCorrection(slug: String, section: String, text: String, source: String?) {
        val correction = RuleCorrection(
            id = "corr_${System.currentTimeMillis()}",
            gameSlug = slug,
            sectionAr = section,
            proposedCorrectionAr = text,
            sourceCitationAr = source,
            date = "2026-10-07"
        )
        _uiState.value = _uiState.value.copy(
            ruleCorrections = listOf(correction) + _uiState.value.ruleCorrections,
            snackbarMessage = "شكراً لمساهمتك! سيتم فحص التدقيق مع المصادر المعتمدة."
        )
        navigateBack()
    }

    fun clearSnackbar() {
        _uiState.value = _uiState.value.copy(snackbarMessage = null)
    }

    // Filtered games
    fun getFilteredGames(): List<Game> {
        val state = _uiState.value
        return MirathRepository.games.filter { game ->
            // Audience filter
            val matchesAudience = when (state.selectedAudience) {
                AudienceMode.ALL -> true
                AudienceMode.FAMILIES -> game.audienceSuitability.contains("families")
                AudienceMode.SCHOOLS -> game.audienceSuitability.contains("schools")
                AudienceMode.RESEARCHERS -> game.audienceSuitability.contains("researchers")
                AudienceMode.HISTORY_LOVERS -> game.audienceSuitability.contains("history_lovers")
            }

            // Category
            val matchesCategory = state.selectedCategory == null || game.primaryCategory == state.selectedCategory

            // Region
            val matchesRegion = state.selectedRegion == null || game.origin.regionId == state.selectedRegion

            // Rules Difficulty
            val matchesRulesDiff = state.selectedRulesDifficulty == null || game.rulesDifficulty == state.selectedRulesDifficulty

            // Search by Name, Region, or Genre/Type with instant matching
            val matchesSearch = if (state.searchQuery.isBlank()) true else {
                val country = MirathRepository.getCountryById(game.origin.countryId)
                val region = MirathRepository.getRegionById(game.origin.regionId)
                ArabicSearchUtil.matchesGame(
                    query = state.searchQuery,
                    titleAr = game.titleAr,
                    titleEn = game.titleEn,
                    originalTitle = game.originalTitle,
                    aliases = game.aliases,
                    primaryCategory = game.primaryCategory,
                    secondaryCategories = game.secondaryCategories,
                    tags = game.tags,
                    playStyle = game.playStyle,
                    culture = game.origin.culture,
                    countryNameAr = country?.nameAr,
                    countryNameEn = country?.nameEn,
                    regionNameAr = region?.nameAr,
                    regionNameEn = region?.nameEn,
                    summaryAr = game.historicalContext.summaryAr
                )
            }

            matchesAudience && matchesCategory && matchesRegion && matchesRulesDiff && matchesSearch
        }
    }
}
