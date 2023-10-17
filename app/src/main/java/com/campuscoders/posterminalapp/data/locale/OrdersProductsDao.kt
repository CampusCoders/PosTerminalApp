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

    @Query("SELECT * FROM OrdersProducts WHERE order_products_order_id = :orderId")
    suspend fun getOrdersProductsByOrderId(orderId: String): List<OrdersProducts>?
}