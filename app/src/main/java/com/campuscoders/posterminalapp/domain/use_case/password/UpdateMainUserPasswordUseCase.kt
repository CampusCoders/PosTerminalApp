package com.campuscoders.posterminalapp.domain.use_case.password

import com.campuscoders.posterminalapp.domain.repository.locale.LoginRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class UpdateMainUserPasswordUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend fun executeUpdateMainUserPassword(vknTckn: String, newPassword: String): Resource<Int> {
        return try {
            val response = repository.updateMainUserPassword(vknTckn,newPassword)
            if (response > 0) {
                Resource.Success(response)
            } else {
                Resource.Error(null,"Update failed")
            }
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error! (executeUpdateMainUserPassword)")
        }
    }
}