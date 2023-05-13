package com.wei.amazingtalker_recruit.feature.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.fragment.findNavController
import com.wei.amazingtalker_recruit.core.base.BaseFragment
import com.wei.amazingtalker_recruit.core.models.Event
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.feature.login.databinding.FragmentWelcomeBinding
import com.wei.amazingtalker_recruit.feature.login.viewmodels.WelcomeViewModel

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {

    override val viewModel: WelcomeViewModel by viewModels()

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentWelcomeBinding
        get() = FragmentWelcomeBinding::inflate

    override fun LifecycleCoroutineScope.setupObservers() {
    }

    override fun FragmentWelcomeBinding.addOnClickListener() {
    }

    override fun FragmentWelcomeBinding.setupViews() {
    }

    override fun initData() {
        viewModel.navigateToLogin()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is NavigateEvent.ByDirections -> {
                findNavController().navigate(event.directions)
            }
        }
    }
}