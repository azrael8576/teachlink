package com.wei.amazingtalker.feature.login.welcome

import androidx.lifecycle.viewModelScope
import com.wei.amazingtalker.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : BaseViewModel<
    WelcomeViewAction,
    WelcomeViewState,
    >(WelcomeViewState()) {

    private fun navigateToLogin() {
        viewModelScope.launch {
            updateState {
                copy(
                    isGetStartedClicked = true,
                )
            }
        }
    }

    override fun dispatch(action: WelcomeViewAction) {
        when (action) {
            is WelcomeViewAction.GetStarted -> navigateToLogin()
        }
    }
}
