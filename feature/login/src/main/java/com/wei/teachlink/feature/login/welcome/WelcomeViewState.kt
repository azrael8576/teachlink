package com.wei.teachlink.feature.login.welcome

import com.wei.teachlink.core.base.Action
import com.wei.teachlink.core.base.State

sealed class WelcomeViewAction : Action {
    data object GetStarted : WelcomeViewAction()
}

data class WelcomeViewState(
    val isGetStartedClicked: Boolean = false,
) : State
