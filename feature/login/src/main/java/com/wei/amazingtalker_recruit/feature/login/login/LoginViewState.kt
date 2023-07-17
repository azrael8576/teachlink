package com.wei.amazingtalker_recruit.feature.login.login

import com.wei.amazingtalker_recruit.core.authentication.TokenManager
import com.wei.amazingtalker_recruit.core.base.Action
import com.wei.amazingtalker_recruit.core.base.State

sealed class LoginViewAction : Action {
    data class Login(val account: String = "", val password: String = "") : LoginViewAction()
}

data class LoginViewState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isUserLoggedIn: Boolean = TokenManager.isTokenValid
) : State
