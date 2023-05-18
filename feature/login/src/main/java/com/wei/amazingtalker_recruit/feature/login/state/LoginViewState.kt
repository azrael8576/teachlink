package com.wei.amazingtalker_recruit.feature.login.state

import com.wei.amazingtalker_recruit.core.models.Action
import com.wei.amazingtalker_recruit.core.models.Event
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.core.models.State

sealed class LoginViewAction : Action {
    object Login : LoginViewAction()
}

class LoginViewState : State

sealed class LoginViewEvent : Event {
    data class NavToHome(val navigateEvent: NavigateEvent.ByDeepLink) : LoginViewEvent()
}
