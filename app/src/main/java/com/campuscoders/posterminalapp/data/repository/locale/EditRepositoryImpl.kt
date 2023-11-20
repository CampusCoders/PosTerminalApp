package com.campuscoders.posterminalapp.data.repository.locale

import com.campuscoders.posterminalapp.data.locale.CategoriesDao
import com.campuscoders.posterminalapp.data.locale.ProductsDao
import com.campuscoders.posterminalapp.domain.model.Categories
import com.campuscoders.posterminalapp.domain.model.Products
import com.campuscoders.posterminalapp.domain.repository.locale.EditRepository
import javax.inject.Inject

class EditRepositoryImpl @Inject constructor(
    private val categoriesDao: CategoriesDao,
    private val productsDao: ProductsDao
): EditRepository {

    override suspend fun deleteCategoryById(categoryId: Int): Int {
        return categoriesDao.deleteCategoryById(categoryId)
    }

    override suspend fun deleteProductById(productId: Int): Int {
        return productsDao.deleteProductById(productId)
    }

    override suspend fun fetchCategoryById(categoryId: String): Categories? {
        return categoriesDao.getCategoryById(categoryId)
    }

    override suspend fun deleteProductByCategoryId(categoryId: Int): Int {
        return productsDao.deleteProductByCategoryId(categoryId)
    }

    override suspend fun updateCategory(categories: Categories): Int {
        return categoriesDao.updateCategory(categories)
    }

    override suspend fun updateProduct(products: Products): Int {
        return productsDao.updateProduct(products)
    }
}