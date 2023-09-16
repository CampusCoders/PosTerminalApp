package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class SaveOrderUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun executeSaveOrder(order: Orders): Resource<Long> {
        return try {
            val response = repository.saveOrderToDatabase(order)
            if (response > 0) {
                Resource.Success(response)
            } else {
                Resource.Error(null,"Failed to save Order")
            }
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Hata - (executeSaveOrder)")
        }
    }
}