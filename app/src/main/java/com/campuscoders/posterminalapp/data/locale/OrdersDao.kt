package com.campuscoders.posterminalapp.data.locale

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.campuscoders.posterminalapp.domain.model.Orders

@Dao
interface OrdersDao {

    @Insert
    suspend fun insertOrderToDatabase(order: Orders): Long

    @Query("SELECT * FROM Orders")
    suspend fun getOrders(): List<Orders>?

    @Query("SELECT * FROM Orders WHERE order_customer_id = :customerId")
    suspend fun getOrderByCustomerId(customerId: String): Orders?

    @Query("SELECT * FROM Orders WHERE orderId = :orderId")
    suspend fun getOrderById(orderId: String): Orders?

    @Query("UPDATE Orders SET order_receipt_type = :orderReceiptType, order_date = :orderDate, order_time = :orderTime, order_status = :orderStatus, order_receipt_no = :orderReceiptNo WHERE orderId = :orderId")
    suspend fun updateOrder(orderReceiptType: String, orderDate: String, orderTime: String, orderStatus: String, orderReceiptNo: String, orderId: String): Int
}