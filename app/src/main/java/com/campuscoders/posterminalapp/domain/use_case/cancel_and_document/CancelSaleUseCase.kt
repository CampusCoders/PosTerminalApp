package com.campuscoders.posterminalapp.domain.use_case.cancel_and_document

import com.campuscoders.posterminalapp.domain.repository.locale.DocumentRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class CancelSaleUseCase @Inject constructor(private val repository: DocumentRepository) {
    suspend fun executeCancelSale(orderId: String): Resource<Int> {
        return try {
            val response = repository.cancelSale(orderId)
            if (response > 0) {
                Resource.Success(response)
            } else {
                Resource.Error(null,"Update failed")
            }
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error! (executeCancelSale)")
        }
    }
}