package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.model.Customers
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class UpdateCustomerInfosUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun updateCustomerInfos(customer: Customers): Resource<Boolean> {
        return try {
            val response = repository.updateCustomer(customer)
            if (response > 0) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Customer update failed")
            }
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"Error - (updateCustomerInfos)")
        }
    }
}