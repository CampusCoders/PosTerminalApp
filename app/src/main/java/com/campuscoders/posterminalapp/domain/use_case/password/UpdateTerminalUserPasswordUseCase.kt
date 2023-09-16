package com.campuscoders.posterminalapp.domain.use_case.password

import com.campuscoders.posterminalapp.domain.repository.locale.LoginRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class UpdateTerminalUserPasswordUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend fun executeUpdateTerminalUserPassword(vknTckn: String, newPassword: String): Resource<Int> {
        return try {
            val response = repository.updateTerminalUserPassword(vknTckn, newPassword)
            if (response > 0) {
                Resource.Success(response)
            } else {
                Resource.Error(null,"TerminalUser password update failed")
            }
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error! (executeUpdateTerminalUserPassword)")
        }
    }
}