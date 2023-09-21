package com.campuscoders.posterminalapp.presentation.sale

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.domain.model.Categories
import com.campuscoders.posterminalapp.domain.use_case.sale.FetchAllCategoriesUseCase
import com.campuscoders.posterminalapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val fetchAllCategoriesUseCase: FetchAllCategoriesUseCase
): ViewModel() {

    private var _statusCategoriesList = MutableLiveData<Resource<List<Categories>>>()
    val statusCategoriesList: LiveData<Resource<List<Categories>>>
        get() = _statusCategoriesList

    init {
        getCategories()
    }

    private fun getCategories() {
        _statusCategoriesList.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = fetchAllCategoriesUseCase.executeFetchAllCategories()
            _statusCategoriesList.value = response
        }
    }
}