package com.campuscoders.posterminalapp.domain.use_case.cashier_and_report

import com.campuscoders.posterminalapp.domain.model.TerminalUsers
import com.campuscoders.posterminalapp.domain.repository.locale.CashierAndReportRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchAllCashiersUseCase @Inject constructor(private val repository: CashierAndReportRepository) {
    suspend fun executeFetchAllCashiers(): Resource<List<TerminalUsers>> {
        return try {
            val response = repository.fetchAllCashiers()
            response?.let {
                return@let Resource.Success(it)
            } ?: Resource.Error(null,"No Cashier data")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error - (executeFetchAllCashiers)")
        }
    }
}