package com.campuscoders.posterminalapp.data.repository.locale

import com.campuscoders.posterminalapp.data.locale.OrdersDao
import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.domain.repository.locale.DocumentRepository
import javax.inject.Inject

class DocumentRepositoryImpl @Inject constructor(
    private val ordersDao: OrdersDao
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
}