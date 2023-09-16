package com.campuscoders.posterminalapp.domain.use_case.cashier_and_report

import com.campuscoders.posterminalapp.domain.model.TerminalUsers
import com.campuscoders.posterminalapp.domain.repository.locale.CashierAndReportRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchTerminalUserByIdUseCase @Inject constructor(private val repository: CashierAndReportRepository){
    suspend fun executeFetchTerminalUserById(terminalId: String): Resource<TerminalUsers> {
        return try {
            val response = repository.fetchTerminalUserById(terminalId)
            response?.let {
                return@let Resource.Success(it)
            } ?: Resource.Error(null,"Failed to fetch terminalUser")
        } catch (e: Exception) {
            Resource.Error(null, e.localizedMessage?:"Error!")
        }
    }
}