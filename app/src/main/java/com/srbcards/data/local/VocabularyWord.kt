package com.srbcards.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a single vocabulary entry from the CSV dataset.
 *
 * Column mapping:
 *  - word_sr_plur = "N/A" for expressions, adverbs, verbs, and nouns with no standard plural.
 *  - gender        = "N/A" for non-nouns; "m", "f", or "n" for nouns.
 *  - type          = "noun" | "expression" | "verb" | "adverb"
 */
@Entity(tableName = "vocabulary_words")
data class VocabularyWord(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "word_sr_sing")
    val wordSrSing: String,

    @ColumnInfo(name = "word_sr_plur")
    val wordSrPlur: String,

    @ColumnInfo(name = "word_ru")
    val wordRu: String,

    /** "m", "f", "n", or "N/A" */
    val gender: String,

    /** "noun", "expression", "verb", or "adverb" */
    val type: String,

    val category: String
)
