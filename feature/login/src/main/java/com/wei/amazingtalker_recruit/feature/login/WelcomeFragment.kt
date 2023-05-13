package com.wei.amazingtalker_recruit.feature.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wei.amazingtalker_recruit.core.base.BaseFragment
import com.wei.amazingtalker_recruit.core.extensions.observeEvent
import com.wei.amazingtalker_recruit.core.models.Event
import com.wei.amazingtalker_recruit.core.models.NavigateEvent
import com.wei.amazingtalker_recruit.feature.login.databinding.FragmentWelcomeBinding
import com.wei.amazingtalker_recruit.feature.login.viewmodels.WelcomeViewModel

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {

    private val viewModel: WelcomeViewModel by viewModels()

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentWelcomeBinding
        get() = FragmentWelcomeBinding::inflate

    override fun setupViews() {
        with(binding) {

        }
    }

    override fun setupObservers() {
        with(viewLifecycleOwner.lifecycleScope) {
            launchWhenStarted {
                observeEvent(viewModel.events) { event ->
                    handleEvent(event)
                }
            }
        }
    }

    override fun init() {
        viewModel.navigateToLogin()
    }

    private fun handleEvent(event: Event) {
        when (event) {
            is NavigateEvent.ByDirections -> {
                findNavController().navigate(event.directions)
            }
        }
    }
}