package com.campuscoders.posterminalapp.presentation.cancel_and_document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.domain.use_case.cancel_and_document.FetchOrderByMaliIdUseCase
import com.campuscoders.posterminalapp.domain.use_case.cancel_and_document.FetchOrderByReceiptNoUseCase
import com.campuscoders.posterminalapp.domain.use_case.cancel_and_document.FetchOrderByTerminalIdUseCase
import kotlinx.coroutines.launch

class BaseViewModel(
    private val fetchOrderByReceiptNoUseCase: FetchOrderByReceiptNoUseCase,
    private val fetchOrderByMaliIdUseCase: FetchOrderByMaliIdUseCase,
    private val fetchOrderByTerminalIdUseCase: FetchOrderByTerminalIdUseCase
): ViewModel() {

    fun querySale(searchType: String, searchKey: String) {
        // set live data value to loading
        viewModelScope.launch {
            when(searchType) {
                "receiptNo" -> {
                    val response = fetchOrderByReceiptNoUseCase.executeFetchOrderByReceiptNo(searchKey)
                }
                "orderMaliId" -> {
                    val response = fetchOrderByMaliIdUseCase.executeFetchOrderByMaliId(searchKey)
                }
                "terminalId" -> {
                    val response = fetchOrderByTerminalIdUseCase.executeFetchOrderByTerminal(searchKey)
                }
            }
        }
    }
}