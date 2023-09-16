package com.campuscoders.posterminalapp.domain.use_case.cashier_and_report

import com.campuscoders.posterminalapp.domain.repository.locale.CashierAndReportRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchLastTerminalUserIdUseCase @Inject constructor(private val repository: CashierAndReportRepository) {
    suspend fun executeFetchLastTerminalUserId(): Resource<Int> {
        return try {
            val response = repository.fetchLastTerminalUserId()
            response?.let {
                return@let Resource.Success(it)
            } ?: Resource.Error(null,"Failed to fetch last terminalUser Id")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error - executeFetchLastTerminalUserId")
        }
    }
}