package com.wei.amazingtalker_recruit.feature.login.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wei.amazingtalker_recruit.core.extensions.SharedFlowEvents
import com.wei.amazingtalker_recruit.core.extensions.setEvent
import com.wei.amazingtalker_recruit.core.models.Event
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.feature.login.WelcomeFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : ViewModel() {
    val events = SharedFlowEvents<Event>()

    fun navigateToLogin() {
        CoroutineScope(Dispatchers.Main).launch {
            //模擬初始化 Login 時間 (eg.檢查本地資料, 版本號, Server狀態, 登入狀態, Welcome動畫等...)
            delay(3000)
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment()
            postEvent(NavigateEvent.ByDirections(action))
        }
    }

    private fun postEvent(event: Event) {
        viewModelScope.launch {
            events.setEvent(event)
        }
    }
}
