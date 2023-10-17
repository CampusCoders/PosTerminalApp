package com.campuscoders.posterminalapp.domain.use_case.cancel_and_document

import com.campuscoders.posterminalapp.domain.model.OrdersProducts
import com.campuscoders.posterminalapp.domain.repository.locale.DocumentRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchOrderProductsByOrderIdUseCase @Inject constructor(private val repository: DocumentRepository) {
    suspend fun executeFetchOrderProductsByOrderId(orderId: String): Resource<List<OrdersProducts>> {
        return try {
            val response = repository.fetchOrdersProductsByOrderId(orderId)
            response?.let {
                return@let Resource.Success(it)
            } ?: Resource.Error(null,"Error OrdersProducts")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error")
        }
    }
}