package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.model.Products
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class SaveProductUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun executeSaveProduct(product: Products): Resource<Boolean> {
        return try {
            val response = repository.saveProductToDatabase(product)
            if (response > 0) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Failed to insert product")
            }
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"Error - Exception (executeSaveProduct)")
        }
    }
}