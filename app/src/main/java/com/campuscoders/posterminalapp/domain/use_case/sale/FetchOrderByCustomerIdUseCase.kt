package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchOrderByCustomerIdUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun executeFetchOrderByCustomerId(customerId: String): Resource<Orders> {
        return try {
            val response = repository.fetchOrderByCustomerId(customerId)
            response?.let {
                return@let Resource.Success(it)
            } ?: Resource.Error(null,"No Order data")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error - (executeFetchOrderByCustomerId)")
        }
    }
}