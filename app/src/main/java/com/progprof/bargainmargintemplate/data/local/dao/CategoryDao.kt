package com.progprof.bargainmargintemplate.data.local.dao
import com.progprof.bargainmargintemplate.data.local.entities.CategoryEntity

import androidx.room.*
import com.progprof.bargainmargintemplate.data.local.entities.CategorySpendingHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Delete
    suspend fun deleteCategory(categoryEntity: CategoryEntity)

    @Update
    suspend fun updateCategory(categoryEntity: CategoryEntity)
}

@Dao
interface CategorySpendingHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshot(snapshot: CategorySpendingHistoryEntity)

    @Query("SELECT * FROM category_spending_history WHERE categoryId = :categoryId ORDER BY year, month")
    fun getSpendingHistoryForCategory(categoryId: Int): Flow<List<CategorySpendingHistoryEntity>>

    @Query("SELECT * FROM category_spending_history WHERE year = :year AND month = :month")
    suspend fun getSnapshotsForMonth(year: Int, month: Int): List<CategorySpendingHistoryEntity>
}
