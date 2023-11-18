package com.campuscoders.posterminalapp.domain.repository.locale

import com.campuscoders.posterminalapp.domain.model.Categories

interface EditRepository {

    suspend fun deleteCategoryById(categoryId: Int): Int

    suspend fun deleteProductById(productId: Int): Int

    suspend fun fetchCategoryById(categoryId: String): Categories?

    suspend fun deleteProductByCategoryId(categoryId: Int): Int
}