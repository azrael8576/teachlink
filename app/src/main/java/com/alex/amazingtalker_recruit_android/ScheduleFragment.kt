package com.alex.amazingtalker_recruit_android

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.alex.amazingtalker_recruit_android.databinding.FragmentScheduleBinding
import com.alex.amazingtalker_recruit_android.utilities.InjectorUtils
import com.alex.amazingtalker_recruit_android.utilities.TEST_DATA_TEACHER_NAME
import com.alex.amazingtalker_recruit_android.utilities.TEST_DATA_TIME_STARTED_AT
import com.alex.amazingtalker_recruit_android.viewmodels.ScheduleViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ScheduleFragment : Fragment() {

    private var binding: FragmentScheduleBinding? = null
    private var searchJob: Job? = null
    private val viewModel: ScheduleViewModel by viewModels {
        InjectorUtils.provideScheduleViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentScheduleBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            //TODO adapter
            subscribeUi()

            setHasOptionsMenu(true)
        }
    }

    private fun subscribeUi() {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.getAmazingtalkerTeacherScheduleResponse(TEST_DATA_TEACHER_NAME, TEST_DATA_TIME_STARTED_AT).collect{
            // TODO adapter.submitList()
            // Update View with the latest favorite news
                Log.e(TAG, "subscribeUi: availables ${it.availables.toString()}", )
                Log.e(TAG, "subscribeUi: bookeds ${it.bookeds.toString()}", )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}