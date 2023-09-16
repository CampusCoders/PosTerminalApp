package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.model.Categories
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class SaveAllCategoriesUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun executeSaveAllCategories(vararg categories: Categories): Resource<Boolean> {
        return try {
            val response = repository.saveAllCategoriesToDatabase(*categories)
            if (response.isNotEmpty()) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Failed to insert categories")
            }
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"Error - Exception (executeSaveAllCategories)")
        }
    }
}