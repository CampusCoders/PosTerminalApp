package com.campuscoders.posterminalapp.domain.repository.locale

import com.campuscoders.posterminalapp.domain.model.Categories
import com.campuscoders.posterminalapp.domain.model.Products

interface EditRepository {

    suspend fun deleteCategoryById(categoryId: Int): Int

    suspend fun deleteProductById(productId: Int): Int

    suspend fun fetchCategoryById(categoryId: String): Categories?

    suspend fun deleteProductByCategoryId(categoryId: Int): Int

    suspend fun updateCategory(categories: Categories): Int

    suspend fun updateProduct(products: Products): Int
}