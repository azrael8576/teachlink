package com.wei.amazingtalker_recruit

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wei.amazingtalker_recruit.adapters.ScheduleTimeListAdapter
import com.wei.amazingtalker_recruit.data.AmazingtalkerTeacherScheduleUnit
import com.wei.amazingtalker_recruit.data.Resource
import com.wei.amazingtalker_recruit.data.ScheduleUnitState
import com.wei.amazingtalker_recruit.databinding.FragmentScheduleBinding
import com.wei.amazingtalker_recruit.utilities.AMAZINGTALKER_TEACHER_SCHEDULE_INTERVAL_TIME_UNIT
import com.wei.amazingtalker_recruit.utilities.DateTimeUtils
import com.wei.amazingtalker_recruit.utilities.DateTimeUtils.getLocalOffsetDateTime
import com.wei.amazingtalker_recruit.utilities.InjectorUtils
import com.wei.amazingtalker_recruit.viewmodels.ScheduleViewModel
import com.wei.amazingtalker_recruit.viewmodels.WeekAction
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.coroutines.*
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class ScheduleFragment : Fragment() {

    private var binding: FragmentScheduleBinding? = null
    private val viewModel: ScheduleViewModel by viewModels {
        InjectorUtils.provideScheduleViewModelFactory()
    }
    private var mCurrentTabTag = ""

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
            val adapter = ScheduleTimeListAdapter()
            scheduleTimeRecyclerview.adapter = adapter
            subscribeUi(this, adapter)
            addOnTabSelectedListener(this, adapter)

            Toast.makeText(context,
                String.format(
                    requireContext().getString(R.string.inquirying_teacher_calendar, viewModel.currentTeacherNameValue.value)
                ), Toast.LENGTH_SHORT).show();

            setHasOptionsMenu(true)
        }
    }

    private fun addOnTabSelectedListener(binding: FragmentScheduleBinding, adapter: ScheduleTimeListAdapter) {
        binding.tablayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.d(TAG, "======onTabSelected====$ tab.tag")
                mCurrentTabTag = tab.tag.toString()
                updateAdapterList(adapter, viewModel.amazingtalkerTeacherScheduleUnitList.value)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                Log.d(TAG, "======onTabUnselected====$ tab.tag")
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                Log.d(TAG, "======onTabReselected====$ tab.tag")
            }
        })
    }

    private fun subscribeUi(binding: FragmentScheduleBinding, adapter: ScheduleTimeListAdapter) {
        viewModel.weekMondayLocalDate.observe(viewLifecycleOwner) {
            setButtonLastWeekBehavior(binding, it)
        }

        viewModel.weekSundayLocalDate.observe(viewLifecycleOwner) {
            setButtonNextWeekBehavior(binding, it)
        }

        viewModel.weekLocalDateText.observe(viewLifecycleOwner) {
            binding.textWeek.text = it
        }

        viewModel.dateTabStringList.observe(viewLifecycleOwner) { dateTabOffsetDateTimeOptions ->
            binding.tablayout.doOnLayout {
                putTabToTablayoutByOptions(binding, dateTabOffsetDateTimeOptions)
            }
        }

        viewModel.currentSearchResult.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        val scheduleUnitList = async {
                            val scheduleUnitList = mutableListOf<AmazingtalkerTeacherScheduleUnit>()

                            scheduleUnitList
                                .plus(DateTimeUtils.getIntervalTimeByScheduleList(it.value.availables, AMAZINGTALKER_TEACHER_SCHEDULE_INTERVAL_TIME_UNIT, ScheduleUnitState.AVAILABLE))
                                .plus(DateTimeUtils.getIntervalTimeByScheduleList(it.value.bookeds, AMAZINGTALKER_TEACHER_SCHEDULE_INTERVAL_TIME_UNIT, ScheduleUnitState.BOOKED))
                        }
                        viewModel.setAmazingtalkerTeacherScheduleUnitList(scheduleUnitList.await())
                        binding?.scheduleTimeRecyclerview?.isVisible = true
                    }
                }

                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Api Failed", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        viewModel.amazingtalkerTeacherScheduleUnitList.observe(viewLifecycleOwner) {
            updateAdapterList(adapter, it)
        }
    }

    private fun setButtonLastWeekBehavior(binding: FragmentScheduleBinding, it: OffsetDateTime) {
        if (it < OffsetDateTime.now( ZoneId.systemDefault() )) {
            binding.buttonLastWeek.colorFilter = null
            binding.buttonLastWeek.setOnClickListener(null)
        } else {
            binding.buttonLastWeek.setColorFilter(resources.getColor(R.color.amazingtalker_green_900))
            binding.buttonLastWeek.setOnClickListener(View.OnClickListener {
                binding.scheduleTimeRecyclerview.isVisible = false
                viewModel.updateWeek(WeekAction.ACTION_LAST_WEEK)
            })
        }
    }

    private fun setButtonNextWeekBehavior(binding: FragmentScheduleBinding, it: OffsetDateTime) {
        binding.buttonNextWeek.setColorFilter(resources.getColor(R.color.amazingtalker_green_900))
        binding.buttonNextWeek.setOnClickListener(View.OnClickListener {
            binding.scheduleTimeRecyclerview.isVisible = false
            viewModel.updateWeek(WeekAction.ACTION_NEXT_WEEK)
        })
    }

    private fun putTabToTablayoutByOptions(binding: FragmentScheduleBinding, options: MutableList<OffsetDateTime>) {
        val tabWidth = binding.tablayout.width / 3
        val tabHeight = binding.tablayout.height
        binding.tablayout.removeAllTabs()
        options.forEachIndexed { _, element ->
            binding.tablayout.newTab().run {
                setCustomView(R.layout.custom_tab)
                customView?.minimumWidth = tabWidth
                customView?.minimumHeight = tabHeight
                tag = element
                val dateFormatter = DateTimeFormatter.ofPattern("E, MMM dd")
                text = dateFormatter.format(element)
                binding.tablayout.addTab(this)
            }
        }
    }

    private fun updateAdapterList(
        adapter: ScheduleTimeListAdapter,
        teacherScheduleUnitList: List<AmazingtalkerTeacherScheduleUnit>?
    ) {
        if (mCurrentTabTag.isEmpty()) return

        if (getTeacherScheduleUnitListByFilterCurrentTabLocalTime(teacherScheduleUnitList) != null) {
            getTeacherScheduleUnitListByFilterCurrentTabLocalTime(teacherScheduleUnitList)?.let {
                adapter.addHeaderAndSubmitList(
                    it
                )
            }
            binding?.scheduleTimeRecyclerview?.scrollToPosition(0)
        }
    }

    private fun getTeacherScheduleUnitListByFilterCurrentTabLocalTime(
        teacherScheduleUnitList: List<AmazingtalkerTeacherScheduleUnit>?
    ): List<AmazingtalkerTeacherScheduleUnit>? {
        var currentTabLocalTime = Instant.from(DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(mCurrentTabTag))
            .atOffset(ZoneOffset.UTC)
            .getLocalOffsetDateTime()

         return teacherScheduleUnitList?.filter {item ->
            item.start.dayOfYear == currentTabLocalTime.dayOfYear
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}