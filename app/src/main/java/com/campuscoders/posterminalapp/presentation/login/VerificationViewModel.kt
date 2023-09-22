package com.campuscoders.posterminalapp.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.campuscoders.posterminalapp.utils.Constants.MINUTE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(

): ViewModel() {

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
        var timerMinute = MINUTE
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

}