package com.campuscoders.posterminalapp.domain.use_case.edit

import com.campuscoders.posterminalapp.domain.model.Categories
import com.campuscoders.posterminalapp.domain.repository.locale.EditRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchCategoryByIdUseCase @Inject constructor(private val repository: EditRepository) {
    suspend fun executeFetchCategoryById(categoryId: String): Resource<Categories> {
        return try {
            val response = repository.fetchCategoryById(categoryId)
            response?.let {
                Resource.Success(it)
            } ?: Resource.Error(null,"Failed to fetch Category!")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error - (executeFetchCategoryById)")
        }
    }
}