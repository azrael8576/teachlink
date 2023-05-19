package com.wei.amazingtalker_recruit.feature.teacherschedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wei.amazingtalker_recruit.core.base.BaseFragment
import com.wei.amazingtalker_recruit.core.extensions.state.observeState
import com.wei.amazingtalker_recruit.feature.teacherschedule.databinding.FragmentScheduleDetailBinding
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleDetailViewAction
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleDetailViewEvent
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleDetailViewState
import com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels.ScheduleDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow

@AndroidEntryPoint
class ScheduleDetailFragment : BaseFragment<
        FragmentScheduleDetailBinding,
        ScheduleDetailViewModel,
        ScheduleDetailViewAction,
        ScheduleDetailViewEvent,
        ScheduleDetailViewState
        >() {

    override val viewModel: ScheduleDetailViewModel by viewModels()
    private val args: ScheduleDetailFragmentArgs by navArgs()

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScheduleDetailBinding
        get() = FragmentScheduleDetailBinding::inflate

    override fun FragmentScheduleDetailBinding.setupViews() {
    }

    override fun FragmentScheduleDetailBinding.addOnClickListener() {
        btnBack.setOnClickListener {
            viewModel.dispatch(ScheduleDetailViewAction.ClickBack)
        }
    }

    override fun FragmentScheduleDetailBinding.handleState(
        viewLifecycleOwner: LifecycleOwner,
        states: StateFlow<ScheduleDetailViewState>
    ) {
        states.let {
            states.observeState(viewLifecycleOwner, ScheduleDetailViewState::teacherName) {
                tvName.text = it.toString()
            }
            states.observeState(viewLifecycleOwner, ScheduleDetailViewState::start) {
                tvStart.text = it.toString()
            }
            states.observeState(viewLifecycleOwner, ScheduleDetailViewState::end) {
                binding.tvEnd.text = it.toString()
            }
            states.observeState(viewLifecycleOwner, ScheduleDetailViewState::state) {
                tvState.text = it?.name
            }
            states.observeState(viewLifecycleOwner, ScheduleDetailViewState::duringDayType) {
                tvDuringDayType.text = it?.name
            }
        }
    }

    override fun FragmentScheduleDetailBinding.handleEvent(event: ScheduleDetailViewEvent) {
        when (event) {
            is ScheduleDetailViewEvent.NavPopBackStack -> {
                findNavController().popBackStack()
            }
        }
    }

    override fun FragmentScheduleDetailBinding.initData() {
        viewModel.dispatch(ScheduleDetailViewAction.InitNavData(args.intervalScheduleTimeSlot))
    }

}