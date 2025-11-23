package com.progprof.bargainmargintemplate.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.progprof.bargainmargintemplate.data.local.entities.ExpenseEntity;
import com.progprof.bargainmargintemplate.data.local.entities.MonthEntity;
import com.progprof.bargainmargintemplate.data.local.entities.WeekEntity;
import com.progprof.bargainmargintemplate.data.local.relations.MonthWithWeeks;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
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
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BudgetDao_Impl implements BudgetDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MonthEntity> __insertionAdapterOfMonthEntity;

  private final EntityInsertionAdapter<MonthEntity> __insertionAdapterOfMonthEntity_1;

  private final EntityInsertionAdapter<WeekEntity> __insertionAdapterOfWeekEntity;

  private final EntityInsertionAdapter<ExpenseEntity> __insertionAdapterOfExpenseEntity;

  private final EntityDeletionOrUpdateAdapter<MonthEntity> __updateAdapterOfMonthEntity;

  private final EntityDeletionOrUpdateAdapter<WeekEntity> __updateAdapterOfWeekEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteExpenseById;

  public BudgetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMonthEntity = new EntityInsertionAdapter<MonthEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `months` (`id`,`year`,`month`,`totalBudget`,`totalSpent`,`monthlyGoal`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MonthEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getYear());
        statement.bindLong(3, entity.getMonth());
        statement.bindDouble(4, entity.getTotalBudget());
        statement.bindDouble(5, entity.getTotalSpent());
        statement.bindDouble(6, entity.getMonthlyGoal());
      }
    };
    this.__insertionAdapterOfMonthEntity_1 = new EntityInsertionAdapter<MonthEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `months` (`id`,`year`,`month`,`totalBudget`,`totalSpent`,`monthlyGoal`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MonthEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getYear());
        statement.bindLong(3, entity.getMonth());
        statement.bindDouble(4, entity.getTotalBudget());
        statement.bindDouble(5, entity.getTotalSpent());
        statement.bindDouble(6, entity.getMonthlyGoal());
      }
    };
    this.__insertionAdapterOfWeekEntity = new EntityInsertionAdapter<WeekEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `weeks` (`id`,`monthId`,`weekNumber`,`weekBudget`,`weekSpent`,`weeklyGoal`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WeekEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMonthId());
        statement.bindLong(3, entity.getWeekNumber());
        statement.bindDouble(4, entity.getWeekBudget());
        statement.bindDouble(5, entity.getWeekSpent());
        statement.bindDouble(6, entity.getWeeklyGoal());
      }
    };
    this.__insertionAdapterOfExpenseEntity = new EntityInsertionAdapter<ExpenseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `expenses` (`id`,`weekId`,`amountOfExpense`,`descriptionOfExpense`,`categoryOfExpense`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExpenseEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getWeekId());
        statement.bindDouble(3, entity.getAmountOfExpense());
        statement.bindString(4, entity.getDescriptionOfExpense());
        statement.bindString(5, entity.getCategoryOfExpense());
      }
    };
    this.__updateAdapterOfMonthEntity = new EntityDeletionOrUpdateAdapter<MonthEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `months` SET `id` = ?,`year` = ?,`month` = ?,`totalBudget` = ?,`totalSpent` = ?,`monthlyGoal` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MonthEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getYear());
        statement.bindLong(3, entity.getMonth());
        statement.bindDouble(4, entity.getTotalBudget());
        statement.bindDouble(5, entity.getTotalSpent());
        statement.bindDouble(6, entity.getMonthlyGoal());
        statement.bindLong(7, entity.getId());
      }
    };
    this.__updateAdapterOfWeekEntity = new EntityDeletionOrUpdateAdapter<WeekEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `weeks` SET `id` = ?,`monthId` = ?,`weekNumber` = ?,`weekBudget` = ?,`weekSpent` = ?,`weeklyGoal` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WeekEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMonthId());
        statement.bindLong(3, entity.getWeekNumber());
        statement.bindDouble(4, entity.getWeekBudget());
        statement.bindDouble(5, entity.getWeekSpent());
        statement.bindDouble(6, entity.getWeeklyGoal());
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteExpenseById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM expenses WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertMonthAndGetId(final MonthEntity month,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMonthEntity.insertAndReturnId(month);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMultipleMonths(final List<MonthEntity> months,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMonthEntity_1.insert(months);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertWeeks(final List<WeekEntity> weeks,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfWeekEntity.insert(weeks);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertExpense(final ExpenseEntity expense,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfExpenseEntity.insert(expense);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMonth(final MonthEntity month, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMonthEntity.handle(month);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateWeek(final WeekEntity week, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfWeekEntity.handle(week);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object addExpenseAndUpdateTotals(final ExpenseEntity expense,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> BudgetDao.DefaultImpls.addExpenseAndUpdateTotals(BudgetDao_Impl.this, expense, __cont), $completion);
  }

  @Override
  public Object deleteExpenseAndUpdateTotals(final ExpenseEntity expense,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> BudgetDao.DefaultImpls.deleteExpenseAndUpdateTotals(BudgetDao_Impl.this, expense, __cont), $completion);
  }

  @Override
  public Object deleteExpenseById(final long expenseId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteExpenseById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, expenseId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteExpenseById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<MonthWithWeeks> getMonthWithWeeks(final int year, final int month) {
    final String _sql = "SELECT * FROM months WHERE year = ? AND month = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, year);
    _argIndex = 2;
    _statement.bindLong(_argIndex, month);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"weeks",
        "months"}, new Callable<MonthWithWeeks>() {
      @Override
      @Nullable
      public MonthWithWeeks call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
            final int _cursorIndexOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "month");
            final int _cursorIndexOfTotalBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "totalBudget");
            final int _cursorIndexOfTotalSpent = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSpent");
            final int _cursorIndexOfMonthlyGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlyGoal");
            final LongSparseArray<ArrayList<WeekEntity>> _collectionWeeks = new LongSparseArray<ArrayList<WeekEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionWeeks.containsKey(_tmpKey)) {
                _collectionWeeks.put(_tmpKey, new ArrayList<WeekEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipweeksAscomProgprofBargainmargintemplateDataLocalEntitiesWeekEntity(_collectionWeeks);
            final MonthWithWeeks _result;
            if (_cursor.moveToFirst()) {
              final MonthEntity _tmpMonth;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final int _tmpYear;
              _tmpYear = _cursor.getInt(_cursorIndexOfYear);
              final int _tmpMonth_1;
              _tmpMonth_1 = _cursor.getInt(_cursorIndexOfMonth);
              final double _tmpTotalBudget;
              _tmpTotalBudget = _cursor.getDouble(_cursorIndexOfTotalBudget);
              final double _tmpTotalSpent;
              _tmpTotalSpent = _cursor.getDouble(_cursorIndexOfTotalSpent);
              final double _tmpMonthlyGoal;
              _tmpMonthlyGoal = _cursor.getDouble(_cursorIndexOfMonthlyGoal);
              _tmpMonth = new MonthEntity(_tmpId,_tmpYear,_tmpMonth_1,_tmpTotalBudget,_tmpTotalSpent,_tmpMonthlyGoal);
              final ArrayList<WeekEntity> _tmpWeeksCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpWeeksCollection = _collectionWeeks.get(_tmpKey_1);
              _result = new MonthWithWeeks(_tmpMonth,_tmpWeeksCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MonthEntity>> getAllMonths() {
    final String _sql = "SELECT * FROM months ORDER BY year, month";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"months"}, new Callable<List<MonthEntity>>() {
      @Override
      @NonNull
      public List<MonthEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "month");
          final int _cursorIndexOfTotalBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "totalBudget");
          final int _cursorIndexOfTotalSpent = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSpent");
          final int _cursorIndexOfMonthlyGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlyGoal");
          final List<MonthEntity> _result = new ArrayList<MonthEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MonthEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpYear;
            _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            final int _tmpMonth;
            _tmpMonth = _cursor.getInt(_cursorIndexOfMonth);
            final double _tmpTotalBudget;
            _tmpTotalBudget = _cursor.getDouble(_cursorIndexOfTotalBudget);
            final double _tmpTotalSpent;
            _tmpTotalSpent = _cursor.getDouble(_cursorIndexOfTotalSpent);
            final double _tmpMonthlyGoal;
            _tmpMonthlyGoal = _cursor.getDouble(_cursorIndexOfMonthlyGoal);
            _item = new MonthEntity(_tmpId,_tmpYear,_tmpMonth,_tmpTotalBudget,_tmpTotalSpent,_tmpMonthlyGoal);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ExpenseEntity>> getExpensesForWeek(final long weekId) {
    final String _sql = "SELECT * FROM expenses WHERE weekId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, weekId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"expenses"}, new Callable<List<ExpenseEntity>>() {
      @Override
      @NonNull
      public List<ExpenseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWeekId = CursorUtil.getColumnIndexOrThrow(_cursor, "weekId");
          final int _cursorIndexOfAmountOfExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "amountOfExpense");
          final int _cursorIndexOfDescriptionOfExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "descriptionOfExpense");
          final int _cursorIndexOfCategoryOfExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryOfExpense");
          final List<ExpenseEntity> _result = new ArrayList<ExpenseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExpenseEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpWeekId;
            _tmpWeekId = _cursor.getLong(_cursorIndexOfWeekId);
            final double _tmpAmountOfExpense;
            _tmpAmountOfExpense = _cursor.getDouble(_cursorIndexOfAmountOfExpense);
            final String _tmpDescriptionOfExpense;
            _tmpDescriptionOfExpense = _cursor.getString(_cursorIndexOfDescriptionOfExpense);
            final String _tmpCategoryOfExpense;
            _tmpCategoryOfExpense = _cursor.getString(_cursorIndexOfCategoryOfExpense);
            _item = new ExpenseEntity(_tmpId,_tmpWeekId,_tmpAmountOfExpense,_tmpDescriptionOfExpense,_tmpCategoryOfExpense);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getWeekById(final long weekId, final Continuation<? super WeekEntity> $completion) {
    final String _sql = "SELECT * FROM weeks WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, weekId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<WeekEntity>() {
      @Override
      @Nullable
      public WeekEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMonthId = CursorUtil.getColumnIndexOrThrow(_cursor, "monthId");
          final int _cursorIndexOfWeekNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "weekNumber");
          final int _cursorIndexOfWeekBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "weekBudget");
          final int _cursorIndexOfWeekSpent = CursorUtil.getColumnIndexOrThrow(_cursor, "weekSpent");
          final int _cursorIndexOfWeeklyGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "weeklyGoal");
          final WeekEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpMonthId;
            _tmpMonthId = _cursor.getLong(_cursorIndexOfMonthId);
            final int _tmpWeekNumber;
            _tmpWeekNumber = _cursor.getInt(_cursorIndexOfWeekNumber);
            final double _tmpWeekBudget;
            _tmpWeekBudget = _cursor.getDouble(_cursorIndexOfWeekBudget);
            final double _tmpWeekSpent;
            _tmpWeekSpent = _cursor.getDouble(_cursorIndexOfWeekSpent);
            final double _tmpWeeklyGoal;
            _tmpWeeklyGoal = _cursor.getDouble(_cursorIndexOfWeeklyGoal);
            _result = new WeekEntity(_tmpId,_tmpMonthId,_tmpWeekNumber,_tmpWeekBudget,_tmpWeekSpent,_tmpWeeklyGoal);
          } else {
            _result = null;
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
  public Object getMonthById(final long monthId,
      final Continuation<? super MonthEntity> $completion) {
    final String _sql = "SELECT * FROM months WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, monthId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MonthEntity>() {
      @Override
      @Nullable
      public MonthEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "month");
          final int _cursorIndexOfTotalBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "totalBudget");
          final int _cursorIndexOfTotalSpent = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSpent");
          final int _cursorIndexOfMonthlyGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlyGoal");
          final MonthEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpYear;
            _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            final int _tmpMonth;
            _tmpMonth = _cursor.getInt(_cursorIndexOfMonth);
            final double _tmpTotalBudget;
            _tmpTotalBudget = _cursor.getDouble(_cursorIndexOfTotalBudget);
            final double _tmpTotalSpent;
            _tmpTotalSpent = _cursor.getDouble(_cursorIndexOfTotalSpent);
            final double _tmpMonthlyGoal;
            _tmpMonthlyGoal = _cursor.getDouble(_cursorIndexOfMonthlyGoal);
            _result = new MonthEntity(_tmpId,_tmpYear,_tmpMonth,_tmpTotalBudget,_tmpTotalSpent,_tmpMonthlyGoal);
          } else {
            _result = null;
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

  private void __fetchRelationshipweeksAscomProgprofBargainmargintemplateDataLocalEntitiesWeekEntity(
      @NonNull final LongSparseArray<ArrayList<WeekEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipweeksAscomProgprofBargainmargintemplateDataLocalEntitiesWeekEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`monthId`,`weekNumber`,`weekBudget`,`weekSpent`,`weeklyGoal` FROM `weeks` WHERE `monthId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "monthId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfMonthId = 1;
      final int _cursorIndexOfWeekNumber = 2;
      final int _cursorIndexOfWeekBudget = 3;
      final int _cursorIndexOfWeekSpent = 4;
      final int _cursorIndexOfWeeklyGoal = 5;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<WeekEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final WeekEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpMonthId;
          _tmpMonthId = _cursor.getLong(_cursorIndexOfMonthId);
          final int _tmpWeekNumber;
          _tmpWeekNumber = _cursor.getInt(_cursorIndexOfWeekNumber);
          final double _tmpWeekBudget;
          _tmpWeekBudget = _cursor.getDouble(_cursorIndexOfWeekBudget);
          final double _tmpWeekSpent;
          _tmpWeekSpent = _cursor.getDouble(_cursorIndexOfWeekSpent);
          final double _tmpWeeklyGoal;
          _tmpWeeklyGoal = _cursor.getDouble(_cursorIndexOfWeeklyGoal);
          _item_1 = new WeekEntity(_tmpId,_tmpMonthId,_tmpWeekNumber,_tmpWeekBudget,_tmpWeekSpent,_tmpWeeklyGoal);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
