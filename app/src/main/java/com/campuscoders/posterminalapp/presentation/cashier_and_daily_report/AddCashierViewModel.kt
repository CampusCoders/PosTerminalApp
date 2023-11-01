package com.campuscoders.posterminalapp.presentation.cashier_and_daily_report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.domain.model.TerminalUsers
import com.campuscoders.posterminalapp.domain.use_case.cashier_and_report.FetchLastTerminalUserIdUseCase
import com.campuscoders.posterminalapp.domain.use_case.cashier_and_report.FetchTerminalUserByIdUseCase
import com.campuscoders.posterminalapp.domain.use_case.cashier_and_report.UpdateCashierUseCase
import com.campuscoders.posterminalapp.domain.use_case.login.SaveTerminalUserUseCase
import com.campuscoders.posterminalapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCashierViewModel @Inject constructor(
    private val fetchTerminalUserByIdUseCase: FetchTerminalUserByIdUseCase,
    private val saveTerminalUserUseCase: SaveTerminalUserUseCase,
    private val fetchLastTerminalUserIdUseCase: FetchLastTerminalUserIdUseCase,
    private val updateCashierUseCase: UpdateCashierUseCase
): ViewModel() {

    private var _statusFetchedTerminalUser = MutableLiveData<Resource<TerminalUsers>>()
    val statusFetchedTerminalUser: LiveData<Resource<TerminalUsers>>
        get() = _statusFetchedTerminalUser

    private var _statusSaveTerminalUser = MutableLiveData<Resource<Boolean>>()
    val statusSaveTerminalUser: LiveData<Resource<Boolean>>
        get() = _statusSaveTerminalUser

    private var _statusLastTerminalUserId = MutableLiveData<Resource<Int>>()
    val statusLastTerminalUserId: LiveData<Resource<Int>>
        get() = _statusLastTerminalUserId

    fun getTerminalUser(terminalId: String) {
        _statusFetchedTerminalUser.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = fetchTerminalUserByIdUseCase.executeFetchTerminalUserById(terminalId)
            _statusFetchedTerminalUser.value = response
        }
    }

    fun saveTerminalUser(terminalUser: TerminalUsers) {
        _statusSaveTerminalUser.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = saveTerminalUserUseCase.executeSaveTerminalUser(terminalUser)
            _statusSaveTerminalUser.value = response
        }
    }

    fun getLastTerminalUsersId() {
        _statusLastTerminalUserId.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = fetchLastTerminalUserIdUseCase.executeFetchLastTerminalUserId()
            _statusLastTerminalUserId.value = response
        }
    }

    fun updateTerminalUser(terminalUser: TerminalUsers) {
        _statusSaveTerminalUser.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = updateCashierUseCase.executeUpdateCashier(terminalUser)
            _statusSaveTerminalUser.value = response
        }
    }

    fun resetSaveTerminalUser() {
        _statusSaveTerminalUser = MutableLiveData<Resource<Boolean>>()
    }

    fun resetFetchedTerminalUser() {
        _statusFetchedTerminalUser = MutableLiveData<Resource<TerminalUsers>>()
    }
}