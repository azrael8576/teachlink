package com.wei.amazingtalker_recruit.feature.login.welcome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.wei.amazingtalker_recruit.core.ui.theme.AppTheme
import com.wei.amazingtalker_recruit.core.base.BaseFragment
import com.wei.amazingtalker_recruit.core.extensions.state.observeState
import com.wei.amazingtalker_recruit.feature.login.databinding.FragmentWelcomeBinding
import com.wei.amazingtalker_recruit.feature.login.state.WelcomeViewAction
import com.wei.amazingtalker_recruit.feature.login.state.WelcomeViewState
import com.wei.amazingtalker_recruit.feature.login.viewmodels.WelcomeViewModel
import kotlinx.coroutines.flow.StateFlow

class WelcomeFragment : BaseFragment<
    FragmentWelcomeBinding,
    WelcomeViewModel,
    WelcomeViewAction,
    WelcomeViewState
    >() {

  override val viewModel: WelcomeViewModel by viewModels()

  override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentWelcomeBinding
    get() = FragmentWelcomeBinding::inflate


  override fun FragmentWelcomeBinding.setupViews() {
    composeView.apply {
      // Dispose the Composition when the view's LifecycleOwner
      // is destroyed
      setViewCompositionStrategy(
        ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
      )
      setContent {
        AppTheme {
          WelcomeScreen()
        }
      }
    }
  }

  override fun FragmentWelcomeBinding.addOnClickListener() {
  }

  override fun FragmentWelcomeBinding.handleState(
    viewLifecycleOwner: LifecycleOwner,
    states: StateFlow<WelcomeViewState>
  ) {
    states.let {
      states.observeState(
        viewLifecycleOwner,
        WelcomeViewState::isInitialized
      ) { isInitialized ->
        if (isInitialized) {
          val action = WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment()
          findNavController().navigate(action)
        }
      }
    }
  }

}