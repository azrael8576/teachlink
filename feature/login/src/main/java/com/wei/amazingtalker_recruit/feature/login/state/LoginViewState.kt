package com.wei.amazingtalker_recruit.feature.login.state

import com.wei.amazingtalker_recruit.core.base.Action
import com.wei.amazingtalker_recruit.core.base.State

sealed class LoginViewAction : Action {
    object Login : LoginViewAction()
}

data class LoginViewState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isUserLoggedIn: Boolean = false
) : State
