package com.campuscoders.posterminalapp.presentation.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.domain.model.Products
import com.campuscoders.posterminalapp.domain.use_case.edit.DeleteProductByIdUseCase
import com.campuscoders.posterminalapp.domain.use_case.sale.FetchAllProductsByCategoryIdUseCase
import com.campuscoders.posterminalapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProductViewModel @Inject constructor(
    private val fetchAllProductsByCategoryIdUseCase: FetchAllProductsByCategoryIdUseCase,
    private val deleteProductByIdUseCase: DeleteProductByIdUseCase
): ViewModel() {

    private var _statusProductsList = MutableLiveData<Resource<List<Products>>>()
    val statusProductsList: LiveData<Resource<List<Products>>>
        get() = _statusProductsList

    private var _statusDeleteProduct = MutableLiveData<Resource<Boolean>>()
    val statusDeleteProducts: LiveData<Resource<Boolean>>
        get() = _statusDeleteProduct

    fun getProductsByCategoryId(categoryId: String) {
        _statusProductsList.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = fetchAllProductsByCategoryIdUseCase.executeFetchAllProductsByCategoryId(categoryId)
            _statusProductsList.value = response
        }
    }

    fun deleteProduct(productId: Int) {
        _statusDeleteProduct.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = deleteProductByIdUseCase.executeDeleteProductById(productId)
            _statusDeleteProduct.value = response
        }
    }
}