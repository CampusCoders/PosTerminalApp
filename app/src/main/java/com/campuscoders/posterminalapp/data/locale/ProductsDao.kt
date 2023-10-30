package com.campuscoders.posterminalapp.data.locale

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.campuscoders.posterminalapp.domain.model.Products

@Dao
interface ProductsDao {

    @Insert
    suspend fun insertAllProducts(vararg products: Products): List<Long>

    @Insert
    suspend fun insertProduct(product: Products): Long

    @Query("SELECT * FROM Products")
    suspend fun getProducts(): List<Products>?

    @Query("SELECT * FROM Products WHERE product_category_id = :categoryId")
    suspend fun getProductsByCategoryId(categoryId: String): List<Products>?

    @Query("SELECT * FROM Products WHERE productId = :productId")
    suspend fun getProductByProductId(productId: String): Products?

    @Query("DELETE FROM Products WHERE productId = :productId")
    suspend fun deleteProductById(productId: Int): Int

    @Query("SELECT * FROM Products WHERE product_barcode = :productBarcode")
    suspend fun queryProductByBarcode(productBarcode: String): Products?

    @Query("DELETE FROM Products WHERE product_category_id = :categoryId")
    suspend fun deleteProductByCategoryId(categoryId: Int): Int
}