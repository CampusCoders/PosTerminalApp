package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.model.Products
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchAllProductsByCategoryIdUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun executeFetchAllProductsByCategoryId(categoryId: String): Resource<List<Products>> {
        return try {
            val response = repository.fetchAllProductsByCategoryId(categoryId)
            response?.let {
                if (it.isNotEmpty()) {
                    Resource.Success(it)
                } else {
                    Resource.Error(null,"Failed to fetch Products")
                }
            } ?: Resource.Error(null,"No Data (Products)")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error - Exception (executeFetchAllProductsByCategoryId)")
        }
    }
}