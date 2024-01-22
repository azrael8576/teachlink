package com.wei.amazingtalker.feature.login.welcome

import com.wei.amazingtalker.core.base.Action
import com.wei.amazingtalker.core.base.State

sealed class WelcomeViewAction : Action {
    data object GetStarted : WelcomeViewAction()
}

data class WelcomeViewState(
    val isGetStartedClicked: Boolean = false,
) : State
