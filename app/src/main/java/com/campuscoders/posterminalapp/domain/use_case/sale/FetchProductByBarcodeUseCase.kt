package com.campuscoders.posterminalapp.domain.use_case.sale

import com.campuscoders.posterminalapp.domain.model.Products
import com.campuscoders.posterminalapp.domain.repository.locale.SaleRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchProductByBarcodeUseCase @Inject constructor(private val repository: SaleRepository) {
    suspend fun executeFetchProductByBarcode(barcode: String): Resource<Products> {
        return try {
            val response = repository.fetchProductByBarcode(barcode)
            response?.let {
                Resource.Success(it)
            } ?: Resource.Error(null,"Ürün bulunamadı.")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error - (executeFetchProductByBarcode)")
        }
    }
}