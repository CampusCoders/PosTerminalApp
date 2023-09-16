package com.campuscoders.posterminalapp.domain.use_case.edit

import com.campuscoders.posterminalapp.domain.repository.locale.EditRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class DeleteProductByIdUseCase @Inject constructor(private val repository: EditRepository) {
    suspend fun executeDeleteProductById(productId: Int): Resource<Boolean> {
        return try {
            val response = repository.deleteProductById(productId)
            if (response > 0) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Failed to delete Product")
            }
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"Error - (executeDeleteProductById)")
        }
    }
}