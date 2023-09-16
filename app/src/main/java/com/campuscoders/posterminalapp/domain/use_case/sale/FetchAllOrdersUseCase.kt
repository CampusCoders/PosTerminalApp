package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchAllOrdersUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun executeFetchAllOrders(): Resource<List<Orders>> {
        return try {
            val response = repository.fetchAllOrders()
            response?.let {
                if (it.isNotEmpty()) {
                    Resource.Success(it)
                } else {
                    Resource.Error(null,"No Orders data")
                }
            } ?: Resource.Error(null,"Failed to fetch Orders")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error - (executeFetchAllOrders)")
        }
    }
}