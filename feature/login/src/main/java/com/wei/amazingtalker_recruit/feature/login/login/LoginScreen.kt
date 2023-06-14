package com.wei.amazingtalker_recruit.feature.login.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AppTheme
import com.wei.amazingtalker_recruit.feature.login.state.LoginViewAction
import com.wei.amazingtalker_recruit.feature.login.viewmodels.LoginViewModel

@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
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
                Button(
                    onClick = {
                        viewModel.dispatch(LoginViewAction.Login)
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Login")
                }
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    AppTheme {
        LoginScreen()
    }
}