package com.campuscoders.posterminalapp.domain.use_case.cashier_and_report

import com.campuscoders.posterminalapp.domain.repository.locale.CashierAndReportRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class DeleteCashierUseCase @Inject constructor(private val repository: CashierAndReportRepository) {
    suspend fun executeDeleteCashier(terminalId: Int): Resource<Boolean> {
        return try {
            val response = repository.deleteCashier(terminalId)
            if (response > 0) {
                Resource.Success(true)
            } else {
                Resource.Error(false,"Failed to delete Cashier!")
            }
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error - (executeDeleteCashier)")
        }
    }
}