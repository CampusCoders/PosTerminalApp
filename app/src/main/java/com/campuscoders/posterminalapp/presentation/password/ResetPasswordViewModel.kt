package com.campuscoders.posterminalapp.presentation.password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.domain.use_case.password.UpdateMainUserPasswordUseCase
import com.campuscoders.posterminalapp.domain.use_case.password.UpdateTerminalUserPasswordUseCase
import com.campuscoders.posterminalapp.utils.Constants
import com.campuscoders.posterminalapp.utils.Constants.VKN_TCKN
import com.campuscoders.posterminalapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val updateMainUserPasswordUseCase: UpdateMainUserPasswordUseCase,
    private val updateTerminalUserPasswordUseCase: UpdateTerminalUserPasswordUseCase
) : ViewModel(){

    private var _statusIsUpdated = MutableLiveData<Resource<Boolean>>()
    val statusIsUpdated: LiveData<Resource<Boolean>>
        get() = _statusIsUpdated

    private var _timer = MutableLiveData<String>()
    val timer: LiveData<String>
        get() = _timer

    private var _onFinish = MutableLiveData<Boolean>()
    val onFinish: LiveData<Boolean>
        get() = _onFinish

    init {
        startTimer()
    }

    fun startTimer() {
        _onFinish.value = false
        var timerMinute = Constants.MINUTE
        var timerSecond = 0

        viewModelScope.launch {
            var control = true
            while (control) {
                delay(1000)
                if (timerSecond == 0) {
                    timerMinute--
                    timerSecond = 59
                } else timerSecond--

                val formattedMinute = if (timerMinute < 10) "0$timerMinute" else "$timerMinute"
                val formattedSecond = if (timerSecond < 10) "0$timerSecond" else "$timerSecond"
                _timer.value = "$formattedMinute:$formattedSecond"

                if (timerMinute == 0 && timerSecond == 0) {
                    control = false
                    _onFinish.value = true
                }
            }
        }
    }

    fun updateMainUserPassword(newPassword: String) {
        _statusIsUpdated.value = Resource.Loading(null)
        viewModelScope.launch {
            // VKN_TCKN -> SharedPreference'den çekilecek
            val responseFromMainUserPasswordUseCase = updateMainUserPasswordUseCase.executeUpdateMainUserPassword(VKN_TCKN,newPassword)
            when(responseFromMainUserPasswordUseCase) {
                is Resource.Success -> {
                    val responseFromTerminalUserPasswordUseCase = updateTerminalUserPasswordUseCase.executeUpdateTerminalUserPassword(VKN_TCKN,newPassword)
                    when(responseFromTerminalUserPasswordUseCase) {
                        is Resource.Success -> {
                            _statusIsUpdated.value = Resource.Success(true)
                        }
                        is Resource.Loading -> {
                            _statusIsUpdated.value = Resource.Loading(null)
                        }
                        is Resource.Error -> {
                            _statusIsUpdated.value = Resource.Error(false,responseFromMainUserPasswordUseCase.message?:"Update failedd")
                        }
                    }
                    //_statusIsUpdated.value = Resource.Success(true)
                }
                is Resource.Loading -> {
                    _statusIsUpdated.value = Resource.Loading(null)
                }
                is Resource.Error -> {
                    _statusIsUpdated.value = Resource.Error(false,responseFromMainUserPasswordUseCase.message?:"Update failedd")
                }
            }
        }
    }
}