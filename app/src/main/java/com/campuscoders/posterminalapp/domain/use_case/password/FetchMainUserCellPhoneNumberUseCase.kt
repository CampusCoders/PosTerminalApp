package com.campuscoders.posterminalapp.domain.use_case.password

import com.campuscoders.posterminalapp.domain.repository.locale.LoginRepository
import com.campuscoders.posterminalapp.utils.Resource
import javax.inject.Inject

class FetchMainUserCellPhoneNumberUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend fun executeFetchMainUserCellPhoneNumber(vknTckn: String): Resource<String> {
        return try {
            val response = repository.fetchMainUserCellPhoneNumber(vknTckn)
            response?.let {
                return@let Resource.Success(it)
            } ?: Resource.Error(null,"TCKN/VKN Hatalı!")
        } catch (e: Exception) {
            Resource.Error(null,e.localizedMessage?:"Error catched! (executeFetchMainUserCellPhoneNumber)")
        }
    }
}