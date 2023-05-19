package com.wei.amazingtalker_recruit.feature.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.wei.amazingtalker_recruit.core.base.BaseFragment
import com.wei.amazingtalker_recruit.core.navigation.navigate
import com.wei.amazingtalker_recruit.feature.login.databinding.FragmentLoginBinding
import com.wei.amazingtalker_recruit.feature.login.state.LoginViewAction
import com.wei.amazingtalker_recruit.feature.login.state.LoginViewEvent
import com.wei.amazingtalker_recruit.feature.login.state.LoginViewState
import com.wei.amazingtalker_recruit.feature.login.viewmodels.LoginViewModel
import kotlinx.coroutines.flow.StateFlow

class LoginFragment : BaseFragment<
        FragmentLoginBinding,
        LoginViewModel,
        LoginViewAction,
        LoginViewEvent,
        LoginViewState
        >() {

    override val viewModel: LoginViewModel by viewModels()
    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding
        get() = FragmentLoginBinding::inflate

    override fun FragmentLoginBinding.setupViews() {
    }

    override fun FragmentLoginBinding.addOnClickListener() {
        btnNavLogin.setOnClickListener {
            viewModel.dispatch(LoginViewAction.Login)
        }
    }

    override fun FragmentLoginBinding.handleState(
        viewLifecycleOwner: LifecycleOwner,
        state: StateFlow<LoginViewState>
    ) {
    }

    override fun FragmentLoginBinding.handleEvent(event: LoginViewEvent) {
        when (event) {
            is LoginViewEvent.NavToHome -> {
                findNavController().popBackStack(R.id.loginFragment, true)
                findNavController().navigate(event.navigateEvent.deepLink)
            }
        }
    }

    override fun FragmentLoginBinding.initData() {
    }

}