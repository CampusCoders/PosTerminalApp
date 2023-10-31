package com.campuscoders.posterminalapp.domain.use_case.cashier_and_report

import com.campuscoders.posterminalapp.domain.model.TerminalUsers
import com.campuscoders.posterminalapp.domain.repository.locale.CashierAndReportRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class UpdateCashierUseCase @Inject constructor(private val repository: CashierAndReportRepository) {
    suspend fun executeUpdateCashier(terminalUser: TerminalUsers): Resource<Boolean> {
        return try {
            val response = repository.updateTerminalUser(terminalUser)
            if (response > 0) Resource.Success(true)
            else Resource.Error(false,"Failed to update Cashier!")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error - executeFetchLastTerminalUserId")
        }
    }
}