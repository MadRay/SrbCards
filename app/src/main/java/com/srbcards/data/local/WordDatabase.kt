package com.srbcards.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

private const val TAG = "WordDatabase"
private const val DATABASE_NAME = "word_database"
private const val CSV_ASSET = "serbian_russian_a1_vocabulary.csv"

/**
 * Room database with a single [VocabularyWord] entity.
 *
 * ## Pre-population strategy
 * On first launch, [RoomDatabase.Callback.onCreate] fires once after the empty schema is created.
 * A coroutine on [Dispatchers.IO] opens the bundled CSV from `assets/`, parses every row, and
 * bulk-inserts them via [VocabularyDao.insertAll].
 *
 * This is preferable to shipping a pre-built `.db` file because:
 *  - The CSV is human-editable and stays in sync with the source of truth.
 *  - No need to regenerate a binary asset every time the word list changes.
 */
@Database(entities = [VocabularyWord::class], version = 1, exportSchema = false)
abstract class WordDatabase : RoomDatabase() {

    abstract fun vocabularyDao(): VocabularyDao

    companion object {
        @Volatile private var INSTANCE: WordDatabase? = null

        fun getDatabase(context: Context): WordDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
            }

        private fun buildDatabase(appContext: Context): WordDatabase {
            lateinit var db: WordDatabase
            db = Room.databaseBuilder(appContext, WordDatabase::class.java, DATABASE_NAME)
                .addCallback(object : Callback() {
                    override fun onCreate(sqLiteDb: SupportSQLiteDatabase) {
                        super.onCreate(sqLiteDb)
                        // `db` is fully assigned before this callback fires (it fires on first
                        // actual query, not at .build() time), so using it here is safe.
                        CoroutineScope(Dispatchers.IO).launch {
                            seedFromCsv(appContext, db.vocabularyDao())
                        }
                    }
                })
                .build()
            return db
        }

        // ── CSV Seeding ───────────────────────────────────────────────────────

        private suspend fun seedFromCsv(context: Context, dao: VocabularyDao) {
            try {
                context.assets.open(CSV_ASSET).use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).use { reader ->
                        reader.readLine() // skip header line
                        val words = buildList {
                            reader.forEachLine { line ->
                                if (line.isNotBlank()) {
                                    parseCsvLine(line)?.let { add(it) }
                                }
                            }
                        }
                        if (words.isNotEmpty()) {
                            dao.insertAll(words)
                            Log.d(TAG, "Seeded ${words.size} vocabulary words from CSV.")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "CSV seeding failed", e)
            }
        }

        /**
         * Parses a single CSV line into a [VocabularyWord].
         *
         * Uses `limit = 7` so any commas that may appear inside the last field (category)
         * are captured correctly without splitting. The known CSV does not have commas in
         * field values, but this guard makes the parser robust to future edits.
         *
         * CSV columns: id, word_sr_sing, word_sr_plur, word_ru, gender, type, category
         */
        private fun parseCsvLine(line: String): VocabularyWord? {
            return try {
                val p = line.split(",", limit = 7)
                if (p.size < 7) {
                    Log.w(TAG, "Skipping malformed CSV line: $line")
                    return null
                }
                VocabularyWord(
                    id = p[0].trim().toInt(),
                    wordSrSing = p[1].trim(),
                    wordSrPlur = p[2].trim(),
                    wordRu = p[3].trim(),
                    gender = p[4].trim(),
                    type = p[5].trim(),
                    category = p[6].trim()
                )
            } catch (e: Exception) {
                Log.w(TAG, "Failed to parse CSV line: $line", e)
                null
            }
        }
    }
}
