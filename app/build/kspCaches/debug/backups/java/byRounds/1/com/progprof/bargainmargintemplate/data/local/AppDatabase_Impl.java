package com.progprof.bargainmargintemplate.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.progprof.bargainmargintemplate.data.local.dao.BudgetDao;
import com.progprof.bargainmargintemplate.data.local.dao.BudgetDao_Impl;
import com.progprof.bargainmargintemplate.data.local.dao.CategoryDao;
import com.progprof.bargainmargintemplate.data.local.dao.CategoryDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile BudgetDao _budgetDao;

  private volatile CategoryDao _categoryDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(5) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `months` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `year` INTEGER NOT NULL, `month` INTEGER NOT NULL, `totalBudget` REAL NOT NULL, `totalSpent` REAL NOT NULL, `monthlyGoal` REAL NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_months_year_month` ON `months` (`year`, `month`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `weeks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `monthId` INTEGER NOT NULL, `weekNumber` INTEGER NOT NULL, `weekBudget` REAL NOT NULL, `weekSpent` REAL NOT NULL, `weeklyGoal` REAL NOT NULL, FOREIGN KEY(`monthId`) REFERENCES `months`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_weeks_monthId` ON `weeks` (`monthId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `expenses` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `weekId` INTEGER NOT NULL, `amountOfExpense` REAL NOT NULL, `descriptionOfExpense` TEXT NOT NULL, `categoryOfExpense` TEXT NOT NULL, FOREIGN KEY(`weekId`) REFERENCES `weeks`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE TABLE IF NOT EXISTS `categories` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `categoryName` TEXT NOT NULL, `totalBudget` REAL NOT NULL, `budgetRemaining` REAL NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'b0c650720baff5204ee74e58ff77fc11')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `months`");
        db.execSQL("DROP TABLE IF EXISTS `weeks`");
        db.execSQL("DROP TABLE IF EXISTS `expenses`");
        db.execSQL("DROP TABLE IF EXISTS `categories`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsMonths = new HashMap<String, TableInfo.Column>(6);
        _columnsMonths.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMonths.put("year", new TableInfo.Column("year", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMonths.put("month", new TableInfo.Column("month", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMonths.put("totalBudget", new TableInfo.Column("totalBudget", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMonths.put("totalSpent", new TableInfo.Column("totalSpent", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMonths.put("monthlyGoal", new TableInfo.Column("monthlyGoal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMonths = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMonths = new HashSet<TableInfo.Index>(1);
        _indicesMonths.add(new TableInfo.Index("index_months_year_month", true, Arrays.asList("year", "month"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoMonths = new TableInfo("months", _columnsMonths, _foreignKeysMonths, _indicesMonths);
        final TableInfo _existingMonths = TableInfo.read(db, "months");
        if (!_infoMonths.equals(_existingMonths)) {
          return new RoomOpenHelper.ValidationResult(false, "months(com.progprof.bargainmargintemplate.data.local.entities.MonthEntity).\n"
                  + " Expected:\n" + _infoMonths + "\n"
                  + " Found:\n" + _existingMonths);
        }
        final HashMap<String, TableInfo.Column> _columnsWeeks = new HashMap<String, TableInfo.Column>(6);
        _columnsWeeks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeks.put("monthId", new TableInfo.Column("monthId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeks.put("weekNumber", new TableInfo.Column("weekNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeks.put("weekBudget", new TableInfo.Column("weekBudget", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeks.put("weekSpent", new TableInfo.Column("weekSpent", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeeks.put("weeklyGoal", new TableInfo.Column("weeklyGoal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWeeks = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysWeeks.add(new TableInfo.ForeignKey("months", "CASCADE", "NO ACTION", Arrays.asList("monthId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesWeeks = new HashSet<TableInfo.Index>(1);
        _indicesWeeks.add(new TableInfo.Index("index_weeks_monthId", false, Arrays.asList("monthId"), Arrays.asList("ASC")));
        final TableInfo _infoWeeks = new TableInfo("weeks", _columnsWeeks, _foreignKeysWeeks, _indicesWeeks);
        final TableInfo _existingWeeks = TableInfo.read(db, "weeks");
        if (!_infoWeeks.equals(_existingWeeks)) {
          return new RoomOpenHelper.ValidationResult(false, "weeks(com.progprof.bargainmargintemplate.data.local.entities.WeekEntity).\n"
                  + " Expected:\n" + _infoWeeks + "\n"
                  + " Found:\n" + _existingWeeks);
        }
        final HashMap<String, TableInfo.Column> _columnsExpenses = new HashMap<String, TableInfo.Column>(5);
        _columnsExpenses.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("weekId", new TableInfo.Column("weekId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("amountOfExpense", new TableInfo.Column("amountOfExpense", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("descriptionOfExpense", new TableInfo.Column("descriptionOfExpense", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExpenses.put("categoryOfExpense", new TableInfo.Column("categoryOfExpense", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExpenses = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysExpenses.add(new TableInfo.ForeignKey("weeks", "CASCADE", "NO ACTION", Arrays.asList("weekId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesExpenses = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExpenses = new TableInfo("expenses", _columnsExpenses, _foreignKeysExpenses, _indicesExpenses);
        final TableInfo _existingExpenses = TableInfo.read(db, "expenses");
        if (!_infoExpenses.equals(_existingExpenses)) {
          return new RoomOpenHelper.ValidationResult(false, "expenses(com.progprof.bargainmargintemplate.data.local.entities.ExpenseEntity).\n"
                  + " Expected:\n" + _infoExpenses + "\n"
                  + " Found:\n" + _existingExpenses);
        }
        final HashMap<String, TableInfo.Column> _columnsCategories = new HashMap<String, TableInfo.Column>(4);
        _columnsCategories.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("categoryName", new TableInfo.Column("categoryName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("totalBudget", new TableInfo.Column("totalBudget", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("budgetRemaining", new TableInfo.Column("budgetRemaining", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCategories = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCategories = new TableInfo("categories", _columnsCategories, _foreignKeysCategories, _indicesCategories);
        final TableInfo _existingCategories = TableInfo.read(db, "categories");
        if (!_infoCategories.equals(_existingCategories)) {
          return new RoomOpenHelper.ValidationResult(false, "categories(com.progprof.bargainmargintemplate.data.local.entities.CategoryEntity).\n"
                  + " Expected:\n" + _infoCategories + "\n"
                  + " Found:\n" + _existingCategories);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "b0c650720baff5204ee74e58ff77fc11", "ccaed27ac6e9aff9ee6042f3cc0ee04b");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "months","weeks","expenses","categories");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `months`");
      _db.execSQL("DELETE FROM `weeks`");
      _db.execSQL("DELETE FROM `expenses`");
      _db.execSQL("DELETE FROM `categories`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(BudgetDao.class, BudgetDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CategoryDao.class, CategoryDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public BudgetDao budgetDao() {
    if (_budgetDao != null) {
      return _budgetDao;
    } else {
      synchronized(this) {
        if(_budgetDao == null) {
          _budgetDao = new BudgetDao_Impl(this);
        }
        return _budgetDao;
      }
    }
  }

  @Override
  public CategoryDao categoryDao() {
    if (_categoryDao != null) {
      return _categoryDao;
    } else {
      synchronized(this) {
        if(_categoryDao == null) {
          _categoryDao = new CategoryDao_Impl(this);
        }
        return _categoryDao;
      }
    }
  }
}
