package com.wei.amazingtalker.feature.login.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
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
import com.wei.amazingtalker.core.designsystem.theme.AtTheme
import com.wei.amazingtalker.feature.login.BuildConfig
import com.wei.amazingtalker.feature.login.R
import com.wei.amazingtalker.feature.login.utilities.TEST_ACCOUNT
import com.wei.amazingtalker.feature.login.utilities.TEST_PASSWORD

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
    isCompact: Boolean,
    onLoginNav: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiStates: LoginViewState by viewModel.states.collectAsStateWithLifecycle()

    LaunchedEffect(uiStates.isLoginClicked) {
        if (uiStates.isLoginClicked) {
            onLoginNav()
        }
    }

    LoginScreen(
        login = { account: String, password: String ->
            viewModel.dispatch(LoginViewAction.Login(account, password))
        },
    )
}

@Composable
internal fun LoginScreen(
    // TODO: These are test account credentials for demo purposes. Please ensure to clear them (set to "") before any release or production usage.
    account: String = if (BuildConfig.DEBUG) TEST_ACCOUNT else "",
    password: String = if (BuildConfig.DEBUG) TEST_PASSWORD else "",
    login: (String, String) -> Unit,
    withTopSpacer: Boolean = true,
    withBottomSpacer: Boolean = true,
) {
    val accountState = rememberSaveable {
        mutableStateOf(account)
    }

    val passwordState = rememberSaveable {
        mutableStateOf(password)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (withTopSpacer) {
                    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
                }
                Spacer(modifier = Modifier.weight(1.5f))
                Title()
                Spacer(modifier = Modifier.weight(3f))
                AccountTextField(accountState)
                PasswordTextField(passwordState)
                Spacer(modifier = Modifier.weight(0.5f))
                ForgotPasswordText()
                Spacer(modifier = Modifier.weight(1f))
                LoginButton(
                    accountState,
                    passwordState,
                    login,
                )
                Spacer(modifier = Modifier.weight(2f))
                if (withBottomSpacer) {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                }
            }
        }
    }
}

@Composable
private fun Title(modifier: Modifier = Modifier) {
    val title = stringResource(R.string.login)

    Text(
        text = title,
        style = MaterialTheme.typography.displayLarge,
        modifier = modifier
            .semantics { contentDescription = "" },
    )
}

@Composable
internal fun AccountTextField(
    accountState: MutableState<String>,
) {
    val account = stringResource(R.string.account)
    val accountDescription = stringResource(R.string.content_description_account)

    TextField(
        value = accountState.value,
        modifier = Modifier
            .semantics { contentDescription = accountDescription },
        onValueChange = {
            accountState.value = it
        },
        label = {
            Text(account)
        },
        singleLine = true,
    )
}

@Composable
internal fun PasswordTextField(
    passwordState: MutableState<String>,
) {
    val password = stringResource(R.string.password)
    val passwordDescription = stringResource(R.string.content_description_password)

    TextField(
        value = passwordState.value,
        modifier = Modifier
            .semantics { contentDescription = passwordDescription },
        onValueChange = {
            passwordState.value = it
        },
        label = {
            Text(password)
        },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
    )
}

@Composable
internal fun ForgotPasswordText() {
    val forgotPassword = stringResource(R.string.forgot_password)

    Text(
        text = forgotPassword,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier
            .semantics { contentDescription = forgotPassword },
    )
}

@Composable
internal fun LoginButton(
    accountState: MutableState<String>,
    passwordState: MutableState<String>,
    login: (String, String) -> Unit,
) {
    val loginText = stringResource(R.string.login)
    val loginTextDescription = stringResource(R.string.content_description_login)

    Button(
        onClick = {
            login(accountState.value, passwordState.value)
        },
        modifier = Modifier
            .padding(top = 8.dp)
            .semantics { contentDescription = loginTextDescription },
    ) {
        Text(loginText)
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    AtTheme {
        LoginScreen(
            login = { _, _ -> },
        )
    }
}
