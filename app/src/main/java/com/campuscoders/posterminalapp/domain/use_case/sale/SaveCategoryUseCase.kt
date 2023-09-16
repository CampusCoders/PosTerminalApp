package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.model.Categories
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class SaveCategoryUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun executeSaveCategory(category: Categories): Resource<Boolean> {
        return try {
            val response = repository.saveCategoryToDatabase(category)
            if (response > 0) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Failed to insert category")
            }
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"Error - Exception (executeSaveCategory)")
        }
    }
}