package com.wei.amazingtalker_recruit.feature.login.login

import com.wei.amazingtalker_recruit.core.authentication.TokenManager
import com.wei.amazingtalker_recruit.core.base.BaseViewModel
import com.wei.amazingtalker_recruit.feature.login.utilities.TEST_ACCOUNT
import com.wei.amazingtalker_recruit.feature.login.utilities.TEST_PASSWORD
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel<
        LoginViewAction,
        LoginViewState
        >(LoginViewState()) {

    private fun login(account: String, password: String) {
        // TODO 替換至 login API
        if (TEST_ACCOUNT == account && TEST_PASSWORD == password) {
            TokenManager.validateToken()
            updateState {
                copy(
                    isUserLoggedIn = TokenManager.isTokenValid,
                )
            }
        }
    }

    override fun dispatch(action: LoginViewAction) {
        Timber.e("LoginViewModel dispatch() -> $action")
        when (action) {
            is LoginViewAction.Login -> login(action.account, action.password)
        }
    }

}
