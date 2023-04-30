package com.wei.amazingtalker_recruit.feature.teacherschedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wei.amazingtalker_recruit.feature.teacherschedule.databinding.FragmentScheduleDetailBinding
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.TEST_DATA_TEACHER_NAME
import com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels.ScheduleDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleDetailFragment : Fragment() {

    private val viewModel: ScheduleDetailViewModel by viewModels()
    private var binding: FragmentScheduleDetailBinding? = null
    private val args: ScheduleDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentScheduleDetailBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            //TODO subscribeUi()
            //TODO OnClickListener()
            tvName.text = "This is Test Data: $TEST_DATA_TEACHER_NAME"
            tvStart.text = args.teacherScheduleUnit.start.toString()
            tvEnd.text = args.teacherScheduleUnit.end.toString()
            tvState.text = args.teacherScheduleUnit.state.name
            tvDuringDayType.text = args.teacherScheduleUnit.duringDayType.name

            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}