package com.wei.amazingtalker_recruit.feature.teacherschedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wei.amazingtalker_recruit.core.base.BaseFragment
import com.wei.amazingtalker_recruit.feature.teacherschedule.databinding.FragmentScheduleDetailBinding
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleDetailViewAction
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleDetailViewEvent
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleDetailViewState
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.TEST_DATA_TEACHER_NAME
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
        tvName.text = "TeacherName: $TEST_DATA_TEACHER_NAME"
        tvStart.text = args.intervalScheduleTimeSlot.start.toString()
        tvEnd.text = args.intervalScheduleTimeSlot.end.toString()
        tvState.text = args.intervalScheduleTimeSlot.state.name
        tvDuringDayType.text = args.intervalScheduleTimeSlot.duringDayType.name
    }

    override fun FragmentScheduleDetailBinding.addOnClickListener() {
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun handleState(
        viewLifecycleOwner: LifecycleOwner,
        state: StateFlow<ScheduleDetailViewState>
    ) {
    }

    override fun handleEvent(event: ScheduleDetailViewEvent) {
    }

    override fun initData() {
    }

}