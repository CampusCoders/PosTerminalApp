package com.campuscoders.posterminalapp.domain.use_case.cashier_and_report

import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.domain.repository.locale.CashierAndReportRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchOrdersDynamicallyUseCase @Inject constructor(private val repository: CashierAndReportRepository) {
    suspend fun executeFetchOrdersDynamically(orderStatus: String?, orderReceiptType: String?, orderStartDate: Long?, orderEndDate: Long?): Resource<List<Orders>> {
        return try {
            val response = repository.fetchOrdersDynamically(orderStatus,orderReceiptType,orderStartDate,orderEndDate)
            response?.let {
                Resource.Success(it)
            } ?: Resource.Error(null,"Sipariş bulunamadı.")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Exception!")
        }
    }
}