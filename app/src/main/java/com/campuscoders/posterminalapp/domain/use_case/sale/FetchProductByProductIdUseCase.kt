package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.model.Products
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchProductByProductIdUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun executeFetchProductByProductId(productId: String): Resource<Products> {
        return try {
            val response = repository.fetchProductByProductId(productId)
            response?.let {
                return@let Resource.Success(it)
            } ?: Resource.Error(null,"No Data (Products)")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error - Exception (executeFetchProductByProductId)")
        }
    }
}