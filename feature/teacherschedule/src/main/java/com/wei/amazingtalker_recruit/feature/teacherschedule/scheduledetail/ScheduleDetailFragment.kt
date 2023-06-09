package com.wei.amazingtalker_recruit.feature.teacherschedule.scheduledetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wei.amazingtalker_recruit.core.base.BaseFragment
import com.wei.amazingtalker_recruit.core.extensions.state.observeState
import com.wei.amazingtalker_recruit.core.ui.theme.AppTheme
import com.wei.amazingtalker_recruit.feature.teacherschedule.databinding.FragmentScheduleDetailBinding
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleDetailViewAction
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleDetailViewState
import com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels.ScheduleDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow

@AndroidEntryPoint
class ScheduleDetailFragment : BaseFragment<
        FragmentScheduleDetailBinding,
        ScheduleDetailViewModel,
        ScheduleDetailViewAction,
        ScheduleDetailViewState
        >() {

    override val viewModel: ScheduleDetailViewModel by viewModels()
    private val args: ScheduleDetailFragmentArgs by navArgs()

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScheduleDetailBinding
        get() = FragmentScheduleDetailBinding::inflate

    override fun FragmentScheduleDetailBinding.setupViews() {
        composeView.apply {
            // Dispose the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                AppTheme {
                    ScheduleDetailScreen()
                }
            }
        }
    }

    override fun FragmentScheduleDetailBinding.addOnClickListener() {
    }

    override fun FragmentScheduleDetailBinding.handleState(
        viewLifecycleOwner: LifecycleOwner,
        states: StateFlow<ScheduleDetailViewState>
    ) {
        states.let {
            states.observeState(
                viewLifecycleOwner,
                ScheduleDetailViewState::isBackClick
            ) { isBackClick ->
                if (isBackClick) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun FragmentScheduleDetailBinding.initData() {
        viewModel.dispatch(ScheduleDetailViewAction.InitNavData(args.intervalScheduleTimeSlot))
    }

}