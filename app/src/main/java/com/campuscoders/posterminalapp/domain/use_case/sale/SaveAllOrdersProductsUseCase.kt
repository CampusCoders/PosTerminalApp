package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.model.OrdersProducts
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class SaveAllOrdersProductsUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun executeSaveAllOrdersProducts(vararg ordersProducts: OrdersProducts): Resource<Boolean> {
        return try {
            val response = repository.saveAllOrdersProductsToDatabase(*ordersProducts)
            if (response.isNotEmpty()) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Failed to insert OrdersProducts")
            }
        } catch (e: Exception) {
            Resource.Error(false,e.localizedMessage?:"Error - (executeSaveAllOrdersProducts)")
        }
    }
}