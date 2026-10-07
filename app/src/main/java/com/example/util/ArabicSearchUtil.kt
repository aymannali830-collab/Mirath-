package com.example.util

import java.text.Normalizer

/**
 * Arabic-aware text normalization & search utilities.
 * Handles:
 * - Tashkeel (diacritics: Fatha, Damma, Kasra, Sukun, Tanween, Shadda)
 * - Tatweel / Kashida (ـ)
 * - Alef normalization (أ, إ, آ, ٱ -> ا)
 * - Taa Marbuta / Haa (ة -> ه)
 * - Yaa / Alef Maksura (ى -> ي)
 * - Case-folding for Latin characters
 */
object ArabicSearchUtil {

    private val TASHKEEL_REGEX = Regex("[\u064B-\u065F\u0670]")
    private val TATWEEL_REGEX = Regex("\u0640")

    fun normalize(text: String?): String {
        if (text.isNullOrBlank()) return ""
        var normalized = text.trim().lowercase()

        // Remove diacritics & tatweel
        normalized = TASHKEEL_REGEX.replace(normalized, "")
        normalized = TATWEEL_REGEX.replace(normalized, "")

        // Normalize Alefs
        normalized = normalized.replace('أ', 'ا')
            .replace('إ', 'ا')
            .replace('آ', 'ا')
            .replace('ٱ', 'ا')

        // Normalize Taa Marbuta
        normalized = normalized.replace('ة', 'ه')

        // Normalize Alef Maksura / Yaa
        normalized = normalized.replace('ى', 'ي')

        return normalized
    }

    /**
     * Scores how well a game matches a query:
     * exact = 100, prefix = 60, contains = 40, alias match = 30, tag = 20, description = 10
     */
    fun matchScore(
        query: String,
        titleAr: String,
        titleEn: String?,
        aliases: List<String>,
        tags: List<String>,
        whatMakesItDifferentAr: String
    ): Int {
        val normQ = normalize(query)
        if (normQ.isEmpty()) return 10

        val normTitleAr = normalize(titleAr)
        val normTitleEn = normalize(titleEn ?: "")

        if (normTitleAr == normQ || normTitleEn == normQ) return 100
        if (normTitleAr.startsWith(normQ) || normTitleEn.startsWith(normQ)) return 60
        if (normTitleAr.contains(normQ) || normTitleEn.contains(normQ)) return 40

        for (alias in aliases) {
            val normAlias = normalize(alias)
            if (normAlias == normQ) return 50
            if (normAlias.contains(normQ)) return 30
        }

        for (tag in tags) {
            val normTag = normalize(tag)
            if (normTag.contains(normQ)) return 20
        }

        if (normalize(whatMakesItDifferentAr).contains(normQ)) return 15

        return 0
    }

    /**
     * Checks if a game matches a query by name, region/country, or type/genre.
     * Instant fuzzy matching with Arabic normalization.
     */
    fun matchesGame(
        query: String,
        titleAr: String,
        titleEn: String?,
        originalTitle: String?,
        aliases: List<String>,
        primaryCategory: String,
        secondaryCategories: List<String>,
        tags: List<String>,
        playStyle: List<String>,
        culture: String?,
        countryNameAr: String?,
        countryNameEn: String?,
        regionNameAr: String?,
        regionNameEn: String?,
        summaryAr: String? = null
    ): Boolean {
        val normQ = normalize(query)
        if (normQ.isBlank()) return true

        // Split query into tokens for multi-term matching (e.g. "ألعاب العراق" or "سباق نرد")
        val tokens = normQ.split(" ").filter { it.isNotBlank() }

        // Compile searchable strings
        val searchableTexts = mutableListOf<String>()
        searchableTexts.add(normalize(titleAr))
        if (!titleEn.isNullOrBlank()) searchableTexts.add(normalize(titleEn))
        if (!originalTitle.isNullOrBlank()) searchableTexts.add(normalize(originalTitle))
        aliases.forEach { searchableTexts.add(normalize(it)) }

        // Genre / Type
        searchableTexts.add(normalize(primaryCategory))
        secondaryCategories.forEach { searchableTexts.add(normalize(it)) }
        tags.forEach { searchableTexts.add(normalize(it)) }
        playStyle.forEach { searchableTexts.add(normalize(it)) }

        // Region / Origin
        if (!culture.isNullOrBlank()) searchableTexts.add(normalize(culture))
        if (!countryNameAr.isNullOrBlank()) searchableTexts.add(normalize(countryNameAr))
        if (!countryNameEn.isNullOrBlank()) searchableTexts.add(normalize(countryNameEn))
        if (!regionNameAr.isNullOrBlank()) searchableTexts.add(normalize(regionNameAr))
        if (!regionNameEn.isNullOrBlank()) searchableTexts.add(normalize(regionNameEn))
        if (!summaryAr.isNullOrBlank()) searchableTexts.add(normalize(summaryAr))

        // Check if every token matches at least one searchable text
        return tokens.all { token ->
            searchableTexts.any { it.contains(token) }
        }
    }
}
