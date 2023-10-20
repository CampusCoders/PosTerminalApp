package com.campuscoders.posterminalapp.presentation.cancel_and_document

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.domain.model.OrdersProducts
import com.campuscoders.posterminalapp.domain.use_case.cancel_and_document.CancelSaleUseCase
import com.campuscoders.posterminalapp.domain.use_case.cancel_and_document.FetchLatestSuccessfulSaleUseCase
import com.campuscoders.posterminalapp.domain.use_case.cancel_and_document.FetchOrderByMaliIdUseCase
import com.campuscoders.posterminalapp.domain.use_case.cancel_and_document.FetchOrderByReceiptNoUseCase
import com.campuscoders.posterminalapp.domain.use_case.cancel_and_document.FetchOrderByTerminalIdUseCase
import com.campuscoders.posterminalapp.domain.use_case.cancel_and_document.FetchOrderProductsByOrderIdUseCase
import com.campuscoders.posterminalapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor (
    private val fetchOrderByReceiptNoUseCase: FetchOrderByReceiptNoUseCase,
    private val fetchOrderByMaliIdUseCase: FetchOrderByMaliIdUseCase,
    private val fetchOrderByTerminalIdUseCase: FetchOrderByTerminalIdUseCase,
    private val fetchLatestSuccessfulSaleUseCase: FetchLatestSuccessfulSaleUseCase,
    private val fetchOrderProductsByOrderIdUseCase: FetchOrderProductsByOrderIdUseCase,
    private val cancelSaleUseCase: CancelSaleUseCase
): ViewModel() {

    private var _statusOrderDetail = MutableLiveData<Resource<Orders>>()
    val statusOrderDetail: LiveData<Resource<Orders>>
        get() = _statusOrderDetail

    private var _statusProductAndTaxPrice = MutableLiveData<Resource<HashMap<String,String>>>()
    val statusProductAndTaxPrice: LiveData<Resource<HashMap<String,String>>>
        get() = _statusProductAndTaxPrice

    private var _statusCancelSale = MutableLiveData<Resource<Int>>()
    val statusCancelSale: LiveData<Resource<Int>>
        get() = _statusCancelSale

    fun querySale(searchType: String, searchKey: String, context: Context) {
        _statusOrderDetail.value = Resource.Loading(null)
        viewModelScope.launch {
            when(searchType) {
                context.getString(R.string.adapter_document_no)-> {
                    val response = fetchOrderByReceiptNoUseCase.executeFetchOrderByReceiptNo(searchKey)
                    _statusOrderDetail.value = response
                }
                context.getString(R.string.adapter_mali_id) -> {
                    val response = fetchOrderByMaliIdUseCase.executeFetchOrderByMaliId(searchKey)
                    _statusOrderDetail.value = response
                }
                context.getString(R.string.adapter_terminal_id) -> {
                    val response = fetchOrderByTerminalIdUseCase.executeFetchOrderByTerminal(searchKey)
                    _statusOrderDetail.value = response
                }
            }
        }
    }

    fun fetchLatestSuccessfulSale() {
        _statusOrderDetail.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = fetchLatestSuccessfulSaleUseCase.executeFetchLatestSuccessfulSale()
            _statusOrderDetail.value = response
        }
    }

    fun fetchOrdersProductsList() {
        viewModelScope.launch {
            if (_statusOrderDetail.value is Resource.Success) {
                val order = (_statusOrderDetail.value as Resource.Success<Orders>).data
                val responseOrdersProductsList = fetchOrderProductsByOrderIdUseCase.executeFetchOrderProductsByOrderId(order?.orderId?.toString()?:"0")
                calculateTotalPriceAndTax(responseOrdersProductsList)
            }
        }
    }

    private fun calculateTotalPriceAndTax(ordersProductsList: Resource<List<OrdersProducts>>) {
        val hashMap = hashMapOf<String, String>()
        if (ordersProductsList is Resource.Success) {
            var totalPrice = 0
            var totalPriceCent = 0
            var totalTax = 0
            var totalTaxCent = 0
            for (i in ordersProductsList.data!!) {
                totalPrice += i.orderProductsPrice!!.toInt()
                totalPriceCent += i.orderProductsPriceCents!!.toInt()
                totalTax += i.orderProductsKdvPrice!!.toInt()
                totalTaxCent += i.orderProductsKdvPriceCents!!.toInt()
            }
            totalPrice += totalPriceCent / 100
            totalTax += totalTaxCent / 100
            hashMap["price"] = "$totalPrice,${totalPriceCent % 100}"
            hashMap["tax"] = "$totalTax,${totalTaxCent % 100}"
            _statusProductAndTaxPrice.value = Resource.Success(hashMap)
        }
        else {
            _statusProductAndTaxPrice.value = Resource.Error(null,ordersProductsList.message?:"Error")
        }
    }

    fun cancelSale() {
        _statusCancelSale.value = Resource.Loading(null)
        viewModelScope.launch {
            delay(2000)
            if (_statusOrderDetail.value is Resource.Success) {
                val response = cancelSaleUseCase.executeCancelSale(_statusOrderDetail.value!!.data!!.orderId.toString())
                _statusCancelSale.value = response
            }
        }
    }
}