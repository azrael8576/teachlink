package com.wei.amazingtalker_recruit.feature.teacherschedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.wei.amazingtalker_recruit.core.domain.GetTeacherScheduleTimeUseCase
import com.wei.amazingtalker_recruit.core.extensions.getLocalOffsetDateTime
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.model.data.ScheduleState
import com.wei.amazingtalker_recruit.core.result.DataSourceResult
import com.wei.amazingtalker_recruit.feature.teacherschedule.adapters.OnItemClickListener
import com.wei.amazingtalker_recruit.feature.teacherschedule.adapters.ScheduleTimeListAdapter
import com.wei.amazingtalker_recruit.feature.teacherschedule.databinding.FragmentScheduleBinding
import com.wei.amazingtalker_recruit.feature.teacherschedule.utilities.TEACHER_SCHEDULE_TIME_INTERVAL
import com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels.ScheduleViewModel
import com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels.WeekAction
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleFragment : Fragment(), OnItemClickListener {

    @Inject
    lateinit var adapter: ScheduleTimeListAdapter
    @Inject
    lateinit var getTeacherScheduleTimeUseCase: GetTeacherScheduleTimeUseCase
    private val viewModel: ScheduleViewModel by viewModels()
    private var binding: FragmentScheduleBinding? = null
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
            adapter.setOnClickListener(this@ScheduleFragment)
            scheduleTimeRecyclerview.adapter = adapter
            subscribeUi(this, adapter)
            addOnTabSelectedListener(this, adapter)

            Toast.makeText(
                context,
                String.format(
                    requireContext().getString(
                        R.string.inquirying_teacher_calendar,
                        viewModel.currentTeacherNameValue.value
                    )
                ), Toast.LENGTH_SHORT
            ).show();

            setHasOptionsMenu(true)
        }
    }

    private fun addOnTabSelectedListener(
        binding: FragmentScheduleBinding,
        adapter: ScheduleTimeListAdapter
    ) {
        binding.tablayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Timber.d("======onTabSelected====${tab.tag}")

                mCurrentTabTag = tab.tag.toString()
                updateAdapterList(adapter, viewModel.teacherScheduleTimeList.value)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                Timber.d("======onTabUnselected====${tab.tag}")
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                Timber.d("======onTabReselected====${tab.tag}")
            }
        })
    }

    private fun subscribeUi(binding: FragmentScheduleBinding, adapter: ScheduleTimeListAdapter) {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.weekMondayLocalDate.collect {
                setButtonLastWeekBehavior(binding, it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.weekSundayLocalDate.collect {
                setButtonNextWeekBehavior(binding, it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.weekLocalDateText.collect {
                binding.textWeek.text = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.dateTabStringList.collect { dateTabOffsetDateTimeOptions ->
                binding.tablayout.doOnLayout {
                    putTabToTablayoutByOptions(binding, dateTabOffsetDateTimeOptions)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.currentSearchResult.collect { result ->
                when (result) {
                    is DataSourceResult.Success -> {
                        val scheduleTimeList = mutableListOf<IntervalScheduleTimeSlot>()

                        scheduleTimeList.addAll(
                            getTeacherScheduleTimeUseCase(
                                result.data.available,
                                TEACHER_SCHEDULE_TIME_INTERVAL,
                                ScheduleState.AVAILABLE
                            )
                        )
                        scheduleTimeList.addAll(
                            getTeacherScheduleTimeUseCase(
                                result.data.booked,
                                TEACHER_SCHEDULE_TIME_INTERVAL,
                                ScheduleState.BOOKED
                            )
                        )

                        viewModel.setTeacherScheduleTimeList(scheduleTimeList)
                        binding?.scheduleTimeRecyclerview?.isVisible = true

                        Timber.d("API Success")
                    }

                    is DataSourceResult.Error -> {
                        Toast.makeText(requireContext(), "Api Failed", Toast.LENGTH_SHORT).show()

                        Timber.d("API Failed DataSourceResult.Error")
                    }

                    is DataSourceResult.Loading -> {

                        Timber.d("API Loading")
                    }

                    else -> {

                        Timber.e("API Else")
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.teacherScheduleTimeList.collect {
                updateAdapterList(adapter, it)
            }
        }

    }

    private fun setButtonLastWeekBehavior(binding: FragmentScheduleBinding, it: OffsetDateTime) {
        if (it < OffsetDateTime.now(ZoneId.systemDefault())) {
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

    private fun putTabToTablayoutByOptions(
        binding: FragmentScheduleBinding,
        options: MutableList<OffsetDateTime>
    ) {
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
        teacherScheduleTimeList: List<IntervalScheduleTimeSlot>?
    ) {
        if (mCurrentTabTag.isEmpty()) return

        if (getTeacherScheduleTimeListByFilterCurrentTabLocalTime(teacherScheduleTimeList) != null) {
            getTeacherScheduleTimeListByFilterCurrentTabLocalTime(teacherScheduleTimeList)?.let {
                adapter.addHeaderAndSubmitList(
                    it
                )
            }
            binding?.scheduleTimeRecyclerview?.scrollToPosition(0)
        }
    }

    private fun getTeacherScheduleTimeListByFilterCurrentTabLocalTime(
        teacherScheduleTimeList: List<IntervalScheduleTimeSlot>?
    ): List<IntervalScheduleTimeSlot>? {
        var currentTabLocalTime =
            Instant.from(DateTimeFormatter.ISO_ZONED_DATE_TIME.parse(mCurrentTabTag))
                .atOffset(ZoneOffset.UTC)
                .getLocalOffsetDateTime()

        return teacherScheduleTimeList?.filter { item ->
            item.start.dayOfYear == currentTabLocalTime.dayOfYear
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onItemClick(item: IntervalScheduleTimeSlot) {
        //TODO nav event 抽取至 viewModel
        val action = ScheduleFragmentDirections.actionScheduleFragmentToScheduleDetailFragment(item)
        findNavController().navigate(action)
    }
}