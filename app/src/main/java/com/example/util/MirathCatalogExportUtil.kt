package com.example.util

import com.example.data.MirathRepository
import com.example.model.Game

/**
 * Portable JSON Serializer/Deserializer for Mirath.
 * Ensures the platform data can be fully exported or imported without lock-in.
 */
object MirathCatalogExportUtil {

    fun exportCatalogToJson(): String {
        val games = MirathRepository.games
        val countries = MirathRepository.countries
        val regions = MirathRepository.regions

        val sb = StringBuilder()
        sb.append("{\n")
        sb.append("  \"platform\": \"Mirath - Forgotten Games of the World\",\n")
        sb.append("  \"version\": \"1.0.0\",\n")
        sb.append("  \"exportedAt\": \"2026-10-07T11:00:00Z\",\n")
        sb.append("  \"gamesCount\": ${games.size},\n")

        // Games list
        sb.append("  \"games\": [\n")
        games.forEachIndexed { index, g ->
            sb.append("    {\n")
            sb.append("      \"id\": \"${g.id}\",\n")
            sb.append("      \"slug\": \"${g.slug}\",\n")
            sb.append("      \"titleAr\": \"${escape(g.titleAr)}\",\n")
            sb.append("      \"titleEn\": \"${escape(g.titleEn ?: "")}\",\n")
            sb.append("      \"countryId\": \"${g.origin.countryId}\",\n")
            sb.append("      \"regionId\": \"${g.origin.regionId}\",\n")
            sb.append("      \"rulesDifficulty\": ${g.rulesDifficulty},\n")
            sb.append("      \"masteryDifficulty\": ${g.masteryDifficulty},\n")
            sb.append("      \"primaryCategory\": \"${escape(g.primaryCategory)}\",\n")
            sb.append("      \"minPlayers\": ${g.players.min},\n")
            sb.append("      \"maxPlayers\": ${g.players.max},\n")
            sb.append("      \"playTime\": \"${g.estimatedPlayTime.minMinutes}-${g.estimatedPlayTime.maxMinutes} دقيقة\",\n")
            sb.append("      \"engine\": \"${g.playability.engine.name}\",\n")
            sb.append("      \"sourcesCount\": ${g.sources.size}\n")
            sb.append("    }${if (index < games.size - 1) "," else ""}\n")
        }
        sb.append("  ],\n")

        // Countries list
        sb.append("  \"countries\": [\n")
        countries.forEachIndexed { index, c ->
            sb.append("    {\n")
            sb.append("      \"id\": \"${c.id}\",\n")
            sb.append("      \"nameAr\": \"${escape(c.nameAr)}\",\n")
            sb.append("      \"regionId\": \"${c.regionId}\"\n")
            sb.append("    }${if (index < countries.size - 1) "," else ""}\n")
        }
        sb.append("  ]\n")
        sb.append("}\n")

        return sb.toString()
    }

    private fun escape(s: String): String {
        return s.replace("\"", "\\\"").replace("\n", " ")
    }
}
