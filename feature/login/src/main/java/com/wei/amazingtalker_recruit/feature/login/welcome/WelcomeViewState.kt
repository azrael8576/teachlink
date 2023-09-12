package com.wei.amazingtalker_recruit.feature.login.welcome

import com.wei.amazingtalker_recruit.core.base.Action
import com.wei.amazingtalker_recruit.core.base.State

sealed class WelcomeViewAction : Action{
    object GetStarted: WelcomeViewAction()
}

data class WelcomeViewState(
    val isGetStartedClicked: Boolean = false
) : State