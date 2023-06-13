package com.wei.amazingtalker_recruit.feature.login.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.wei.amazingtalker_recruit.core.base.BaseFragment
import com.wei.amazingtalker_recruit.core.designsystem.ui.theme.AppTheme
import com.wei.amazingtalker_recruit.core.extensions.state.observeState
import com.wei.amazingtalker_recruit.core.navigation.DeepLinks
import com.wei.amazingtalker_recruit.core.navigation.navigate
import com.wei.amazingtalker_recruit.feature.login.R
import com.wei.amazingtalker_recruit.feature.login.databinding.FragmentLoginBinding
import com.wei.amazingtalker_recruit.feature.login.state.LoginViewAction
import com.wei.amazingtalker_recruit.feature.login.state.LoginViewState
import com.wei.amazingtalker_recruit.feature.login.viewmodels.LoginViewModel
import kotlinx.coroutines.flow.StateFlow

class LoginFragment : BaseFragment<
        FragmentLoginBinding,
        LoginViewModel,
        LoginViewAction,
        LoginViewState
        >() {

    override val viewModel: LoginViewModel by viewModels()
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding
        get() = FragmentLoginBinding::inflate

    override fun FragmentLoginBinding.setupViews() {
        composeView.apply {
            // Dispose the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                AppTheme {
                    LoginScreen()
                }
            }
        }
    }

    override fun FragmentLoginBinding.addOnClickListener() {
    }

    override fun FragmentLoginBinding.handleState(
        viewLifecycleOwner: LifecycleOwner,
        states: StateFlow<LoginViewState>
    ) {
        states.let {
            states.observeState(
                viewLifecycleOwner,
                LoginViewState::isUserLoggedIn
            ) { isUserLoggedIn ->
                if (isUserLoggedIn) {
                    findNavController().popBackStack(R.id.loginFragment, true)
                    findNavController().navigate(DeepLinks.HOME)
                }
            }
        }
    }

}