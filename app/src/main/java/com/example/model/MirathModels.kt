package com.example.model

/**
 * Mirath Core Specification Models
 * Structured, portable, JSON-exportable model for historical and traditional games.
 */

enum class OriginStatus {
    documented,
    disputed,
    uncertain
}

enum class ClaimStatus {
    supported,
    disputed,
    uncertain
}

enum class SourceType {
    book,
    academic,
    museum,
    archive,
    official,
    reputable_web,
    other
}

enum class GameStatus {
    draft,
    under_review,
    ready,
    published,
    archived
}

enum class PlayabilityEngine {
    move_engine,
    mancala_engine,
    alignment_engine,
    card_trick_engine,
    dice_engine,
    word_engine,
    dexterity_engine,
    quiz_engine,
    custom
}

enum class ReviewStatus {
    unreviewed,
    reviewed,
    verified,
    disputed
}

data class Source(
    val sourceId: String,
    val label: String,
    val author: String? = null,
    val year: Int? = null,
    val publisher: String? = null,
    val url: String? = null,
    val type: SourceType = SourceType.book,
    val accessedAt: String? = null
)

data class ClaimBlock(
    val claimId: String,
    val textAr: String,
    val sourceRefs: List<String> = emptyList(),
    val status: ClaimStatus = ClaimStatus.supported
)

data class DisputedClaim(
    val narrativeTitleAr: String,
    val narrativeSummaryAr: String,
    val sourceRefs: List<String> = emptyList()
)

data class GameOrigin(
    val countryId: String,
    val regionId: String,
    val culture: String? = null,
    val status: OriginStatus = OriginStatus.documented,
    val claims: List<DisputedClaim> = emptyList()
)

data class HistoricalContext(
    val summaryAr: String? = null,
    val era: String? = null,
    val notesAr: String? = null,
    val claims: List<ClaimBlock> = emptyList()
)

data class PlayerSpecs(
    val min: Int,
    val max: Int,
    val teamBased: Boolean = false
)

data class PlayTimeSpecs(
    val minMinutes: Int,
    val maxMinutes: Int
)

data class RuleStep(
    val stepNumber: Int,
    val titleAr: String,
    val detailAr: String,
    val tipAr: String? = null
)

data class RuleVariant(
    val nameAr: String,
    val descriptionAr: String,
    val regionOrEraAr: String? = null
)

data class GameRules(
    val overviewAr: String,
    val steps: List<RuleStep>,
    val winCondition: String,
    val drawCondition: String? = null,
    val variants: List<RuleVariant> = emptyList()
)

data class TutorialStep(
    val stepIndex: Int,
    val titleAr: String,
    val explanationAr: String,
    val interactiveHintAr: String? = null
)

data class TutorialModes(
    val quick: List<TutorialStep> = emptyList(),
    val learn: List<TutorialStep> = emptyList(),
    val practice: List<TutorialStep> = emptyList()
)

data class AiModeConfig(
    val available: Boolean,
    val difficulties: List<String> = listOf("beginner", "intermediate", "expert")
)

data class GameModes(
    val learning: Boolean = true,
    val ai: AiModeConfig = AiModeConfig(available = true),
    val localMultiplayer: Boolean = true,
    val remoteMultiplayer: Boolean = false
)

data class ImageAttribution(
    val source: String,
    val license: String,
    val creator: String,
    val attributionText: String,
    val url: String? = null,
    val status: String = "documented" // or "missing"
)

data class Credit(
    val roleAr: String,
    val nameAr: String
)

data class VersionRecord(
    val version: String,
    val changedAt: String,
    val changedBy: String,
    val notesAr: String
)

data class ReviewRecord(
    val status: ReviewStatus = ReviewStatus.verified,
    val reviewedBy: String? = null,
    val reviewedAt: String? = null,
    val notesAr: String? = null
)

data class PlayabilityConfig(
    val engine: PlayabilityEngine,
    val status: String = "custom", // custom, templated, simplified
    val fidelity: String = "faithful", // faithful, simplified
    val playableRoute: String,
    val fallbackEngine: String? = null,
    val ai: Boolean = true,
    val local: Boolean = true,
    val remote: Boolean = false,
    val tutorial: Boolean = true,
    val notes: String? = null
)

data class RelatedGameLink(
    val targetSlug: String,
    val reasonAr: String
)

data class PrintableBoardSpec(
    val titleAr: String,
    val dimensionsAr: String,
    val instructionsAr: String,
    val pieceRequirementsAr: String
)

data class Game(
    val id: String,
    val slug: String,
    val titleAr: String,
    val titleEn: String? = null,
    val originalTitle: String? = null,
    val aliases: List<String> = emptyList(),
    val origin: GameOrigin,
    val historicalContext: HistoricalContext,
    val players: PlayerSpecs,
    val estimatedPlayTime: PlayTimeSpecs,
    val timeToLearnMinutes: Int,
    val rulesDifficulty: Int, // 1 to 5
    val masteryDifficulty: Int, // 1 to 5
    val primaryCategory: String,
    val secondaryCategories: List<String> = emptyList(),
    val playStyle: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val whatMakesItDifferentAr: String,
    val whyThisGameAr: String,
    val modes: GameModes = GameModes(),
    val rules: GameRules,
    val tutorial: TutorialModes = TutorialModes(),
    val playability: PlayabilityConfig,
    val sources: List<Source> = emptyList(),
    val imageAttribution: List<ImageAttribution> = emptyList(),
    val credits: List<Credit> = emptyList(),
    val review: ReviewRecord = ReviewRecord(),
    val version: String = "1.0",
    val versionHistory: List<VersionRecord> = emptyList(),
    val status: GameStatus = GameStatus.published,
    val relatedGames: List<RelatedGameLink> = emptyList(),
    val printableBoard: PrintableBoardSpec? = null,
    val audienceSuitability: List<String> = listOf("families", "schools", "researchers", "history_lovers"),
    val createdAt: String = "2026-01-01",
    val updatedAt: String = "2026-10-07"
)

data class Country(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val regionId: String,
    val summaryAr: String,
    val historicalGamingCultureAr: String
)

data class Region(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val summaryAr: String
)

data class GlossaryTerm(
    val id: String,
    val slug: String,
    val termAr: String,
    val definitionAr: String,
    val originLanguageAr: String? = null,
    val relatedGamesSlugs: List<String> = emptyList()
)

data class Suggestion(
    val id: String,
    val titleAr: String,
    val countryAr: String,
    val descriptionAr: String,
    val sourceUrl: String?,
    val submitterName: String?,
    val date: String
)

data class RuleCorrection(
    val id: String,
    val gameSlug: String,
    val sectionAr: String,
    val proposedCorrectionAr: String,
    val sourceCitationAr: String?,
    val date: String
)
