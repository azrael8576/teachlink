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

    private fun login() {
        // TODO 替換至 login API
        if ("account" == states.value.account && "password" == states.value.password) {
            TokenManager.validateToken()
            updateState {
                copy(
                    isUserLoggedIn = TokenManager.isTokenValid,
                )
            }
        }
    }

    private fun setAccount(account: String) {
        updateState {
            copy(
                account = account,
            )
        }
    }

    private fun setPassword(password: String) {
        updateState {
            copy(
                password = password,
            )
        }
    }

    override fun dispatch(action: LoginViewAction) {
        when (action) {
            is LoginViewAction.Login -> login()
            is LoginViewAction.SetAccount -> setAccount(account = action.account)
            is LoginViewAction.SetPassword -> setPassword(password = action.password)
        }
    }

}
