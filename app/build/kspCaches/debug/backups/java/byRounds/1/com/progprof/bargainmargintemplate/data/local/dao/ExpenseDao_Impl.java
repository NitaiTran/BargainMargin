package com.progprof.bargainmargintemplate.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.progprof.bargainmargintemplate.data.local.entities.ExpenseEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
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
public final class ExpenseDao_Impl implements ExpenseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ExpenseEntity> __insertionAdapterOfExpenseEntity;

  private final EntityDeletionOrUpdateAdapter<ExpenseEntity> __deletionAdapterOfExpenseEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  public ExpenseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExpenseEntity = new EntityInsertionAdapter<ExpenseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `expenses` (`id`,`amountOfExpense`,`descriptionOfExpense`,`categoryOfExpense`,`weekOfExpense`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExpenseEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getAmountOfExpense());
        statement.bindString(3, entity.getDescriptionOfExpense());
        statement.bindString(4, entity.getCategoryOfExpense());
        statement.bindLong(5, entity.getWeekOfExpense());
      }
    };
    this.__deletionAdapterOfExpenseEntity = new EntityDeletionOrUpdateAdapter<ExpenseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `expenses` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExpenseEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM expenses";
        return _query;
      }
    };
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
  public Object deleteExpense(final ExpenseEntity expense,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfExpenseEntity.handle(expense);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAll.acquire();
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
          __preparedStmtOfClearAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ExpenseEntity>> getRecentExpenses(final int limit) {
    final String _sql = "SELECT * FROM expenses ORDER BY id DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"expenses"}, new Callable<List<ExpenseEntity>>() {
      @Override
      @NonNull
      public List<ExpenseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmountOfExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "amountOfExpense");
          final int _cursorIndexOfDescriptionOfExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "descriptionOfExpense");
          final int _cursorIndexOfCategoryOfExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryOfExpense");
          final int _cursorIndexOfWeekOfExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "weekOfExpense");
          final List<ExpenseEntity> _result = new ArrayList<ExpenseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExpenseEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final double _tmpAmountOfExpense;
            _tmpAmountOfExpense = _cursor.getDouble(_cursorIndexOfAmountOfExpense);
            final String _tmpDescriptionOfExpense;
            _tmpDescriptionOfExpense = _cursor.getString(_cursorIndexOfDescriptionOfExpense);
            final String _tmpCategoryOfExpense;
            _tmpCategoryOfExpense = _cursor.getString(_cursorIndexOfCategoryOfExpense);
            final int _tmpWeekOfExpense;
            _tmpWeekOfExpense = _cursor.getInt(_cursorIndexOfWeekOfExpense);
            _item = new ExpenseEntity(_tmpId,_tmpAmountOfExpense,_tmpDescriptionOfExpense,_tmpCategoryOfExpense,_tmpWeekOfExpense);
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
  public Flow<List<ExpenseEntity>> getAllExpenses() {
    final String _sql = "SELECT * FROM expenses";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"expenses"}, new Callable<List<ExpenseEntity>>() {
      @Override
      @NonNull
      public List<ExpenseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmountOfExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "amountOfExpense");
          final int _cursorIndexOfDescriptionOfExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "descriptionOfExpense");
          final int _cursorIndexOfCategoryOfExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryOfExpense");
          final int _cursorIndexOfWeekOfExpense = CursorUtil.getColumnIndexOrThrow(_cursor, "weekOfExpense");
          final List<ExpenseEntity> _result = new ArrayList<ExpenseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExpenseEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final double _tmpAmountOfExpense;
            _tmpAmountOfExpense = _cursor.getDouble(_cursorIndexOfAmountOfExpense);
            final String _tmpDescriptionOfExpense;
            _tmpDescriptionOfExpense = _cursor.getString(_cursorIndexOfDescriptionOfExpense);
            final String _tmpCategoryOfExpense;
            _tmpCategoryOfExpense = _cursor.getString(_cursorIndexOfCategoryOfExpense);
            final int _tmpWeekOfExpense;
            _tmpWeekOfExpense = _cursor.getInt(_cursorIndexOfWeekOfExpense);
            _item = new ExpenseEntity(_tmpId,_tmpAmountOfExpense,_tmpDescriptionOfExpense,_tmpCategoryOfExpense,_tmpWeekOfExpense);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
