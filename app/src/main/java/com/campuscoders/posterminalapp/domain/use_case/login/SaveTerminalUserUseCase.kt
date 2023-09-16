package com.campuscoders.posterminalapp.domain.use_case.login

import com.campuscoders.posterminalapp.domain.model.TerminalUsers
import com.campuscoders.posterminalapp.domain.repository.locale.LoginRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class SaveTerminalUserUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend fun executeSaveTerminalUser(terminalUsers: TerminalUsers): Resource<Boolean> {
        return try {
            val response = repository.saveTerminalUserToDatabase(terminalUsers)
            if (response > 0) {
                Resource.Success(true)
            } else {
                Resource.Error(false, "Insert Error")
            }
        } catch (e: Exception) {
            Resource.Error(null, e.localizedMessage?:"UseCase Error!")
        }
    }
}