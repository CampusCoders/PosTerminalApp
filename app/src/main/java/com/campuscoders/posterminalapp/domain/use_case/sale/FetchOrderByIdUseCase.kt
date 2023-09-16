package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchOrderByIdUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun executeFetchOrderById(orderId: String): Resource<Orders> {
        return try {
            val response = repository.fetchOrderById(orderId)
            response?.let {
                return@let Resource.Success(it)
            } ?: Resource.Error(null,"Failed to fetch Order")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error - (executeFetchOrderById)")
        }
    }
}