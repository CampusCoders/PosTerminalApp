package com.campuscoders.posterminalapp.presentation.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.domain.use_case.sale.UpdateOrderUseCase
import com.campuscoders.posterminalapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val updateOrderUseCase: UpdateOrderUseCase
): ViewModel() {

    private var _statusUpdateOrder = MutableLiveData<Resource<Boolean>>()
    val statusUpdateOrder: LiveData<Resource<Boolean>>
        get() = _statusUpdateOrder

    fun updateOrder(orderReceiptType: String, orderDate: String, orderTime: String, orderStatus: String, orderReceiptNo: String, orderId: String, orderTotal: String, orderTotalTax: String, orderTimestamp: Long) {
        _statusUpdateOrder.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = updateOrderUseCase.executeUpdateOrder(orderReceiptType, orderDate, orderTime, orderStatus, orderReceiptNo, orderId, orderTotal, orderTotalTax, orderTimestamp)
            _statusUpdateOrder.value = response
        }
    }
}