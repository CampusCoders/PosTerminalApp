package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.model.Customers
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class SaveCustomerUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun executeSaveCustomer(customer: Customers): Resource<Long> {
        return try {
            val response = repository.saveCustomerToDatabase(customer)
            if (response > 0) {
                Resource.Success(response)
            } else {
                Resource.Error(null,"Failed to insert customer")
            }
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage ?: "Error - Exception (executeSaveCustomer)")
        }
    }
}