package com.campuscoders.posterminalapp.domain.repository.locale

import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.domain.model.TerminalUsers

interface CashierAndReportRepository {

    suspend fun deleteCashier(terminalId: Int): Int

    suspend fun fetchAllCashiers(): List<TerminalUsers>?

    suspend fun fetchLastTerminalUserId(): Int?

    suspend fun fetchTerminalUserById(terminalId: String): TerminalUsers?

    suspend fun fetchOrdersDynamically(orderStatus: String?, orderReceiptType: String?, orderDate: String?): List<Orders>?

    suspend fun updateTerminalUser(terminalUsers: TerminalUsers): Int
}