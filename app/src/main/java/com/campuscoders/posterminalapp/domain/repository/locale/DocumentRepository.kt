package com.campuscoders.posterminalapp.domain.repository.locale

import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.domain.model.OrdersProducts

interface DocumentRepository {

    suspend fun fetchOrderDetailsByReceiptNo(orderReceiptNo: String): Orders?

    suspend fun fetchOrderDetailsByMaliId(orderMaliId: String): Orders?

    suspend fun fetchOrderDetailsByTerminalId(orderTerminalId: String): Orders?

    suspend fun fetchLatestSuccessfulSale(): Orders?

    suspend fun fetchOrdersProductsByOrderId(orderId: String): List<OrdersProducts>?
}