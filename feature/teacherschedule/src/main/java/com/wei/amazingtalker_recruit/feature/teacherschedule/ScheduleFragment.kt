package com.wei.amazingtalker_recruit.feature.teacherschedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        viewModel.dispatch(
            ScheduleViewAction.ShowSnackBar(
                Snackbar.make(
                    root,
                    getString(
                        R.string.inquirying_teacher_calendar,
                        viewModel.states.value.currentTeacherName
                    ),
                    Snackbar.LENGTH_LONG
                )
            )
        )
    }

    override fun FragmentScheduleBinding.addOnClickListener() {
    }

    override fun FragmentScheduleBinding.handleState(
        viewLifecycleOwner: LifecycleOwner,
        states: StateFlow<ScheduleViewState>
    ) {
        states.let { states ->
            states.observeState(viewLifecycleOwner, ScheduleViewState::weekStart) {
                setPreviousWeekButtonState(it)
            }
            states.observeState(viewLifecycleOwner, ScheduleViewState::weekEnd) {
                setNextWeekButtonState(it)
            }
            states.observeState(viewLifecycleOwner, ScheduleViewState::weekDateText) {
                textWeek.text = it
                textWeek.setOnClickListener { view ->
                    //TODO 開啟日曆選單
                    viewModel.dispatch(
                        ScheduleViewAction.ShowSnackBar(
                            Snackbar.make(
                                view,
                                "TODO 開啟日曆選單, 當前星期為: $it",
                                Snackbar.LENGTH_LONG
                            )
                        )
                    )
                }
            }
            states.observeState(viewLifecycleOwner, ScheduleViewState::dateTabs) { dates ->
                tablayout.doOnLayout {
                    Timber.e("observeDateTabs: ${dates.toString()}  binding.tablayout.post ${viewModel.states.value.selectedIndex}")
                    setupTabs(dates)
                    // 延遲選擇tab
                    tablayout.post {
                        // 恢复所選的tab
                        tablayout.getTabAt(viewModel.states.value.selectedIndex)?.select()
                    }
                }
            }

            states.observeState(viewLifecycleOwner, ScheduleViewState::filteredTimeList) { result ->
                Timber.e(" filteredTimeList  $result")
                handleTimeListUpdate(result)
            }
        }
    }

    override fun FragmentScheduleBinding.handleEvent(event: ScheduleViewEvent) {
        when (event) {
            is ScheduleViewEvent.NavToScheduleDetail -> {
                findNavController().navigate(event.navigateEvent.directions)
            }

            is ScheduleViewEvent.ShowSnackBar -> {
                val snackBar = event.snackbar.setTextColor(
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
        }
    }

    override fun FragmentScheduleBinding.initData() {
        if (!TokenManager.isTokenValid) {
            findNavController().popBackStack(R.id.scheduleFragment, true)
            findNavController().navigate(DeepLinks.LOGIN)
        }
    }

    private fun FragmentScheduleBinding.handleTimeListUpdate(result: DataSourceResult<List<IntervalScheduleTimeSlot>>) {

        when (result) {
            is DataSourceResult.Success -> {
                result.data.let {
                    adapter.addHeaderAndSubmitList(
                        it
                    )
                }
                scheduleTimeRecyclerview.isVisible = true
                scheduleTimeRecyclerview.scrollToPosition(0)

                Timber.d("API Success")
            }

            is DataSourceResult.Error -> {
                viewModel.dispatch(
                    ScheduleViewAction.ShowSnackBar(
                        Snackbar.make(
                            root,
                            "Api Failed ${result.exception}",
                            Snackbar.LENGTH_LONG
                        ),
                        maxLines = 4
                    )
                )
                scheduleTimeRecyclerview.isVisible = false

                Timber.d("API Failed ${result.exception}")
            }

            is DataSourceResult.Loading -> {
                Timber.d("API Loading")
            }
        }

    }

    private fun FragmentScheduleBinding.setPreviousWeekButtonState(date: OffsetDateTime) {
        with(buttonLastWeek) {
            if (date < OffsetDateTime.now(ZoneId.systemDefault())) {
                colorFilter = null
                setOnClickListener(null)
            } else {
                setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.amazingtalker_green_900
                    )
                )
                setOnClickListener {
                    viewModel.dispatch(ScheduleViewAction.UpdateWeek(WeekAction.PREVIOUS_WEEK))
                }
            }
        }
    }

    private fun FragmentScheduleBinding.setNextWeekButtonState(date: OffsetDateTime) {
        with(buttonNextWeek) {
            setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.amazingtalker_green_900
                )
            )
            setOnClickListener {
                viewModel.dispatch(ScheduleViewAction.UpdateWeek(WeekAction.NEXT_WEEK))
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
                Timber.e("======onTabSelected====${tab.tag}")

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

    override fun onItemClick(item: IntervalScheduleTimeSlot) {
        viewModel.dispatch(ScheduleViewAction.ClickItem(item))
    }
}