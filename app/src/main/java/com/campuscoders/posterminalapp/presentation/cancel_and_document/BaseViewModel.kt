package com.campuscoders.posterminalapp.presentation.cancel_and_document

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.domain.use_case.cancel_and_document.FetchOrderByMaliIdUseCase
import com.campuscoders.posterminalapp.domain.use_case.cancel_and_document.FetchOrderByReceiptNoUseCase
import com.campuscoders.posterminalapp.domain.use_case.cancel_and_document.FetchOrderByTerminalIdUseCase
import com.campuscoders.posterminalapp.utils.Resource
import kotlinx.coroutines.launch

class BaseViewModel(
    private val fetchOrderByReceiptNoUseCase: FetchOrderByReceiptNoUseCase,
    private val fetchOrderByMaliIdUseCase: FetchOrderByMaliIdUseCase,
    private val fetchOrderByTerminalIdUseCase: FetchOrderByTerminalIdUseCase
): ViewModel() {

    private var _statusOrderDetail = MutableLiveData<Resource<Orders>>()
    val statusOrderDetail: LiveData<Resource<Orders>>
        get() = _statusOrderDetail

    fun querySale(searchType: String, searchKey: String) {
        _statusOrderDetail.value = Resource.Loading(null)
        viewModelScope.launch {
            when(searchType) {
                "receiptNo" -> {
                    val response = fetchOrderByReceiptNoUseCase.executeFetchOrderByReceiptNo(searchKey)
                    _statusOrderDetail.value = response
                }
                "orderMaliId" -> {
                    val response = fetchOrderByMaliIdUseCase.executeFetchOrderByMaliId(searchKey)
                    _statusOrderDetail.value = response
                }
                "terminalId" -> {
                    val response = fetchOrderByTerminalIdUseCase.executeFetchOrderByTerminal(searchKey)
                    _statusOrderDetail.value = response
                }
            }
        }
    }

    fun fetchLatestSuccessfulSale() {
        _statusOrderDetail.value = Resource.Loading(null)
        viewModelScope.launch {
            // use case
        }
    }
}