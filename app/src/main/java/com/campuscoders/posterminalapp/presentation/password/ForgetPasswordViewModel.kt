package com.campuscoders.posterminalapp.presentation.password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.domain.use_case.password.FetchMainUserCellPhoneNumberUseCase
import com.campuscoders.posterminalapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val fetchMainUserCellPhoneNumberUseCase: FetchMainUserCellPhoneNumberUseCase
): ViewModel(){

    private var _statusIsMatched = MutableLiveData<Resource<Boolean>>()
    val statusIsMatched: LiveData<Resource<Boolean>>
        get() = _statusIsMatched

    fun controlMainUserCellPhone(vknTckn: String, cellPhoneNumber: String) {
        _statusIsMatched.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = fetchMainUserCellPhoneNumberUseCase.executeFetchMainUserCellPhoneNumber(vknTckn)
            when(response) {
                is Resource.Success -> {
                    val cellPhoneNumberFromDb = response.data
                    println("cellPhoneNumberFromDb -> $cellPhoneNumberFromDb ---  cellPhoneNumber -> $cellPhoneNumber")
                    cellPhoneNumberFromDb?.let {
                        if (it == cellPhoneNumber) {
                            _statusIsMatched.value = Resource.Success(true)
                        } else {
                            _statusIsMatched.value = Resource.Error(false,"Not Matched!")
                        }
                    }
                }
                is Resource.Loading -> {
                    _statusIsMatched.value = Resource.Loading(null)
                }
                is Resource.Error -> {
                    _statusIsMatched.value = Resource.Error(false,response.message?:"Error CellPhoneNumber")
                }
            }
        }
    }
}