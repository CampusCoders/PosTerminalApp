package com.campuscoders.posterminalapp.domain.use_case.login

import com.campuscoders.posterminalapp.domain.model.MainUser
import com.campuscoders.posterminalapp.domain.repository.locale.LoginRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchMainUserUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend fun executeFetchMainUser(terminalId: String): Resource<MainUser> {
        return try {
            val response = repository.fetchMainUserFromDatabase(terminalId)
            response?.let {
                return@let Resource.Success(it)
            } ?: Resource.Error(null, "Error MainUser")
        } catch (e: Exception) {
            Resource.Error(null, e.localizedMessage?:"Error Exception")
        }
    }
}