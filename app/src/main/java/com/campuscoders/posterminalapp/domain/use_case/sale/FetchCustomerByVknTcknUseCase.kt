package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.model.Customers
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchCustomerByVknTcknUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun executeFetchCustomerByVknTckn(customerVknTckn: String): Resource<Customers> {
        return try {
            val response = repository.fetchCustomerByVknTckn(customerVknTckn)
            response?.let {
                return@let Resource.Success(it)
            } ?: Resource.Error(null,"No Customer data")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error - (executeFetchCustomerById)")
        }
    }
}