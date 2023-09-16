package com.campuscoders.posterminalapp.domain.use_case.login

import com.campuscoders.posterminalapp.domain.repository.locale.LoginRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchTerminalUserPasswordUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend fun executeFetchTerminalUserPassword(terminalId: String): Resource<String> {
        return try {
            val response = repository.fetchTerminalUserPassword(terminalId)
            response?.let {
                return@let Resource.Success(it)
            } ?: Resource.Error(null,"Kullanıcı bilgisi bulunamadı!")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error catched!")
        }
    }
}