package com.campuscoders.posterminalapp.presentation.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.domain.model.Products
import com.campuscoders.posterminalapp.domain.use_case.edit.UpdateProductUseCase
import com.campuscoders.posterminalapp.domain.use_case.sale.FetchProductByProductIdUseCase
import com.campuscoders.posterminalapp.domain.use_case.sale.SaveProductUseCase
import com.campuscoders.posterminalapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateOrAddProductViewModel @Inject constructor(
    private val updateProductUseCase: UpdateProductUseCase,
    private val saveProductUseCase: SaveProductUseCase,
    private val fetchProductByProductIdUseCase: FetchProductByProductIdUseCase
) : ViewModel() {

    private var _statusFetchedProduct = MutableLiveData<Resource<Products>>()
    val statusFetchedProduct: LiveData<Resource<Products>>
        get() = _statusFetchedProduct

    private var _statusAddProduct = MutableLiveData<Resource<Boolean>>()
    val statusAddProduct: LiveData<Resource<Boolean>>
        get() = _statusAddProduct

    private var _statusUpdateProduct = MutableLiveData<Resource<String>>()
    val statusUpdateProduct: LiveData<Resource<String>>
        get() = _statusUpdateProduct

    fun getProduct(productId: String) {
        _statusFetchedProduct.value = Resource.Loading()
        viewModelScope.launch {
            val response = fetchProductByProductIdUseCase.executeFetchProductByProductId(productId)
            _statusFetchedProduct.value = response
        }
    }

    fun addProduct(products: Products) {
        _statusAddProduct.value = Resource.Loading()
        viewModelScope.launch {
            val response = saveProductUseCase.executeSaveProduct(products)
            _statusAddProduct.value = response
        }
    }

    fun updateProduct(products: Products) {
        _statusUpdateProduct.value = Resource.Loading()
        viewModelScope.launch {
            val response = updateProductUseCase.executeUpdateProduct(products)
            _statusUpdateProduct.value = response
        }
    }
}