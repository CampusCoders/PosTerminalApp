package com.campuscoders.posterminalapp.domain.use_case.edit

import com.campuscoders.posterminalapp.domain.model.Products
import com.campuscoders.posterminalapp.domain.repository.locale.EditRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(private val repository: EditRepository) {
    suspend fun executeUpdateProduct(products: Products): Resource<String> {
        return try {
            val response = repository.updateProduct(products)
            if (response > 0) Resource.Success("Ürün güncellendi.")
            else Resource.Error(null,"Güncelleme işlemi başarısız.")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error! - executeUpdateProduct")
        }
    }
}