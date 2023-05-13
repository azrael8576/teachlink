package com.wei.amazingtalker_recruit.feature.login.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wei.amazingtalker_recruit.core.authentication.TokenManager
import com.wei.amazingtalker_recruit.core.extensions.SharedFlowEvents
import com.wei.amazingtalker_recruit.core.extensions.setEvent
import com.wei.amazingtalker_recruit.core.models.Event
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.core.navigation.DeepLinks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    val events = SharedFlowEvents<Event>()

    fun login() {
        // TODO 替換至 login API
        TokenManager.validateToken()
        postEvent(NavigateEvent.ByDeepLink(DeepLinks.HOME))
    }

    private fun postEvent(event: Event) {
        viewModelScope.launch {
            events.setEvent(event)
        }
    }
}
