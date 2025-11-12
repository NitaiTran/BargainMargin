package com.progprof.bargainmargintemplate;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Double;
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
public final class BudgetDao_Impl implements BudgetDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PersistentData> __insertionAdapterOfPersistentData;

  public BudgetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPersistentData = new EntityInsertionAdapter<PersistentData>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `budget_table` (`id`,`MonthlyTotalBudget`,`MonthlyRemainingBudget`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PersistentData entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getDBMonthlyTotalBudget() == null) {
          statement.bindNull(2);
        } else {
          statement.bindDouble(2, entity.getDBMonthlyTotalBudget());
        }
        if (entity.getDBMonthlyRemainingBudget() == null) {
          statement.bindNull(3);
        } else {
          statement.bindDouble(3, entity.getDBMonthlyRemainingBudget());
        }
      }
    };
  }

  @Override
  public Object insert(final PersistentData budget, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPersistentData.insert(budget);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PersistentData>> getMyData() {
    final String _sql = "SELECT * FROM budget_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"budget_table"}, new Callable<List<PersistentData>>() {
      @Override
      @NonNull
      public List<PersistentData> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDBMonthlyTotalBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "MonthlyTotalBudget");
          final int _cursorIndexOfDBMonthlyRemainingBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "MonthlyRemainingBudget");
          final List<PersistentData> _result = new ArrayList<PersistentData>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PersistentData _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final Double _tmpDBMonthlyTotalBudget;
            if (_cursor.isNull(_cursorIndexOfDBMonthlyTotalBudget)) {
              _tmpDBMonthlyTotalBudget = null;
            } else {
              _tmpDBMonthlyTotalBudget = _cursor.getDouble(_cursorIndexOfDBMonthlyTotalBudget);
            }
            final Double _tmpDBMonthlyRemainingBudget;
            if (_cursor.isNull(_cursorIndexOfDBMonthlyRemainingBudget)) {
              _tmpDBMonthlyRemainingBudget = null;
            } else {
              _tmpDBMonthlyRemainingBudget = _cursor.getDouble(_cursorIndexOfDBMonthlyRemainingBudget);
            }
            _item = new PersistentData(_tmpId,_tmpDBMonthlyTotalBudget,_tmpDBMonthlyRemainingBudget);
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
