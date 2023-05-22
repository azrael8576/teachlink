package com.wei.amazingtalker_recruit.feature.teacherschedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.wei.amazingtalker_recruit.core.authentication.TokenManager
import com.wei.amazingtalker_recruit.core.base.BaseFragment
import com.wei.amazingtalker_recruit.core.extensions.state.observeState
import com.wei.amazingtalker_recruit.core.model.data.IntervalScheduleTimeSlot
import com.wei.amazingtalker_recruit.core.navigation.DeepLinks
import com.wei.amazingtalker_recruit.core.navigation.navigate
import com.wei.amazingtalker_recruit.core.result.DataSourceResult
import com.wei.amazingtalker_recruit.feature.teacherschedule.adapters.OnItemClickListener
import com.wei.amazingtalker_recruit.feature.teacherschedule.adapters.ScheduleTimeListAdapter
import com.wei.amazingtalker_recruit.feature.teacherschedule.databinding.FragmentScheduleBinding
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleViewAction
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleViewEvent
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.ScheduleViewState
import com.wei.amazingtalker_recruit.feature.teacherschedule.state.WeekAction
import com.wei.amazingtalker_recruit.feature.teacherschedule.viewmodels.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@AndroidEntryPoint
class ScheduleFragment
    : BaseFragment<
        FragmentScheduleBinding,
        ScheduleViewModel,
        ScheduleViewAction,
        ScheduleViewEvent,
        ScheduleViewState
        >(), OnItemClickListener {

    @Inject
    lateinit var adapter: ScheduleTimeListAdapter
    override val viewModel: ScheduleViewModel by viewModels()

    override val inflate: (LayoutInflater, ViewGroup?, Boolean) -> FragmentScheduleBinding
        get() = FragmentScheduleBinding::inflate

    override fun FragmentScheduleBinding.setupViews() {
        adapter.setOnClickListener(this@ScheduleFragment)
        scheduleTimeRecyclerview.adapter = adapter
        buttonLastWeek.setActiveColorFilter()
        buttonNextWeek.setActiveColorFilter()
    }

    private fun ImageButton.setActiveColorFilter() {
        setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.amazingtalker_green_900
            )
        )
    }

    override fun FragmentScheduleBinding.addOnClickListener() {
        buttonLastWeek.setOnClickListener {
            viewModel.dispatch(
                ScheduleViewAction.UpdateWeek(
                    WeekAction.PREVIOUS_WEEK
                )
            )
        }
        buttonNextWeek.setOnClickListener {
            viewModel.dispatch(
                ScheduleViewAction.UpdateWeek(
                    WeekAction.NEXT_WEEK
                )
            )
        }
    }

    override fun FragmentScheduleBinding.handleState(
        viewLifecycleOwner: LifecycleOwner,
        states: StateFlow<ScheduleViewState>
    ) {
        states.let { states ->
            states.observeState(viewLifecycleOwner, ScheduleViewState::weekStart) {
                setPreviousWeekButtonState(it)
            }

            states.observeState(viewLifecycleOwner, ScheduleViewState::weekDateText) {
                setWeekDateTextState(it)

            }
            states.observeState(viewLifecycleOwner, ScheduleViewState::dateTabs) { dates ->
                setDateTabsState(dates)

            }

            states.observeState(viewLifecycleOwner, ScheduleViewState::filteredTimeList) { list ->
                handleTimeListUpdate(list)
            }

            states.observeState(viewLifecycleOwner, ScheduleViewState::filteredStatus) { status ->
                handleFilteredStatus(status)
            }
        }
    }

    override fun FragmentScheduleBinding.handleEvent(event: ScheduleViewEvent) {
        Timber.e("handleEvent $event")
        when (event) {
            is ScheduleViewEvent.NavToScheduleDetail -> {
                findNavController().navigate(event.navigateEvent.directions)
            }

            is ScheduleViewEvent.ShowSnackBar -> {
                val message = if (event.resId != 0) getString(R.string.inquirying_teacher_calendar, event.message) else event.message

                val snackBar = Snackbar.make(
                    binding.root,
                    message,
                    event.duration
                ).setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.amazingtalker_green_700
                    )
                )

                val snackBarView = snackBar.view
                val snackTextView =
                    snackBarView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
                snackTextView.maxLines = event.maxLines
                snackBar.show()
            }

            is ScheduleViewEvent.NavPopToLogin -> {
                findNavController().popBackStack(R.id.scheduleFragment, true)
                findNavController().navigate(DeepLinks.LOGIN)
            }
        }
    }

    override fun FragmentScheduleBinding.checkConditions() {
        Timber.e("checkConditions TokenManager.isTokenValid: ${TokenManager.isTokenValid}")
        if (!TokenManager.isTokenValid) {
            viewModel.dispatch(ScheduleViewAction.IsInvalidToken)
        }
    }

    private fun FragmentScheduleBinding.setPreviousWeekButtonState(date: OffsetDateTime) {
        with(buttonLastWeek) {
            if (date < OffsetDateTime.now(ZoneId.systemDefault())) {
                colorFilter = null
                setOnClickListener(null)
            } else {
                setActiveColorFilter()
                setOnClickListener {
                    viewModel.dispatch(ScheduleViewAction.UpdateWeek(WeekAction.PREVIOUS_WEEK))
                }
            }
        }
    }

    private fun FragmentScheduleBinding.setWeekDateTextState(weekDate: String) {
        textWeek.text = weekDate
        textWeek.setOnClickListener {
            //TODO 開啟日曆選單
            viewModel.dispatch(
                ScheduleViewAction.ShowSnackBar(
                    "開啟日曆選單: $weekDate",
                    Snackbar.LENGTH_LONG
                )
            )
        }
    }

    private fun FragmentScheduleBinding.setDateTabsState(dates: MutableList<OffsetDateTime>) {
        tablayout.doOnLayout {
            setupTabs(dates)
            // 延遲選擇tab
            tablayout.post {
                // 恢复所選的tab
                tablayout.getTabAt(viewModel.states.value.selectedIndex)?.select()
            }
        }
    }

    private fun FragmentScheduleBinding.setupTabs(
        options: MutableList<OffsetDateTime>
    ) {
        with(tablayout) {
            val tabWidth = width / 3
            val tabHeight = height
            // 移除所有 Tabs
            removeAllTabs()
            options.forEachIndexed { _, element ->
                newTab().run {
                    setCustomView(R.layout.custom_tab)
                    customView?.minimumWidth = tabWidth
                    customView?.minimumHeight = tabHeight
                    tag = element
                    val dateFormatter = DateTimeFormatter.ofPattern("E, MMM dd")
                    text = dateFormatter.format(element)
                    addTab(this)
                }
            }
            // 重新添加 TabSelectedListener
            addTabSelectedListener(tablayout)
        }
    }

    private fun addTabSelectedListener(tabLayout: TabLayout) {
        tabLayout.clearOnTabSelectedListeners()
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                Timber.d("======onTabSelected====${tab.tag}")

                val date = tab.tag as? OffsetDateTime
                if (date != null) {
                    viewModel.dispatch(ScheduleViewAction.SelectedTab(date, tab.position))
                } else {
                    Timber.e("Invalid tab tag, expected OffsetDateTime but got ${tab.tag?.javaClass?.name}")
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun FragmentScheduleBinding.handleTimeListUpdate(list: List<IntervalScheduleTimeSlot>) {
        list.let {
            adapter.addHeaderAndSubmitList(
                it
            )
        }
    }

    private fun FragmentScheduleBinding.handleFilteredStatus(dataSourceResult: DataSourceResult<List<IntervalScheduleTimeSlot>>) {

        when (dataSourceResult) {
            is DataSourceResult.Success -> {
                scheduleTimeRecyclerview.isVisible = true
                scheduleTimeRecyclerview.scrollToPosition(0)
            }

            is DataSourceResult.Error -> {
                scheduleTimeRecyclerview.isVisible = false
            }

            is DataSourceResult.Loading -> {
                Timber.d("API Loading")
            }
        }
    }

    override fun onItemClick(item: IntervalScheduleTimeSlot) {
        viewModel.dispatch(ScheduleViewAction.ClickItem(item))
    }
}