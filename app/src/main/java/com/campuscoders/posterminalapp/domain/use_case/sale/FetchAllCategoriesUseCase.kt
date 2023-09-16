package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.model.Categories
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchAllCategoriesUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun executeFetchAllCategories(): Resource<List<Categories>> {
        return try {
            val response = repository.fetchAllCategories()
            response?.let {
                if (it.isNotEmpty()) {
                    Resource.Success(it)
                } else {
                    Resource.Error(null,"Failed to fetch Categories")
                }
            } ?: Resource.Error(null,"No Data (Categories)")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error - Exception (executeFetchAllCategories)")
        }
    }
}