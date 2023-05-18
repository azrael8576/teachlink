package com.wei.amazingtalker_recruit.feature.login.viewmodels

import com.wei.amazingtalker_recruit.core.authentication.TokenManager
import com.wei.amazingtalker_recruit.core.base.BaseViewModel
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.core.navigation.DeepLinks
import com.wei.amazingtalker_recruit.feature.login.state.LoginViewAction
import com.wei.amazingtalker_recruit.feature.login.state.LoginViewEvent
import com.wei.amazingtalker_recruit.feature.login.state.LoginViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel<
        LoginViewAction,
        LoginViewEvent,
        LoginViewState
        >(LoginViewState()) {

    private fun login() {
        // TODO 替換至 login API
        TokenManager.validateToken()
        postEvent(LoginViewEvent.NavToHome((NavigateEvent.ByDeepLink(DeepLinks.HOME))))
    }

    override fun dispatch(action: LoginViewAction) {
        when (action) {
            is LoginViewAction.Login -> login()
        }
    }

}
