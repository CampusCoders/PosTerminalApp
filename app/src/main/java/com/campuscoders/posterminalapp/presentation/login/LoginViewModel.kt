package com.campuscoders.posterminalapp.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.domain.model.MainUser
import com.campuscoders.posterminalapp.domain.model.TerminalUsers
import com.campuscoders.posterminalapp.domain.use_case.login.FetchMainUserUseCase
import com.campuscoders.posterminalapp.domain.use_case.login.FetchTerminalUserUseCase
import com.campuscoders.posterminalapp.domain.use_case.login.SaveMainUserUseCase
import com.campuscoders.posterminalapp.domain.use_case.login.SaveTerminalUserUseCase
import com.campuscoders.posterminalapp.utils.Constants
import com.campuscoders.posterminalapp.utils.Constants.ADMIN
import com.campuscoders.posterminalapp.utils.Constants.IPTAL_IADE
import com.campuscoders.posterminalapp.utils.Constants.KASIYER_EKLEME_DUZENLEME
import com.campuscoders.posterminalapp.utils.Constants.KASIYER_GORUTULEME
import com.campuscoders.posterminalapp.utils.Constants.KASIYER_SILME
import com.campuscoders.posterminalapp.utils.Constants.POS_YONETIMI
import com.campuscoders.posterminalapp.utils.Constants.RAPOR_KAYDET_GONDER
import com.campuscoders.posterminalapp.utils.Constants.TAHSILAT
import com.campuscoders.posterminalapp.utils.Constants.TUM_RAPORLARI_GORUNTULEME
import com.campuscoders.posterminalapp.utils.Constants.URUN_EKLEME_DUZENLEME
import com.campuscoders.posterminalapp.utils.Constants.URUN_GORUNTULEME
import com.campuscoders.posterminalapp.utils.Constants.URUN_SILME
import com.campuscoders.posterminalapp.utils.Resource
import com.campuscoders.posterminalapp.utils.TimeAndDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val saveMainUserUseCase: SaveMainUserUseCase,
    private val fetchMainUserUseCase: FetchMainUserUseCase,
    private val saveTerminalUserUseCase: SaveTerminalUserUseCase,
    private val fetchTerminalUserUseCase: FetchTerminalUserUseCase
): ViewModel() {

    interface MainUserCallBack {
        fun onMainUserFetched(mainUserFromDb: MainUser?)
        fun onError(message: String)
    }

    private var _statusInsertMainUser = MutableLiveData<Resource<Boolean>>()
    val statusInsertMainUser: LiveData<Resource<Boolean>>
        get() = _statusInsertMainUser

    private var _statusInsertTerminalUser = MutableLiveData<Resource<Boolean>>()
    val statusInsertTerminalUser: LiveData<Resource<Boolean>>
        get() = _statusInsertTerminalUser

    private var _statusControlMainUser = MutableLiveData<Resource<Boolean>>()
    val statusControlMainUser: LiveData<Resource<Boolean>>
        get() = _statusControlMainUser

    fun saveMainUser(mainUser: MainUser) {
        _statusInsertMainUser.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = saveMainUserUseCase.executeSaveMainUser(mainUser)
            when(response) {
                is Resource.Success -> {
                    // save terminal
                    saveTerminalUser(
                        TerminalUsers(
                            mainUser.mainUserTerminalId,
                            mainUser.mainUserVknTckn,
                            mainUser.mainUserUyeIsyeriNo,
                            "${mainUser.mainUserFirstName} ${mainUser.mainUserLastName}",
                            mainUser.mainUserPassword,
                            TimeAndDate.getLocalDate(Constants.DATE_FORMAT),
                            TimeAndDate.getTime(),
                            IPTAL_IADE,
                            TAHSILAT,
                            KASIYER_GORUTULEME,
                            KASIYER_EKLEME_DUZENLEME,
                            KASIYER_SILME,
                            URUN_GORUNTULEME,
                            URUN_EKLEME_DUZENLEME,
                            URUN_SILME,
                            TUM_RAPORLARI_GORUNTULEME,
                            RAPOR_KAYDET_GONDER,
                            POS_YONETIMI,
                            ADMIN)
                    )
                }
                is Resource.Loading -> {_statusInsertMainUser.value = Resource.Loading(null)}
                is Resource.Error -> {_statusInsertMainUser.value = Resource.Error(false,response.message?:"Error Insert MainUser")}
            }
            _statusInsertMainUser.value = response
        }
    }

    private fun saveTerminalUser(terminalUsers: TerminalUsers) {
        _statusInsertTerminalUser.value = Resource.Loading(null)
        viewModelScope.launch {
            val response = saveTerminalUserUseCase.executeSaveTerminalUser(terminalUsers)
            _statusInsertTerminalUser.value = response
        }
    }

    private fun fetchMainUser(terminalId: String, callBack: MainUserCallBack) {
        viewModelScope.launch {
            val response = fetchMainUserUseCase.executeFetchMainUser(terminalId)
            when(response) {
                is Resource.Success -> {
                    callBack.onMainUserFetched(response.data)
                }
                is Resource.Loading -> {
                    _statusControlMainUser.value = Resource.Loading(null)
                }
                is Resource.Error -> {
                    _statusControlMainUser.value = Resource.Error(null,response.message?:"Error! (fetchMainUserUseCase)")
                    callBack.onError(response.message?:"Error! (fetchMainUserUseCase)")
                }
            }
        }
    }

    fun controlMainUser(mainUser: MainUser) {
        _statusControlMainUser.value = Resource.Loading(null)
        fetchMainUser(mainUser.mainUserTerminalId!!, object : MainUserCallBack {
            override fun onMainUserFetched(mainUserFromDb: MainUser?) {
                if (mainUserFromDb != null) {
                    if (mainUserFromDb.mainUserTerminalId != mainUser.mainUserTerminalId ||
                        mainUserFromDb.mainUserVknTckn != mainUser.mainUserVknTckn ||
                        mainUserFromDb.mainUserUyeIsyeriNo != mainUser.mainUserUyeIsyeriNo ||
                        mainUserFromDb.mainUserPassword != mainUser.mainUserPassword) {
                        _statusControlMainUser.value = Resource.Error(false, "NOT MATCHED!")
                    } else {
                        _statusControlMainUser.value = Resource.Success(true)
                    }
                } else {
                    _statusControlMainUser.value = Resource.Error(false, "MainUserFromDb is null")
                }
            }

            override fun onError(message: String) {
                _statusControlMainUser.value = Resource.Error(false, message)
            }
        })
    }
}