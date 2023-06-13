package com.wei.amazingtalker_recruit.feature.login.state

import com.wei.amazingtalker_recruit.core.base.Action
import com.wei.amazingtalker_recruit.core.base.State

sealed class WelcomeViewAction : Action

data class WelcomeViewState(
    val isInitialized: Boolean = false,
) : State