package com.wei.amazingtalker_recruit.feature.login.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.wei.amazingtalker_recruit.feature.login.state.LoginViewAction

@Composable
fun LoginScreen(onLoginClick: (() -> Unit)? = null) {
  Box(
    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        text = "跨功能模組導航:\n Home Module",
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.align(Alignment.CenterHorizontally)
      )
      Button(
        onClick = {
          if (onLoginClick != null) {
            onLoginClick()
          } else {
          }
        },
        modifier = Modifier.padding(top = 8.dp)
      ) {
        Text("Login")
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