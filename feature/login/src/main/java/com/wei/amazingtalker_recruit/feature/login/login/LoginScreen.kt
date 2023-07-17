package com.wei.amazingtalker_recruit.feature.login.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AtTheme
import com.wei.amazingtalker_recruit.feature.login.R
import com.wei.amazingtalker_recruit.feature.login.state.LoginViewAction
import com.wei.amazingtalker_recruit.feature.login.state.LoginViewState
import com.wei.amazingtalker_recruit.feature.login.utilities.TEST_ACCOUNT
import com.wei.amazingtalker_recruit.feature.login.utilities.TEST_PASSWORD
import com.wei.amazingtalker_recruit.feature.login.viewmodels.LoginViewModel

/**
 *
 * UI 事件決策樹
 * 下圖顯示了一個決策樹，用於查找處理特定事件用例的最佳方法。
 *
 *                                                      ┌───────┐
 *                                                      │ Start │
 *                                                      └───┬───┘
 *                                                          ↓
 *                                       ┌───────────────────────────────────┐
 *                                       │ Where is event originated?        │
 *                                       └──────┬─────────────────────┬──────┘
 *                                              ↓                     ↓
 *                                              UI                  ViewModel
 *                                              │                     │
 *                           ┌─────────────────────────┐      ┌───────────────┐
 *                           │ When the event requires │      │ Update the UI │
 *                           │ ...                     │      │ State         │
 *                           └─┬─────────────────────┬─┘      └───────────────┘
 *                             ↓                     ↓
 *                        Business logic      UI behavior logic
 *                             │                     │
 *     ┌─────────────────────────────────┐   ┌──────────────────────────────────────┐
 *     │ Delegate the business logic to  │   │ Modify the UI element state in the   │
 *     │ the ViewModel                   │   │ UI directly                          │
 *     └─────────────────────────────────┘   └──────────────────────────────────────┘
 *
 *
 */
@Composable
internal fun LoginRoute(
    onLoginNav: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiStates: LoginViewState by viewModel.states.collectAsStateWithLifecycle()

    LaunchedEffect(uiStates.isUserLoggedIn) {
        if (uiStates.isUserLoggedIn) {
            onLoginNav()
        }
    }

    LoginScreen(
        login = { account: String, password: String ->
            viewModel.dispatch(LoginViewAction.Login(account, password))
        }
    )
}

@Composable
internal fun LoginScreen(
    login: (String, String) -> Unit,
) {
    val accountState = rememberSaveable {
        // TODO : 測試帳號，Release 時需清空
        mutableStateOf(TEST_ACCOUNT)
    }

    val passwordState = rememberSaveable {
        // TODO : 測試密碼，Release 時需清空
        mutableStateOf(TEST_PASSWORD)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Title(Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(16.dp))
                AccountTextField(accountState)
                PasswordTextField(passwordState)
                Spacer(modifier = Modifier.height(32.dp))
                LoginButton(
                    accountState,
                    passwordState,
                    login
                )
            }
        }
    }

}

@Composable
private fun Title(modifier: Modifier = Modifier) {
    val text = stringResource(R.string.login_title)
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier.semantics { contentDescription = text }
    )
}


@Composable
internal fun AccountTextField(
    accountState: MutableState<String>,
) {
    val text = stringResource(R.string.account)
    TextField(
        value = accountState.value,
        modifier = Modifier
            .semantics { contentDescription = text },
        onValueChange = {
            accountState.value = it
        },
        label = {
            Text(text)
        },
        singleLine = true,
    )
}


@Composable
internal fun PasswordTextField(
    passwordState: MutableState<String>,
) {
    val text = stringResource(R.string.password)
    TextField(
        value = passwordState.value,
        modifier = Modifier
            .semantics { contentDescription = text },
        onValueChange = {
            passwordState.value = it
        },
        label = {
            Text(text)
        },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
    )
}

@Composable
private fun LoginButton(
    accountState: MutableState<String>,
    passwordState: MutableState<String>,
    login: (String, String) -> Unit
) {
    val text = stringResource(R.string.login)
    Button(
        onClick = {
            login(accountState.value, passwordState.value)
        },
        modifier = Modifier
            .padding(top = 8.dp)
            .semantics { contentDescription = text }
    ) {
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    AtTheme {
        LoginScreen(
            login = { _, _ -> }
        )
    }
}