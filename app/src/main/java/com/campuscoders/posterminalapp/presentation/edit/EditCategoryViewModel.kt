package com.campuscoders.posterminalapp.presentation.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.domain.model.Categories
import com.campuscoders.posterminalapp.domain.use_case.edit.DeleteCategoryByIdUseCase
import com.campuscoders.posterminalapp.domain.use_case.edit.DeleteProductByCategoryIdUseCase
import com.campuscoders.posterminalapp.domain.use_case.sale.FetchAllCategoriesUseCase
import com.campuscoders.posterminalapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCategoryViewModel @Inject constructor(
    private val fetchAllCategoriesUseCase: FetchAllCategoriesUseCase,
    private val deleteCategoryByIdUseCase: DeleteCategoryByIdUseCase,
    private val deleteProductByCategoryIdUseCase: DeleteProductByCategoryIdUseCase
): ViewModel() {

    private var _statusCategoriesList = MutableLiveData<Resource<List<Categories>>>()
    val statusCategoriesList: LiveData<Resource<List<Categories>>>
        get() = _statusCategoriesList

    private var _statusDeleteCategory = MutableLiveData<Resource<Boolean>>()
    val statusDeleteCategory: LiveData<Resource<Boolean>>
        get() = _statusDeleteCategory

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

    fun deleteCategory(categoryId: Int) {
        _statusDeleteCategory.value = Resource.Loading(null)
        viewModelScope.launch {
            val responseFromDeleteCategory = deleteCategoryByIdUseCase.executeDeleteCategoryById(categoryId)
            if (responseFromDeleteCategory is Resource.Success) {
                val responseFromDeleteProduct = deleteProductByCategoryIdUseCase.executeDeleteProductByCategoryId(categoryId)
                _statusDeleteCategory.value = responseFromDeleteProduct
            } else if (responseFromDeleteCategory is Resource.Error) {
                _statusDeleteCategory.value = responseFromDeleteCategory
            }
        }
    }
}