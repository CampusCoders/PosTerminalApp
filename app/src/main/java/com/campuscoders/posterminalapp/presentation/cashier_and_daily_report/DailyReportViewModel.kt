package com.campuscoders.posterminalapp.presentation.cashier_and_daily_report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.campuscoders.posterminalapp.domain.model.Orders
import com.campuscoders.posterminalapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DailyReportViewModel @Inject constructor(

): ViewModel() {

    private var _statusSumOfOrdersPrice = MutableLiveData<String>()
    val statusSumOfOrdersPrice: LiveData<String>
        get() = _statusSumOfOrdersPrice

    private var _statusSumOfOrdersTax = MutableLiveData<String>()
    val statusSumOfOrdersTax: LiveData<String>
        get() = _statusSumOfOrdersPrice

    private var _statusOrderList = MutableLiveData<Resource<List<Orders>>>()
    val statusOrderList: LiveData<Resource<List<Orders>>>
        get() = _statusOrderList

    // methods
}