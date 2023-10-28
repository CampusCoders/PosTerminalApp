package com.campuscoders.posterminalapp.presentation.cashier_and_daily_report

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.domain.use_case.cashier_and_report.FetchOrdersDynamicallyUseCase
import com.campuscoders.posterminalapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyReportViewModel @Inject constructor(
    private val fetchOrdersDynamicallyUseCase: FetchOrdersDynamicallyUseCase
): ViewModel() {

    private var _statusOrderList = MutableLiveData<Resource<List<Orders>>>()
    val statusOrderList: LiveData<Resource<List<Orders>>>
        get() = _statusOrderList

    fun fetchOrders(orderStatus: String?, orderReceiptType: String?, orderDate: String?, context: Context) {
        _statusOrderList.value = Resource.Loading(null)

        var status: String? = orderStatus
        var receiptType: String? = orderReceiptType
        var date: String? = orderDate

        if (status == context.getString(R.string.adapter_all_result)) status = null
        if (receiptType == context.getString(R.string.adapter_all_documents)) receiptType = null

        status = when(status) {
            context.getString(R.string.adapter_successful) -> {
                "Successful"
            }
            context.getString(R.string.adapter_basket_cancel) -> {
                "Basket Cancel"
            }
            context.getString(R.string.adapter_cancel_sale) -> {
                "Sale Cancel"
            }
            null -> null
            else -> ""
        }
        viewModelScope.launch {
            val response = fetchOrdersDynamicallyUseCase.executeFetchOrdersDynamically(status, receiptType, date)
            _statusOrderList.value = response
        }
    }
}