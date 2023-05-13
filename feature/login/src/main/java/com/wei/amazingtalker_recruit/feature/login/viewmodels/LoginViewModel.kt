package com.wei.amazingtalker_recruit.feature.login.viewmodels

import com.wei.amazingtalker_recruit.core.authentication.TokenManager
import com.wei.amazingtalker_recruit.core.base.BaseViewModel
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.core.navigation.DeepLinks
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel() {
    fun login() {
        // TODO 替換至 login API
        TokenManager.validateToken()
        postEvent(NavigateEvent.ByDeepLink(DeepLinks.HOME))
    }

}
