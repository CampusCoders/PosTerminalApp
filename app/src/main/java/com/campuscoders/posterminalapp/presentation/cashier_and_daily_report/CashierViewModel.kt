package com.campuscoders.posterminalapp.presentation.cashier_and_daily_report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.domain.model.TerminalUsers
import com.campuscoders.posterminalapp.domain.use_case.cashier_and_report.DeleteCashierUseCase
import com.campuscoders.posterminalapp.domain.use_case.cashier_and_report.FetchAllCashiersUseCase
import com.campuscoders.posterminalapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CashierViewModel @Inject constructor(
    private val deleteCashierUseCase: DeleteCashierUseCase,
    private val fetchAllCashiersUseCase: FetchAllCashiersUseCase
): ViewModel() {

    private var _statusFetchedCashiersList = MutableLiveData<Resource<List<TerminalUsers>>>()
    val statusFetchedCashiersList: LiveData<Resource<List<TerminalUsers>>>
        get() = _statusFetchedCashiersList

    private var _statusDeleteCashier = MutableLiveData<Resource<Boolean>>()
    val statusDeleteCashier: LiveData<Resource<Boolean>>
        get() = _statusDeleteCashier

    fun getAllCashiers() {
        _statusFetchedCashiersList.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = fetchAllCashiersUseCase.executeFetchAllCashiers()
            _statusFetchedCashiersList.value = response
        }
    }

    fun deleteCashier(terminalId: Int) {
        _statusDeleteCashier.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = deleteCashierUseCase.executeDeleteCashier(terminalId)
            _statusDeleteCashier.value = response
        }
    }
}