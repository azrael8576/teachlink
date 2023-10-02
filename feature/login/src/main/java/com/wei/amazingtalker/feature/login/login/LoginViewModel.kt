package com.wei.amazingtalker.feature.login.login

import androidx.lifecycle.viewModelScope
import com.wei.amazingtalker.core.base.BaseViewModel
import com.wei.amazingtalker.core.data.repository.UserDataRepository
import com.wei.amazingtalker.feature.login.utilities.TEST_ACCOUNT
import com.wei.amazingtalker.feature.login.utilities.TEST_PASSWORD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : BaseViewModel<
        LoginViewAction,
        LoginViewState
        >(LoginViewState()) {

    private fun login(account: String, password: String) {
        // TODO 替換至 login API
        viewModelScope.launch {
            if (TEST_ACCOUNT == account && TEST_PASSWORD == password) {
                userDataRepository.setTokenString(OffsetDateTime.now().toString())

                updateState {
                    copy(
                        isLoginClicked = true,
                    )
                }
            }
        }
    }

    override fun dispatch(action: LoginViewAction) {
        Timber.e("LoginViewModel dispatch() -> $action")
        when (action) {
            is LoginViewAction.Login -> login(action.account, action.password)
        }
    }

}
