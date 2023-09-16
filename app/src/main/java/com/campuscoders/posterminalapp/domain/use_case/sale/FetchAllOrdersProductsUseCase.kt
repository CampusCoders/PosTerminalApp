package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.model.OrdersProducts
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchAllOrdersProductsUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun executeFetchAllOrdersProducts(): Resource<List<OrdersProducts>> {
        return try {
            val response = repository.fetchAllOrdersProducts()
            response?.let {
                if (it.isNotEmpty()) {
                    Resource.Success(it)
                } else {
                    Resource.Error(null,"No OrdersProducts data")
                }
            } ?: Resource.Error(null,"Failed to fetch OrdersProducts")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error - (executeFetchAllOrdersProducts)")
        }
    }
}