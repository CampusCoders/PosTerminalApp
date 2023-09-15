package com.campuscoders.posterminalapp.data.locale

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.campuscoders.posterminalapp.domain.model.OrdersProducts

@Dao
interface OrdersProductsDao {

    @Insert
    suspend fun insertAllOrdersProducts(vararg ordersProducts: OrdersProducts): List<Long>

    @Query("SELECT * FROM OrdersProducts")
    suspend fun getOrdersProducts(): List<OrdersProducts>?
}