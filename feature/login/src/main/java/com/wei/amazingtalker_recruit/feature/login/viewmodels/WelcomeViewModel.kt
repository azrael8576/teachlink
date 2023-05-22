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
import timber.log.Timber
import java.time.OffsetDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor() : BaseViewModel<
        WelcomeViewAction,
        WelcomeViewEvent,
        WelcomeViewState
        >(WelcomeViewState()) {

    /**
     * 在 ViewModel 的 init{} 區塊，Fragment 可能還未進行 UI 事件監聽註冊。因此，此區段內的 postEvent() 方法，
     * 也即 SharedFlow 的 emit() 操作，可能導致事件的丟失。在這種情況下，Google 官方建議的做法是不區分狀態與事件，
     * 而是統一使用 StateFlow，並添加標示位來表示事件是否已經被消費。
     */
    init {
        // TODO: 修復事件丟失問題。由於此區塊在 Fragment 完全創建之前就執行，postEvent() 可能會導致事件丟失。
//        navigateToLogin()
    }

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
