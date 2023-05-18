package com.wei.amazingtalker_recruit.feature.login.state

import com.wei.amazingtalker_recruit.core.models.Action
import com.wei.amazingtalker_recruit.core.models.Event
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.core.models.State

sealed class WelcomeViewAction : Action {
    object NavToLogin : WelcomeViewAction()
}

class WelcomeViewState : State

sealed class WelcomeViewEvent : Event {
    data class NavToLogin(val navigateEvent: NavigateEvent.ByDirections) : WelcomeViewEvent()
}
