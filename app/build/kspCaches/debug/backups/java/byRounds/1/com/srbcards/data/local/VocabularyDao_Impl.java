package com.srbcards.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class VocabularyDao_Impl implements VocabularyDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<VocabularyWord> __insertionAdapterOfVocabularyWord;

  public VocabularyDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfVocabularyWord = new EntityInsertionAdapter<VocabularyWord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `vocabulary_words` (`id`,`word_sr_sing`,`word_sr_plur`,`word_ru`,`gender`,`type`,`category`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VocabularyWord entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getWordSrSing());
        statement.bindString(3, entity.getWordSrPlur());
        statement.bindString(4, entity.getWordRu());
        statement.bindString(5, entity.getGender());
        statement.bindString(6, entity.getType());
        statement.bindString(7, entity.getCategory());
      }
    };
  }

  @Override
  public Object insertAll(final List<VocabularyWord> words,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfVocabularyWord.insert(words);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRandomWords(final int limit,
      final Continuation<? super List<VocabularyWord>> $completion) {
    final String _sql = "SELECT * FROM vocabulary_words ORDER BY RANDOM() LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<VocabularyWord>>() {
      @Override
      @NonNull
      public List<VocabularyWord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWordSrSing = CursorUtil.getColumnIndexOrThrow(_cursor, "word_sr_sing");
          final int _cursorIndexOfWordSrPlur = CursorUtil.getColumnIndexOrThrow(_cursor, "word_sr_plur");
          final int _cursorIndexOfWordRu = CursorUtil.getColumnIndexOrThrow(_cursor, "word_ru");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final List<VocabularyWord> _result = new ArrayList<VocabularyWord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VocabularyWord _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpWordSrSing;
            _tmpWordSrSing = _cursor.getString(_cursorIndexOfWordSrSing);
            final String _tmpWordSrPlur;
            _tmpWordSrPlur = _cursor.getString(_cursorIndexOfWordSrPlur);
            final String _tmpWordRu;
            _tmpWordRu = _cursor.getString(_cursorIndexOfWordRu);
            final String _tmpGender;
            _tmpGender = _cursor.getString(_cursorIndexOfGender);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            _item = new VocabularyWord(_tmpId,_tmpWordSrSing,_tmpWordSrPlur,_tmpWordRu,_tmpGender,_tmpType,_tmpCategory);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRandomNouns(final int limit,
      final Continuation<? super List<VocabularyWord>> $completion) {
    final String _sql = "SELECT * FROM vocabulary_words WHERE type = 'noun' ORDER BY RANDOM() LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<VocabularyWord>>() {
      @Override
      @NonNull
      public List<VocabularyWord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWordSrSing = CursorUtil.getColumnIndexOrThrow(_cursor, "word_sr_sing");
          final int _cursorIndexOfWordSrPlur = CursorUtil.getColumnIndexOrThrow(_cursor, "word_sr_plur");
          final int _cursorIndexOfWordRu = CursorUtil.getColumnIndexOrThrow(_cursor, "word_ru");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final List<VocabularyWord> _result = new ArrayList<VocabularyWord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VocabularyWord _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpWordSrSing;
            _tmpWordSrSing = _cursor.getString(_cursorIndexOfWordSrSing);
            final String _tmpWordSrPlur;
            _tmpWordSrPlur = _cursor.getString(_cursorIndexOfWordSrPlur);
            final String _tmpWordRu;
            _tmpWordRu = _cursor.getString(_cursorIndexOfWordRu);
            final String _tmpGender;
            _tmpGender = _cursor.getString(_cursorIndexOfGender);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            _item = new VocabularyWord(_tmpId,_tmpWordSrSing,_tmpWordSrPlur,_tmpWordRu,_tmpGender,_tmpType,_tmpCategory);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRandomNounsWithPlural(final int limit,
      final Continuation<? super List<VocabularyWord>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM vocabulary_words\n"
            + "        WHERE type = 'noun' AND word_sr_plur != 'N/A'\n"
            + "        ORDER BY RANDOM() LIMIT ?\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<VocabularyWord>>() {
      @Override
      @NonNull
      public List<VocabularyWord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWordSrSing = CursorUtil.getColumnIndexOrThrow(_cursor, "word_sr_sing");
          final int _cursorIndexOfWordSrPlur = CursorUtil.getColumnIndexOrThrow(_cursor, "word_sr_plur");
          final int _cursorIndexOfWordRu = CursorUtil.getColumnIndexOrThrow(_cursor, "word_ru");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final List<VocabularyWord> _result = new ArrayList<VocabularyWord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VocabularyWord _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpWordSrSing;
            _tmpWordSrSing = _cursor.getString(_cursorIndexOfWordSrSing);
            final String _tmpWordSrPlur;
            _tmpWordSrPlur = _cursor.getString(_cursorIndexOfWordSrPlur);
            final String _tmpWordRu;
            _tmpWordRu = _cursor.getString(_cursorIndexOfWordRu);
            final String _tmpGender;
            _tmpGender = _cursor.getString(_cursorIndexOfGender);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            _item = new VocabularyWord(_tmpId,_tmpWordSrSing,_tmpWordSrPlur,_tmpWordRu,_tmpGender,_tmpType,_tmpCategory);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRandomByCategory(final String category, final int limit,
      final Continuation<? super List<VocabularyWord>> $completion) {
    final String _sql = "SELECT * FROM vocabulary_words WHERE category = ? ORDER BY RANDOM() LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, category);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<VocabularyWord>>() {
      @Override
      @NonNull
      public List<VocabularyWord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWordSrSing = CursorUtil.getColumnIndexOrThrow(_cursor, "word_sr_sing");
          final int _cursorIndexOfWordSrPlur = CursorUtil.getColumnIndexOrThrow(_cursor, "word_sr_plur");
          final int _cursorIndexOfWordRu = CursorUtil.getColumnIndexOrThrow(_cursor, "word_ru");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final List<VocabularyWord> _result = new ArrayList<VocabularyWord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VocabularyWord _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpWordSrSing;
            _tmpWordSrSing = _cursor.getString(_cursorIndexOfWordSrSing);
            final String _tmpWordSrPlur;
            _tmpWordSrPlur = _cursor.getString(_cursorIndexOfWordSrPlur);
            final String _tmpWordRu;
            _tmpWordRu = _cursor.getString(_cursorIndexOfWordRu);
            final String _tmpGender;
            _tmpGender = _cursor.getString(_cursorIndexOfGender);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            _item = new VocabularyWord(_tmpId,_tmpWordSrSing,_tmpWordSrPlur,_tmpWordRu,_tmpGender,_tmpType,_tmpCategory);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRandomNounsByCategory(final String category, final int limit,
      final Continuation<? super List<VocabularyWord>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM vocabulary_words\n"
            + "        WHERE category = ? AND type = 'noun'\n"
            + "        ORDER BY RANDOM() LIMIT ?\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, category);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<VocabularyWord>>() {
      @Override
      @NonNull
      public List<VocabularyWord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWordSrSing = CursorUtil.getColumnIndexOrThrow(_cursor, "word_sr_sing");
          final int _cursorIndexOfWordSrPlur = CursorUtil.getColumnIndexOrThrow(_cursor, "word_sr_plur");
          final int _cursorIndexOfWordRu = CursorUtil.getColumnIndexOrThrow(_cursor, "word_ru");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final List<VocabularyWord> _result = new ArrayList<VocabularyWord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VocabularyWord _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpWordSrSing;
            _tmpWordSrSing = _cursor.getString(_cursorIndexOfWordSrSing);
            final String _tmpWordSrPlur;
            _tmpWordSrPlur = _cursor.getString(_cursorIndexOfWordSrPlur);
            final String _tmpWordRu;
            _tmpWordRu = _cursor.getString(_cursorIndexOfWordRu);
            final String _tmpGender;
            _tmpGender = _cursor.getString(_cursorIndexOfGender);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            _item = new VocabularyWord(_tmpId,_tmpWordSrSing,_tmpWordSrPlur,_tmpWordRu,_tmpGender,_tmpType,_tmpCategory);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRandomNounsWithPluralByCategory(final String category, final int limit,
      final Continuation<? super List<VocabularyWord>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM vocabulary_words\n"
            + "        WHERE category = ? AND type = 'noun' AND word_sr_plur != 'N/A'\n"
            + "        ORDER BY RANDOM() LIMIT ?\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, category);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<VocabularyWord>>() {
      @Override
      @NonNull
      public List<VocabularyWord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWordSrSing = CursorUtil.getColumnIndexOrThrow(_cursor, "word_sr_sing");
          final int _cursorIndexOfWordSrPlur = CursorUtil.getColumnIndexOrThrow(_cursor, "word_sr_plur");
          final int _cursorIndexOfWordRu = CursorUtil.getColumnIndexOrThrow(_cursor, "word_ru");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final List<VocabularyWord> _result = new ArrayList<VocabularyWord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VocabularyWord _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpWordSrSing;
            _tmpWordSrSing = _cursor.getString(_cursorIndexOfWordSrSing);
            final String _tmpWordSrPlur;
            _tmpWordSrPlur = _cursor.getString(_cursorIndexOfWordSrPlur);
            final String _tmpWordRu;
            _tmpWordRu = _cursor.getString(_cursorIndexOfWordRu);
            final String _tmpGender;
            _tmpGender = _cursor.getString(_cursorIndexOfGender);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            _item = new VocabularyWord(_tmpId,_tmpWordSrSing,_tmpWordSrPlur,_tmpWordRu,_tmpGender,_tmpType,_tmpCategory);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getDistractors(final int excludeId, final String type, final int limit,
      final Continuation<? super List<VocabularyWord>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM vocabulary_words\n"
            + "        WHERE id != ? AND type = ?\n"
            + "        ORDER BY RANDOM() LIMIT ?\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, excludeId);
    _argIndex = 2;
    _statement.bindString(_argIndex, type);
    _argIndex = 3;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<VocabularyWord>>() {
      @Override
      @NonNull
      public List<VocabularyWord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWordSrSing = CursorUtil.getColumnIndexOrThrow(_cursor, "word_sr_sing");
          final int _cursorIndexOfWordSrPlur = CursorUtil.getColumnIndexOrThrow(_cursor, "word_sr_plur");
          final int _cursorIndexOfWordRu = CursorUtil.getColumnIndexOrThrow(_cursor, "word_ru");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final List<VocabularyWord> _result = new ArrayList<VocabularyWord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VocabularyWord _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpWordSrSing;
            _tmpWordSrSing = _cursor.getString(_cursorIndexOfWordSrSing);
            final String _tmpWordSrPlur;
            _tmpWordSrPlur = _cursor.getString(_cursorIndexOfWordSrPlur);
            final String _tmpWordRu;
            _tmpWordRu = _cursor.getString(_cursorIndexOfWordRu);
            final String _tmpGender;
            _tmpGender = _cursor.getString(_cursorIndexOfGender);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            _item = new VocabularyWord(_tmpId,_tmpWordSrSing,_tmpWordSrPlur,_tmpWordRu,_tmpGender,_tmpType,_tmpCategory);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getDistractorsWithPlural(final int excludeId, final String type, final int limit,
      final Continuation<? super List<VocabularyWord>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM vocabulary_words\n"
            + "        WHERE id != ? AND type = ? AND word_sr_plur != 'N/A'\n"
            + "        ORDER BY RANDOM() LIMIT ?\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, excludeId);
    _argIndex = 2;
    _statement.bindString(_argIndex, type);
    _argIndex = 3;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<VocabularyWord>>() {
      @Override
      @NonNull
      public List<VocabularyWord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWordSrSing = CursorUtil.getColumnIndexOrThrow(_cursor, "word_sr_sing");
          final int _cursorIndexOfWordSrPlur = CursorUtil.getColumnIndexOrThrow(_cursor, "word_sr_plur");
          final int _cursorIndexOfWordRu = CursorUtil.getColumnIndexOrThrow(_cursor, "word_ru");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final List<VocabularyWord> _result = new ArrayList<VocabularyWord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VocabularyWord _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpWordSrSing;
            _tmpWordSrSing = _cursor.getString(_cursorIndexOfWordSrSing);
            final String _tmpWordSrPlur;
            _tmpWordSrPlur = _cursor.getString(_cursorIndexOfWordSrPlur);
            final String _tmpWordRu;
            _tmpWordRu = _cursor.getString(_cursorIndexOfWordRu);
            final String _tmpGender;
            _tmpGender = _cursor.getString(_cursorIndexOfGender);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            _item = new VocabularyWord(_tmpId,_tmpWordSrSing,_tmpWordSrPlur,_tmpWordRu,_tmpGender,_tmpType,_tmpCategory);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getFallbackDistractors(final List<Integer> excludeIds, final int limit,
      final Continuation<? super List<VocabularyWord>> $completion) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("\n");
    _stringBuilder.append("        SELECT * FROM vocabulary_words");
    _stringBuilder.append("\n");
    _stringBuilder.append("        WHERE id NOT IN (");
    final int _inputSize = excludeIds.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    _stringBuilder.append("\n");
    _stringBuilder.append("        ORDER BY RANDOM() LIMIT ");
    _stringBuilder.append("?");
    _stringBuilder.append("\n");
    _stringBuilder.append("        ");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 1 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int _item : excludeIds) {
      _statement.bindLong(_argIndex, _item);
      _argIndex++;
    }
    _argIndex = 1 + _inputSize;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<VocabularyWord>>() {
      @Override
      @NonNull
      public List<VocabularyWord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWordSrSing = CursorUtil.getColumnIndexOrThrow(_cursor, "word_sr_sing");
          final int _cursorIndexOfWordSrPlur = CursorUtil.getColumnIndexOrThrow(_cursor, "word_sr_plur");
          final int _cursorIndexOfWordRu = CursorUtil.getColumnIndexOrThrow(_cursor, "word_ru");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final List<VocabularyWord> _result = new ArrayList<VocabularyWord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VocabularyWord _item_1;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpWordSrSing;
            _tmpWordSrSing = _cursor.getString(_cursorIndexOfWordSrSing);
            final String _tmpWordSrPlur;
            _tmpWordSrPlur = _cursor.getString(_cursorIndexOfWordSrPlur);
            final String _tmpWordRu;
            _tmpWordRu = _cursor.getString(_cursorIndexOfWordRu);
            final String _tmpGender;
            _tmpGender = _cursor.getString(_cursorIndexOfGender);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            _item_1 = new VocabularyWord(_tmpId,_tmpWordSrSing,_tmpWordSrPlur,_tmpWordRu,_tmpGender,_tmpType,_tmpCategory);
            _result.add(_item_1);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllCategories(final Continuation<? super List<String>> $completion) {
    final String _sql = "SELECT DISTINCT category FROM vocabulary_words ORDER BY category ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getWordCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM vocabulary_words";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
