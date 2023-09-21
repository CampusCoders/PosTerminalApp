package com.campuscoders.posterminalapp.presentation.sale

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.domain.model.Products
import com.campuscoders.posterminalapp.domain.use_case.sale.FetchAllProductsByCategoryIdUseCase
import com.campuscoders.posterminalapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val fetchAllProductsByCategoryIdUseCase: FetchAllProductsByCategoryIdUseCase
) : ViewModel() {

    private var _statusProductsList = MutableLiveData<Resource<List<Products>>>()
    val statusProductsList: LiveData<Resource<List<Products>>>
        get() = _statusProductsList

    fun getProductsByCategoryId(categoryId: String) {
        _statusProductsList.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = fetchAllProductsByCategoryIdUseCase.executeFetchAllProductsByCategoryId(categoryId)
            _statusProductsList.value = response
        }
    }
}