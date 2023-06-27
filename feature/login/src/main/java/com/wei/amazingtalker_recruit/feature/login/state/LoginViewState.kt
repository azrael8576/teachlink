package com.wei.amazingtalker_recruit.feature.login.state

import com.wei.amazingtalker_recruit.core.authentication.TokenManager
import com.wei.amazingtalker_recruit.core.base.Action
import com.wei.amazingtalker_recruit.core.base.State

sealed class LoginViewAction : Action {
    object Login : LoginViewAction()
    data class SetAccount(val account: String) : LoginViewAction()
    data class SetPassword(val password: String) : LoginViewAction()
}

data class LoginViewState(
    val account: String = "account",
    val password: String = "password",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isUserLoggedIn: Boolean = TokenManager.isTokenValid
) : State
