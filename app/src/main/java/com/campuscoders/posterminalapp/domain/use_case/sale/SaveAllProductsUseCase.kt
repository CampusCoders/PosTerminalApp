package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.model.Products
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class SaveAllProductsUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun executeSaveAllProducts(vararg products: Products): Resource<Boolean> {
        return try {
            val response = repository.saveAllProductsToDatabase(*products)
            if (response.isNotEmpty()) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Failed to insert products")
            }
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"Error - Exception (executeSaveAllProducts)")
        }
    }
}