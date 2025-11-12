package com.progprof.bargainmargintemplate.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.progprof.bargainmargintemplate.data.local.entities.BudgetEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
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

  private final EntityInsertionAdapter<BudgetEntity> __insertionAdapterOfBudgetEntity;

  public BudgetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBudgetEntity = new EntityInsertionAdapter<BudgetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `budgets` (`id`,`totalBudget`,`totalRemainingBudget`,`monthlyRemainingBudget`,`week1TotalBudget`,`week1RemainingBudget`,`week2TotalBudget`,`week2RemainingBudget`,`week3TotalBudget`,`week3RemainingBudget`,`week4TotalBudget`,`week4RemainingBudget`,`myCurrentWeek`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BudgetEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getTotalBudget());
        statement.bindDouble(3, entity.getTotalRemainingBudget());
        statement.bindDouble(4, entity.getMonthlyRemainingBudget());
        statement.bindDouble(5, entity.getWeek1TotalBudget());
        statement.bindDouble(6, entity.getWeek1RemainingBudget());
        statement.bindDouble(7, entity.getWeek2TotalBudget());
        statement.bindDouble(8, entity.getWeek2RemainingBudget());
        statement.bindDouble(9, entity.getWeek3TotalBudget());
        statement.bindDouble(10, entity.getWeek3RemainingBudget());
        statement.bindDouble(11, entity.getWeek4TotalBudget());
        statement.bindDouble(12, entity.getWeek4RemainingBudget());
        statement.bindLong(13, entity.getMyCurrentWeek());
      }
    };
  }

  @Override
  public Object upsertBudget(final BudgetEntity budget,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBudgetEntity.insert(budget);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<BudgetEntity> getBudget() {
    final String _sql = "SELECT * FROM budgets LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"budgets"}, new Callable<BudgetEntity>() {
      @Override
      @Nullable
      public BudgetEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTotalBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "totalBudget");
          final int _cursorIndexOfTotalRemainingBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "totalRemainingBudget");
          final int _cursorIndexOfMonthlyRemainingBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlyRemainingBudget");
          final int _cursorIndexOfWeek1TotalBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "week1TotalBudget");
          final int _cursorIndexOfWeek1RemainingBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "week1RemainingBudget");
          final int _cursorIndexOfWeek2TotalBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "week2TotalBudget");
          final int _cursorIndexOfWeek2RemainingBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "week2RemainingBudget");
          final int _cursorIndexOfWeek3TotalBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "week3TotalBudget");
          final int _cursorIndexOfWeek3RemainingBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "week3RemainingBudget");
          final int _cursorIndexOfWeek4TotalBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "week4TotalBudget");
          final int _cursorIndexOfWeek4RemainingBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "week4RemainingBudget");
          final int _cursorIndexOfMyCurrentWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "myCurrentWeek");
          final BudgetEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final double _tmpTotalBudget;
            _tmpTotalBudget = _cursor.getDouble(_cursorIndexOfTotalBudget);
            final double _tmpTotalRemainingBudget;
            _tmpTotalRemainingBudget = _cursor.getDouble(_cursorIndexOfTotalRemainingBudget);
            final double _tmpMonthlyRemainingBudget;
            _tmpMonthlyRemainingBudget = _cursor.getDouble(_cursorIndexOfMonthlyRemainingBudget);
            final double _tmpWeek1TotalBudget;
            _tmpWeek1TotalBudget = _cursor.getDouble(_cursorIndexOfWeek1TotalBudget);
            final double _tmpWeek1RemainingBudget;
            _tmpWeek1RemainingBudget = _cursor.getDouble(_cursorIndexOfWeek1RemainingBudget);
            final double _tmpWeek2TotalBudget;
            _tmpWeek2TotalBudget = _cursor.getDouble(_cursorIndexOfWeek2TotalBudget);
            final double _tmpWeek2RemainingBudget;
            _tmpWeek2RemainingBudget = _cursor.getDouble(_cursorIndexOfWeek2RemainingBudget);
            final double _tmpWeek3TotalBudget;
            _tmpWeek3TotalBudget = _cursor.getDouble(_cursorIndexOfWeek3TotalBudget);
            final double _tmpWeek3RemainingBudget;
            _tmpWeek3RemainingBudget = _cursor.getDouble(_cursorIndexOfWeek3RemainingBudget);
            final double _tmpWeek4TotalBudget;
            _tmpWeek4TotalBudget = _cursor.getDouble(_cursorIndexOfWeek4TotalBudget);
            final double _tmpWeek4RemainingBudget;
            _tmpWeek4RemainingBudget = _cursor.getDouble(_cursorIndexOfWeek4RemainingBudget);
            final int _tmpMyCurrentWeek;
            _tmpMyCurrentWeek = _cursor.getInt(_cursorIndexOfMyCurrentWeek);
            _result = new BudgetEntity(_tmpId,_tmpTotalBudget,_tmpTotalRemainingBudget,_tmpMonthlyRemainingBudget,_tmpWeek1TotalBudget,_tmpWeek1RemainingBudget,_tmpWeek2TotalBudget,_tmpWeek2RemainingBudget,_tmpWeek3TotalBudget,_tmpWeek3RemainingBudget,_tmpWeek4TotalBudget,_tmpWeek4RemainingBudget,_tmpMyCurrentWeek);
          } else {
            _result = null;
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
