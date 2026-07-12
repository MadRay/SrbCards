package com.srbcards.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VocabularyDao {

    // ── Seeding ───────────────────────────────────────────────────────────────

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(words: List<VocabularyWord>)

    // ── Full-dataset queries ──────────────────────────────────────────────────

    /** Random words from the entire dataset. */
    @Query("SELECT * FROM vocabulary_words ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomWords(limit: Int = 20): List<VocabularyWord>

    /** Random nouns (for SR↔RU modes filtered to nouns). */
    @Query("SELECT * FROM vocabulary_words WHERE type = 'noun' ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomNouns(limit: Int = 20): List<VocabularyWord>

    /**
     * Random nouns that have a valid plural form (word_sr_plur != 'N/A').
     * Used exclusively for SING_TO_PLUR mode.
     */
    @Query(
        """
        SELECT * FROM vocabulary_words
        WHERE type = 'noun' AND word_sr_plur != 'N/A'
        ORDER BY RANDOM() LIMIT :limit
        """
    )
    suspend fun getRandomNounsWithPlural(limit: Int = 20): List<VocabularyWord>

    // ── Category-filtered queries ─────────────────────────────────────────────

    /** Random words filtered to a specific category. */
    @Query(
        "SELECT * FROM vocabulary_words WHERE category = :category ORDER BY RANDOM() LIMIT :limit"
    )
    suspend fun getRandomByCategory(category: String, limit: Int = 20): List<VocabularyWord>

    /** Random nouns filtered to a specific category. */
    @Query(
        """
        SELECT * FROM vocabulary_words
        WHERE category = :category AND type = 'noun'
        ORDER BY RANDOM() LIMIT :limit
        """
    )
    suspend fun getRandomNounsByCategory(category: String, limit: Int = 20): List<VocabularyWord>

    /** Random nouns with a valid plural, filtered to a specific category. */
    @Query(
        """
        SELECT * FROM vocabulary_words
        WHERE category = :category AND type = 'noun' AND word_sr_plur != 'N/A'
        ORDER BY RANDOM() LIMIT :limit
        """
    )
    suspend fun getRandomNounsWithPluralByCategory(
        category: String,
        limit: Int = 20
    ): List<VocabularyWord>

    // ── Distractor queries ────────────────────────────────────────────────────

    /**
     * Random words of the same [type], excluding the question word by [excludeId].
     * Used to generate multiple-choice distractors for SR↔RU modes.
     */
    @Query(
        """
        SELECT * FROM vocabulary_words
        WHERE id != :excludeId AND type = :type
        ORDER BY RANDOM() LIMIT :limit
        """
    )
    suspend fun getDistractors(excludeId: Int, type: String, limit: Int = 3): List<VocabularyWord>

    /**
     * Same as [getDistractors] but also requires word_sr_plur != 'N/A'.
     * Used to generate distractors for SING_TO_PLUR mode so all options are valid plurals.
     */
    @Query(
        """
        SELECT * FROM vocabulary_words
        WHERE id != :excludeId AND type = :type AND word_sr_plur != 'N/A'
        ORDER BY RANDOM() LIMIT :limit
        """
    )
    suspend fun getDistractorsWithPlural(
        excludeId: Int,
        type: String,
        limit: Int = 3
    ): List<VocabularyWord>

    /**
     * Fallback distractor query ignoring type restriction.
     * Triggered when there are not enough same-type words for 3 distractors.
     */
    @Query(
        """
        SELECT * FROM vocabulary_words
        WHERE id NOT IN (:excludeIds)
        ORDER BY RANDOM() LIMIT :limit
        """
    )
    suspend fun getFallbackDistractors(
        excludeIds: List<Int>,
        limit: Int = 3
    ): List<VocabularyWord>

    // ── Metadata queries ──────────────────────────────────────────────────────

    /** All distinct category names in alphabetical order. */
    @Query("SELECT DISTINCT category FROM vocabulary_words ORDER BY category ASC")
    suspend fun getAllCategories(): List<String>

    /** Total number of words in the database — used to verify seeding. */
    @Query("SELECT COUNT(*) FROM vocabulary_words")
    suspend fun getWordCount(): Int
}
