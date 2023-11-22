package com.campuscoders.posterminalapp.presentation.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.R
import com.campuscoders.posterminalapp.domain.model.TerminalUsers
import com.campuscoders.posterminalapp.domain.use_case.login.FetchMainUserPasswordUseCase
import com.campuscoders.posterminalapp.domain.use_case.login.FetchTerminalUserByMemberStoreIdUseCase
import com.campuscoders.posterminalapp.domain.use_case.login.FetchTerminalUserPasswordUseCase
import com.campuscoders.posterminalapp.domain.use_case.login.FetchTerminalUserUseCase
import com.campuscoders.posterminalapp.utils.CustomSharedPreferences
import com.campuscoders.posterminalapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginTwoViewModel @Inject constructor(
    private val fetchMainUserPasswordUseCase: FetchMainUserPasswordUseCase,
    private val fetchTerminalUserPasswordUseCase: FetchTerminalUserPasswordUseCase,
    private val fetchTerminalUserUseCase: FetchTerminalUserUseCase,
    private val fetchTerminalUserByMemberStoreIdUseCase: FetchTerminalUserByMemberStoreIdUseCase
): ViewModel() {

    interface PasswordCallback {
        fun onPasswordFetched(passwordFromCallBack: String?)
        fun onError(message: String)
    }

    private var _statusControlPassword = MutableLiveData<Resource<Boolean>>()
    val statusControlPassword: LiveData<Resource<Boolean>>
        get() = _statusControlPassword

    private fun fetchMainUserPassword(memberStoreId: String, callback: PasswordCallback) {
        viewModelScope.launch {
            val response = fetchMainUserPasswordUseCase.executeFetchMainUserPassword(memberStoreId)
            when(response) {
                is Resource.Success -> {
                    callback.onPasswordFetched(response.data)
                }
                is Resource.Loading -> {
                    _statusControlPassword.value = Resource.Loading(null)
                }
                is Resource.Error -> {
                    _statusControlPassword.value = Resource.Error(null,response.message?:"Error! (fetchMainUserPasswordUseCase)")
                    callback.onError(response.message?:"Error! (fetchMainUserPasswordUseCase)")
                }
            }
        }
    }

    private fun fetchTerminalUserPassword(terminalId: String, callback: PasswordCallback) {
        viewModelScope.launch {
            val response = fetchTerminalUserPasswordUseCase.executeFetchTerminalUserPassword(terminalId)
            when(response) {
                is Resource.Success -> {
                    callback.onPasswordFetched(response.data)
                }
                is Resource.Loading -> {
                    _statusControlPassword.value = Resource.Loading(null)
                }
                is Resource.Error -> {
                    _statusControlPassword.value = Resource.Error(null,response.message?:"Error! (fetchTerminalUserPasswordUseCase)")
                    callback.onError(response.message?:"Error! (fetchTerminalUserPasswordUseCase)")
                }
            }
        }
    }

    private fun fetchTerminalUser(id: String, isAdmin: Boolean, context: Context, password: String) {
        viewModelScope.launch {
            if (isAdmin) {
                val response = fetchTerminalUserByMemberStoreIdUseCase.executeFetchTerminalUserByMemberStore(id)
                when(response) {
                    is Resource.Success -> {
                        comparePasswords(response,password,isAdmin,context)
                    }
                    is Resource.Loading -> {
                        _statusControlPassword.value = Resource.Loading(null)
                    }
                    is Resource.Error -> {
                        _statusControlPassword.value = Resource.Error(null,response.message?:"Error (fetchTerminalUserByMemberStoreIdUseCase)")
                    }
                }
            } else {
                val response = fetchTerminalUserUseCase.executeFetchTerminalUser(id)
                when(response) {
                    is Resource.Success -> {
                        comparePasswords(response,password,isAdmin,context)
                    }
                    is Resource.Loading -> {
                        _statusControlPassword.value = Resource.Loading(null)
                    }
                    is Resource.Error -> {
                        _statusControlPassword.value = Resource.Error(null,response.message?:"Error (fetchTerminalUserUseCase)")
                    }
                }
            }
        }
    }

    private fun comparePasswords(response: Resource<TerminalUsers>, password: String, isAdmin: Boolean, context: Context) {
        if (response.data != null) {
            if (response.data.terminalUserPassword == password) {
                _statusControlPassword.value = Resource.Success(true)
                saveToSharedPreferences(response.data,isAdmin, context)
            } else {
                _statusControlPassword.value = Resource.Error(false, context.getString(R.string.not_matched))
            }
        } else {
            _statusControlPassword.value = Resource.Error(false,context.getString(R.string.pass_db_null))
        }
    }

    private fun saveToSharedPreferences(terminalUser: TerminalUsers, isAdmin: Boolean, context: Context) {
        val customSharedPreferences = CustomSharedPreferences(context)
        customSharedPreferences.setTerminalUserLogin(terminalUser.terminalUserTerminalId!!,terminalUser.terminalUserVknTckn!!,
            terminalUser.terminalUserUyeIsyeriNo!!,terminalUser.terminalUserPassword!!,terminalUser.terminalUserFullName!!,terminalUser.terminalUserDate!!,
            terminalUser.terminalUserTime!!,terminalUser.terminalUserIptalIade!!,terminalUser.terminalUserTahsilat!!,
            terminalUser.terminalUserKasiyerGoruntuleme!!,terminalUser.terminalUserKasiyerEklemeDuzenleme!!,terminalUser.terminalUserKasiyerSilme!!,
            terminalUser.terminalUserUrunGoruntuleme!!,terminalUser.terminalUserUrunEklemeDuzenleme!!,terminalUser.terminalUserUrunSilme!!,
            terminalUser.terminalUserTumRaporlariGoruntule!!,terminalUser.terminalUserRaporKaydetGonder!!,terminalUser.terminalUserPosYonetimi!!,
            terminalUser.terminalUserAdmin!!, context)

        if (isAdmin) {
            customSharedPreferences.setMainUserLoginRememberMeManager(true, context)
        } else {
            customSharedPreferences.setMainUserLoginRememberMeCashier(true, context)
        }
    }

    fun controlPassword(id: String, password: String, isAdmin: Boolean, rememberMe: Boolean, context: Context) {
        _statusControlPassword.value = Resource.Loading(null)
        if (rememberMe) {
            fetchTerminalUser(id, isAdmin, context, password)
        } else {
            if (isAdmin) {
                controlMainUserPassword(id, password, context)
            } else {
                controlTerminalUserPassword(id, password, context)
            }
        }
    }

    private fun controlMainUserPassword(memberStoreId: String, password: String, context: Context) {
        fetchMainUserPassword(memberStoreId, object : PasswordCallback {
            override fun onPasswordFetched(passwordFromCallBack: String?) {
                if (passwordFromCallBack != null) {
                    if (passwordFromCallBack == password) {
                        _statusControlPassword.value = Resource.Success(true)
                        val customSharedPreferences = CustomSharedPreferences(context)
                        customSharedPreferences.setMainUserLoginRememberMeManager(false, context)
                    } else {
                        _statusControlPassword.value = Resource.Error(false, context.getString(R.string.not_matched))
                    }
                } else {
                    _statusControlPassword.value = Resource.Error(false,context.getString(R.string.pass_db_null))
                }
            }
            override fun onError(message: String) {
                _statusControlPassword.value = Resource.Error(false,message)
            }
        })
    }

    private fun controlTerminalUserPassword(terminalId: String, password: String, context: Context) {
        fetchTerminalUserPassword(terminalId, object : PasswordCallback {
            override fun onPasswordFetched(passwordFromCallBack: String?) {
                if (passwordFromCallBack != null) {
                    if (passwordFromCallBack == password) {
                        _statusControlPassword.value = Resource.Success(true)
                        val customSharedPreferences = CustomSharedPreferences(context)
                        customSharedPreferences.setMainUserLoginRememberMeCashier(false, context)
                    } else {
                        _statusControlPassword.value = Resource.Error(false, context.getString(R.string.not_matched))
                    }
                } else {
                    _statusControlPassword.value = Resource.Error(false,context.getString(R.string.pass_db_null))
                }
            }
            override fun onError(message: String) {
                _statusControlPassword.value = Resource.Error(false,message)
            }
        })
    }
}