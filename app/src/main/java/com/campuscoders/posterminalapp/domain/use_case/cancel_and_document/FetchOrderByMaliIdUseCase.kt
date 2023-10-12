package com.campuscoders.posterminalapp.domain.use_case.cancel_and_document

import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.domain.repository.locale.DocumentRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchOrderByMaliIdUseCase @Inject constructor(private val repository: DocumentRepository) {
    suspend fun executeFetchOrderByMaliId(orderMaliId: String): Resource<Orders> {
        return try {
            val response = repository.fetchOrderDetailsByMaliId(orderMaliId)
            response?.let {
                return@let Resource.Success(it)
            } ?: Resource.Error(null,"No Order data")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Order Exception")
        }
    }
}