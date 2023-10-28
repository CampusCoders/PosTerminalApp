package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class UpdateOrderUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun executeUpdateOrder(orderReceiptType: String, orderDate: String, orderTime: String, orderStatus: String, orderReceiptNo: String, orderId: String, orderTotal: String, orderTotalTax: String): Resource<Boolean> {
        return try {
            val response = repository.updateOrder(orderReceiptType, orderDate, orderTime, orderStatus, orderReceiptNo, orderId, orderTotal, orderTotalTax)
            if (response > 0) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Order update failed")
            }
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"Error - (executeUpdateOrder)")
        }
    }
}