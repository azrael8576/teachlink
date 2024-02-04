package com.wei.teachlink.feature.login.login

import com.wei.teachlink.core.base.Action
import com.wei.teachlink.core.base.State

sealed class LoginViewAction : Action {
    data class Login(val account: String = "", val password: String = "") : LoginViewAction()
}

data class LoginViewState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginClicked: Boolean = false,
) : State
