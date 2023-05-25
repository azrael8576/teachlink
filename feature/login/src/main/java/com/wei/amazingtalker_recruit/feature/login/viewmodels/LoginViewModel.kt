package com.wei.amazingtalker_recruit.feature.login.viewmodels

import com.wei.amazingtalker_recruit.core.authentication.TokenManager
import com.wei.amazingtalker_recruit.core.base.BaseViewModel
import com.wei.amazingtalker_recruit.feature.login.state.LoginViewAction
import com.wei.amazingtalker_recruit.feature.login.state.LoginViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel<
        LoginViewAction,
        LoginViewState
        >(LoginViewState()) {

    // TODO 替換至 login API
    private fun login() {
        TokenManager.validateToken()
        updateState {
            copy(
                isUserLoggedIn = TokenManager.isTokenValid,
            )
        }
    }

    override fun dispatch(action: LoginViewAction) {
        when (action) {
            is LoginViewAction.Login -> login()
        }
    }

}
