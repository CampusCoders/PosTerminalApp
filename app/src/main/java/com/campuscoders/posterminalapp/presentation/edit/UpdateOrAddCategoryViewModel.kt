package com.campuscoders.posterminalapp.presentation.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.domain.model.Categories
import com.campuscoders.posterminalapp.domain.use_case.edit.FetchCategoryByIdUseCase
import com.campuscoders.posterminalapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateOrAddCategoryViewModel @Inject constructor(
    private val fetchCategoryByIdUseCase: FetchCategoryByIdUseCase
) : ViewModel() {

    private var _statusFetchedCategory = MutableLiveData<Resource<Categories>>()
    val statusFetchedCategory: LiveData<Resource<Categories>>
        get() = _statusFetchedCategory

    fun getCategory(categoryId: String) {
        _statusFetchedCategory.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = fetchCategoryByIdUseCase.executeFetchCategoryById(categoryId)
            _statusFetchedCategory.value = response
        }
    }
}