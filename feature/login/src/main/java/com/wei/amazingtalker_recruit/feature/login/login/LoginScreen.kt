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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AtTheme
import com.wei.amazingtalker_recruit.feature.login.state.LoginViewAction
import com.wei.amazingtalker_recruit.feature.login.state.LoginViewState
import com.wei.amazingtalker_recruit.feature.login.viewmodels.LoginViewModel

@Composable
internal fun LoginRoute(
    onLoginClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiStates: LoginViewState by viewModel.states.collectAsStateWithLifecycle()

    LaunchedEffect(uiStates.isUserLoggedIn) {
        if (uiStates.isUserLoggedIn) {
            onLoginClick()
        }
    }

    LoginScreen(
        uiStates = uiStates,
        setAccount = { account: String ->
            viewModel.dispatch(LoginViewAction.SetAccount(account))
        },
        setPassword = { password: String ->
            viewModel.dispatch(LoginViewAction.SetPassword(password))
        },
        login = {
            viewModel.dispatch(LoginViewAction.Login)
        }
    )
}

@Composable
internal fun LoginScreen(
    uiStates: LoginViewState,
    setAccount: (String) -> Unit,
    setPassword: (String) -> Unit,
    login: () -> Unit,
) {
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
                Text(
                    text = "跨功能模組導航:\n Home Module",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = uiStates.account,
                    onValueChange = {
                        setAccount(it)
                    },
                    label = {
                        Text("Account")
                    },
                    singleLine = true,
                )
                TextField(
                    value = uiStates.password,
                    onValueChange = {
                        setPassword(it)
                    },
                    label = {
                        Text("Password")
                    },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        login()
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Login")
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    AtTheme {
        LoginScreen(
            uiStates = LoginViewState(),
            setAccount = {},
            setPassword = {},
            login = {}
        )
    }
}