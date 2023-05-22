package com.wei.amazingtalker_recruit.feature.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.wei.amazingtalker_recruit.core.base.BaseFragment
import com.wei.amazingtalker_recruit.feature.login.databinding.FragmentWelcomeBinding
import com.wei.amazingtalker_recruit.feature.login.state.WelcomeViewAction
import com.wei.amazingtalker_recruit.feature.login.state.WelcomeViewEvent
import com.wei.amazingtalker_recruit.feature.login.state.WelcomeViewState
import com.wei.amazingtalker_recruit.feature.login.viewmodels.WelcomeViewModel
import kotlinx.coroutines.flow.StateFlow

class WelcomeFragment : BaseFragment<
        FragmentWelcomeBinding,
        WelcomeViewModel,
        WelcomeViewAction,
        WelcomeViewEvent,
        WelcomeViewState
        >() {

    override val viewModel: WelcomeViewModel by viewModels()

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentWelcomeBinding
        get() = FragmentWelcomeBinding::inflate


    override fun FragmentWelcomeBinding.setupViews() {
    }

    override fun FragmentWelcomeBinding.addOnClickListener() {
    }

    override fun FragmentWelcomeBinding.handleState(
        viewLifecycleOwner: LifecycleOwner,
        state: StateFlow<WelcomeViewState>
    ) {

    }

    override fun FragmentWelcomeBinding.handleEvent(event: WelcomeViewEvent) {
        when (event) {
            is WelcomeViewEvent.NavToLogin -> {
                findNavController().navigate(event.navigateEvent.directions)
            }
        }
    }

    // TODO: 此區塊應該被抽取至 ViewModel init{}
    override fun FragmentWelcomeBinding.initData() {
        viewModel.dispatch(WelcomeViewAction.NavToLogin)
    }

}