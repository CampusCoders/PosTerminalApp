package com.campuscoders.posterminalapp.domain.use_case.login

import com.campuscoders.posterminalapp.domain.model.MainUser
import com.campuscoders.posterminalapp.domain.repository.locale.LoginRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class SaveMainUserUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend fun executeSaveMainUser(mainUser: MainUser): Resource<Boolean> {
        return try {
            val response = repository.saveMainUserToDatabase(mainUser)
            if (response > 0) {
                // success
                Resource.Success(true)
            } else {
                Resource.Error(false,"Insert Error")
            }
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Insert exception error")
        }
    }
}