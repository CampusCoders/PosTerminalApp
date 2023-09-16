package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.model.Customers
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchAllCustomersUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun executeFetchAllCustomers(): Resource<List<Customers>> {
        return try {
            val response = repository.fetchAllCustomers()
            response?.let {
                if (it.isNotEmpty()) {
                    Resource.Success(it)
                } else {
                    Resource.Error(null,"Failed to fetch Customers")
                }
            } ?: Resource.Error(null,"No data")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error - (executeFetchAllCustomers)")
        }
    }
}