package com.srbcards.data.repository

import com.srbcards.data.local.VocabularyDao
import com.srbcards.data.local.VocabularyWord

/**
 * Single source of truth for vocabulary data. Wraps [VocabularyDao] and exposes
 * suspend functions for the ViewModel layer, keeping all Room details below this boundary.
 */
class WordRepository(private val dao: VocabularyDao) {

    // ── Full-dataset ──────────────────────────────────────────────────────────

    suspend fun getRandomWords(limit: Int = 20): List<VocabularyWord> =
        dao.getRandomWords(limit)

    suspend fun getRandomNouns(limit: Int = 20): List<VocabularyWord> =
        dao.getRandomNouns(limit)

    suspend fun getRandomNounsWithPlural(limit: Int = 20): List<VocabularyWord> =
        dao.getRandomNounsWithPlural(limit)

    // ── Category-filtered ─────────────────────────────────────────────────────

    suspend fun getRandomByCategory(category: String, limit: Int = 20): List<VocabularyWord> =
        dao.getRandomByCategory(category, limit)

    suspend fun getRandomNounsByCategory(category: String, limit: Int = 20): List<VocabularyWord> =
        dao.getRandomNounsByCategory(category, limit)

    suspend fun getRandomNounsWithPluralByCategory(
        category: String,
        limit: Int = 20
    ): List<VocabularyWord> = dao.getRandomNounsWithPluralByCategory(category, limit)

    // ── Distractors ───────────────────────────────────────────────────────────

    /**
     * Returns up to [limit] distractors of the same [type] as the question word.
     * Falls back to type-agnostic query if the type pool is too small to fill [limit] slots.
     */
    suspend fun getDistractors(
        excludeId: Int,
        type: String,
        limit: Int = 3
    ): List<VocabularyWord> {
        val primary = dao.getDistractors(excludeId, type, limit)
        if (primary.size >= limit) return primary

        val foundIds = primary.map { it.id } + excludeId
        val fallback = dao.getFallbackDistractors(foundIds, limit - primary.size)
        return (primary + fallback).distinctBy { it.id }.take(limit)
    }

    /**
     * Like [getDistractors] but only returns words that have a valid plural form.
     * Used exclusively for SING_TO_PLUR mode.
     */
    suspend fun getDistractorsWithPlural(
        excludeId: Int,
        type: String,
        limit: Int = 3
    ): List<VocabularyWord> {
        val primary = dao.getDistractorsWithPlural(excludeId, type, limit)
        if (primary.size >= limit) return primary

        val foundIds = primary.map { it.id } + excludeId
        val fallback = dao.getFallbackDistractors(foundIds, limit - primary.size)
        return (primary + fallback).distinctBy { it.id }.take(limit)
    }

    // ── Metadata ──────────────────────────────────────────────────────────────

    suspend fun getAllCategories(): List<String> = dao.getAllCategories()

    suspend fun getWordCount(): Int = dao.getWordCount()
}
