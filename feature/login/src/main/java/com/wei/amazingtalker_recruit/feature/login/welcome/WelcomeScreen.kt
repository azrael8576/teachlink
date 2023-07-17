package com.wei.amazingtalker_recruit.feature.login.welcome

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AtTheme
import com.wei.amazingtalker_recruit.feature.login.R
import com.wei.amazingtalker_recruit.feature.login.login.navigation.navigateToLogin
import com.wei.amazingtalker_recruit.feature.login.state.WelcomeViewState
import com.wei.amazingtalker_recruit.feature.login.viewmodels.WelcomeViewModel

@Composable
internal fun WelcomeRoute(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel = hiltViewModel(),
) {
    val uiStates: WelcomeViewState by viewModel.states.collectAsStateWithLifecycle()

    LaunchedEffect(uiStates.isInitialized) {
        if (uiStates.isInitialized) {
            navController.navigateToLogin()
        }
    }

    WelcomeScreen()
}

@Composable
internal fun WelcomeScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val welcome = stringResource(R.string.welcome)
            Text(
                text = welcome,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Center)
                    .semantics {
                        contentDescription = welcome
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    AtTheme {
        WelcomeScreen()
    }
}