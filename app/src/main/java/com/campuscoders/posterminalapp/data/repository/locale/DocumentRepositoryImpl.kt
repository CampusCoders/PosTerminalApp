package com.campuscoders.posterminalapp.data.repository.locale

import com.campuscoders.posterminalapp.data.locale.OrdersDao
import com.campuscoders.posterminalapp.data.locale.OrdersProductsDao
import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.domain.model.OrdersProducts
import com.campuscoders.posterminalapp.domain.repository.locale.DocumentRepository
import javax.inject.Inject

class DocumentRepositoryImpl @Inject constructor(
    private val ordersDao: OrdersDao,
    private val ordersProductsDao: OrdersProductsDao
): DocumentRepository {

    override suspend fun fetchOrderDetailsByReceiptNo(orderReceiptNo: String): Orders? {
        return ordersDao.getOrderByReceiptNo(orderReceiptNo)
    }

    override suspend fun fetchOrderDetailsByMaliId(orderMaliId: String): Orders? {
        return ordersDao.getOrderByMaliId(orderMaliId)
    }

    override suspend fun fetchOrderDetailsByTerminalId(orderTerminalId: String): Orders? {
        return ordersDao.getOrderByTerminalId(orderTerminalId)
    }

    override suspend fun fetchLatestSuccessfulSale(): Orders? {
        return ordersDao.getLatestSuccessfulSale()
    }

    override suspend fun fetchOrdersProductsByOrderId(orderId: String): List<OrdersProducts>? {
        return ordersProductsDao.getOrdersProductsByOrderId(orderId)
    }

    override suspend fun cancelSale(orderId: String): Int {
        return ordersDao.updateOrderStatusAsSaleCancel(orderId)
    }
}