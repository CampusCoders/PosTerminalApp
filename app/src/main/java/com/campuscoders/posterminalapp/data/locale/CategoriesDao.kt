package com.campuscoders.posterminalapp.data.locale

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.campuscoders.posterminalapp.domain.model.Categories

@Dao
interface CategoriesDao {

    @Insert
    suspend fun insertAllCategories(vararg categories: Categories): List<Long>

    @Insert
    suspend fun insertCategory(category: Categories): Long

    @Query("SELECT * FROM Categories")
    suspend fun getCategories(): List<Categories>?

    @Query("DELETE FROM Categories WHERE categoryId = :categoryId")
    suspend fun deleteCategoryById(categoryId: Int): Int

    @Query("SELECT * FROM Categories WHERE categoryId = :categoryId")
    suspend fun getCategoryById(categoryId: String): Categories?
}