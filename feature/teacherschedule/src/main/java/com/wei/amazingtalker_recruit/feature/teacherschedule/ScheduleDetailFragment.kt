package com.wei.amazingtalker_recruit.feature.teacherschedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wei.amazingtalker_recruit.core.base.BaseFragment
import com.wei.amazingtalker_recruit.core.models.Event
import com.wei.amazingtalker_recruit.feature.teacherschedule.databinding.FragmentScheduleDetailBinding
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.TEST_DATA_TEACHER_NAME
import com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels.ScheduleDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleDetailFragment : BaseFragment<FragmentScheduleDetailBinding, ScheduleDetailViewModel>() {

    override val viewModel: ScheduleDetailViewModel by viewModels()
    private val args: ScheduleDetailFragmentArgs by navArgs()

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScheduleDetailBinding
        get() = FragmentScheduleDetailBinding::inflate

    override fun LifecycleCoroutineScope.setupObservers() {
    }

    override fun handleEvent(event: Event) {
    }

    override fun FragmentScheduleDetailBinding.setupViews() {
        tvName.text = "TeacherName: $TEST_DATA_TEACHER_NAME"
        tvStart.text = args.intervalScheduleTimeSlot.start.toString()
        tvEnd.text = args.intervalScheduleTimeSlot.end.toString()
        tvState.text = args.intervalScheduleTimeSlot.state.name
        tvDuringDayType.text = args.intervalScheduleTimeSlot.duringDayType.name
    }

    override fun initData() {
    }

    override fun FragmentScheduleDetailBinding.addOnClickListener() {
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}