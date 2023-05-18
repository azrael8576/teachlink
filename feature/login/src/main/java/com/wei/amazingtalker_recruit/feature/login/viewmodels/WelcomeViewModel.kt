package com.wei.amazingtalker_recruit.feature.login.viewmodels

import com.wei.amazingtalker_recruit.core.base.BaseViewModel
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.feature.login.WelcomeFragmentDirections
import com.wei.amazingtalker_recruit.feature.login.state.WelcomeViewAction
import com.wei.amazingtalker_recruit.feature.login.state.WelcomeViewEvent
import com.wei.amazingtalker_recruit.feature.login.state.WelcomeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : BaseViewModel<
        WelcomeViewAction,
        WelcomeViewEvent,
        WelcomeViewState
        >(WelcomeViewState()) {

    private fun navigateToLogin() {
        CoroutineScope(Dispatchers.Main).launch {
            //模擬初始化 Login 時間 (eg.檢查本地資料, 版本號, Server狀態, 登入狀態, Welcome動畫等...)
            delay(3000)
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment()
            postEvent(WelcomeViewEvent.NavToLogin(NavigateEvent.ByDirections(action)))
        }
    }

    override fun dispatch(action: WelcomeViewAction) {
        when (action) {
            is WelcomeViewAction.NavToLogin -> navigateToLogin()
        }
    }
}
