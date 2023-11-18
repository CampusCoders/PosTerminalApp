package com.campuscoders.posterminalapp.presentation.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.domain.model.Categories
import com.campuscoders.posterminalapp.domain.use_case.edit.FetchCategoryByIdUseCase
import com.campuscoders.posterminalapp.domain.use_case.edit.UpdateCategoryUseCase
import com.campuscoders.posterminalapp.domain.use_case.sale.SaveCategoryUseCase
import com.campuscoders.posterminalapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateOrAddCategoryViewModel @Inject constructor(
    private val fetchCategoryByIdUseCase: FetchCategoryByIdUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val saveCategoryUseCase: SaveCategoryUseCase
) : ViewModel() {

    private var _statusFetchedCategory = MutableLiveData<Resource<Categories>>()
    val statusFetchedCategory: LiveData<Resource<Categories>>
        get() = _statusFetchedCategory

    private var _statusAddCategory = MutableLiveData<Resource<Boolean>>()
    val statusAddCategory : LiveData<Resource<Boolean>>
        get() = _statusAddCategory

    private var _statusUpdateCategory = MutableLiveData<Resource<String>>()
    val statusUpdateCategory : LiveData<Resource<String>>
        get() = _statusUpdateCategory

    fun getCategory(categoryId: String) {
        _statusFetchedCategory.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = fetchCategoryByIdUseCase.executeFetchCategoryById(categoryId)
            _statusFetchedCategory.value = response
        }
    }

    fun addCategory(category: Categories) {
        _statusAddCategory.value = Resource.Loading()
        viewModelScope.launch {
            val response = saveCategoryUseCase.executeSaveCategory(category)
            _statusAddCategory.value = response
        }
    }

    fun updateCategory(category: Categories) {
        _statusUpdateCategory.value = Resource.Loading()
        viewModelScope.launch {
            val response = updateCategoryUseCase.executeUpdateCategory(category)
            _statusUpdateCategory.value = response
        }
    }
}