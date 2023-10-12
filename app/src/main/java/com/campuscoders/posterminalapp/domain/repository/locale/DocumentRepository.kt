package com.campuscoders.posterminalapp.domain.repository.locale

import com.campuscoders.posterminalapp.domain.model.Orders

interface DocumentRepository {

    suspend fun fetchOrderDetailsByReceiptNo(orderReceiptNo: String): Orders?

    suspend fun fetchOrderDetailsByMaliId(orderMaliId: String): Orders?

    suspend fun fetchOrderDetailsByTerminalId(orderTerminalId: String): Orders?
}