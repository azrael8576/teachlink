package com.wei.amazingtalker_recruit.feature.login.welcome

import androidx.lifecycle.viewModelScope
import com.wei.amazingtalker_recruit.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : BaseViewModel<
        WelcomeViewAction,
        WelcomeViewState
        >(WelcomeViewState()) {

    init {
        navigateToLogin()
    }

    private fun navigateToLogin() {
        viewModelScope.launch {
            delay(3000)
            updateState {
                copy(
                    isInitialized = true,
                )
            }
        }
    }

    override fun dispatch(action: WelcomeViewAction) {
    }
}
